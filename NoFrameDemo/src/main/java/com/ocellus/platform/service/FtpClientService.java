package com.ocellus.platform.service;

import com.ocellus.platform.utils.StringUtil;
import com.ocellus.platform.utils.SystemConfigUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class FtpClientService {
    private static final Logger logger = Logger.getLogger(FTPClient.class);

    private static List<String> ASCII_LIST = new ArrayList<String>();
    private static final String PASSIVE_CONNECTION_TYPE = "Passive";
    private static final String ACTIVE_CONNECTION_TYPE = "Active";

    static {
        ASCII_LIST.add("xml");
        ASCII_LIST.add("cgi");
        ASCII_LIST.add("css");
        ASCII_LIST.add("htm");
        ASCII_LIST.add("html");
        ASCII_LIST.add("js");
        ASCII_LIST.add("php");
        ASCII_LIST.add("pl");
        ASCII_LIST.add("shtml");
        ASCII_LIST.add("text");
        ASCII_LIST.add("txt");
    }

    protected FTPClient openConnection() throws Exception {
        String userName = SystemConfigUtil.getProperty("ftp.userName");
        String password = SystemConfigUtil.getProperty("ftp.password");
        String hostName = SystemConfigUtil.getProperty("ftp.hostUrl");
        int port = Integer.parseInt(SystemConfigUtil.getProperty("ftp.port", "22"));
        String passiveMode = SystemConfigUtil.getProperty("ftp.passiveMode");
        String siteCommand = SystemConfigUtil.getProperty("ftp.siteCommand");
        FTPClient client = new FTPClient();

        try {
            logger.info("connecting: " + hostName);
            client.connect(hostName, port);
            logger.info("login with: " + userName);
            if (!client.login(userName, password))
                throw new Exception("Error logging in, bad reply: " + client.getReplyString());

            if (PASSIVE_CONNECTION_TYPE.equalsIgnoreCase(passiveMode)) {
                client.enterLocalPassiveMode();

                if (!FTPReply.isPositiveCompletion(client.getReplyCode()))
                    logger.info("Could not enter local passive mode, continuing");
                else
                    logger.info("FTP In Passive Mode...");
            }

            if (!StringUtil.isEmpty(siteCommand)) {
                client.sendSiteCommand(siteCommand);
                logger.info("Sent SITE command (" + siteCommand + ") with response code (" + client.getReplyCode() + ") and text (" + client.getReplyString() + ").");
            }
        } catch (Exception e) {
            if (client.isConnected()) {
                try {
                    client.disconnect();
                } catch (Exception e2) {
                    //do nothing
                }
            }
            throw e;
        }
        return client;
    }

    protected void closeConnection(FTPClient connection) throws Exception {
        connection.disconnect();
    }

    public void uploadFile(File[] fromFiles, String toDirectory, boolean overwrite) throws Exception {
        FTPClient ftp = openConnection();
        if (ftp != null && ftp.isConnected()) {
            try {
                if (!overwrite) {
                    List<String> remoteFiles = list(ftp, toDirectory);
                    List<String> remoteFileNameList = new ArrayList<String>();
                    for (String remoteFileName : remoteFiles) {
                        remoteFileNameList.add(FilenameUtils.getName(remoteFileName));
                    }

                    List<File> filesToBeOverwritten = new ArrayList<File>();
                    for (File fromFile : fromFiles) {
                        if (remoteFileNameList.contains(fromFile.getName()))
                            filesToBeOverwritten.add(fromFile);
                    }

                    if (!filesToBeOverwritten.isEmpty())
                        throw new Exception("Skipping upload - File(s) would be overwritten, but overwrite = false for files: " + filesToBeOverwritten);
                }

                try {
                    for (File inputFile : fromFiles) {
                        doUpload(ftp, inputFile, toDirectory);
                    }
                } catch (Exception e) {
                    throw new Exception("Error uploading file (" + fromFiles + ") to remote (" + toDirectory + ").", e);
                }
            } catch (Exception e) {
                throw e;
            } finally {
                closeConnection(ftp);
            }
        }
    }

    public void downloadFile(String[] remoteFiles, String toDirectory, boolean overwrite) throws Exception {
        FTPClient ftp = openConnection();
        if (ftp != null && ftp.isConnected()) {
            try {
                List<String> resolvedRemoteFiles = new ArrayList<String>();
                for (String remoteFileSpec : remoteFiles) {
                    if (remoteFileSpec.contains("*") || remoteFileSpec.contains("?")) {
                        List<String> remoteFilesInDir = list(ftp, FilenameUtils.getFullPath(remoteFileSpec));
                        if (remoteFilesInDir != null) {
                            logger.info("FileClient.download() remotesFilesInDir=" + remoteFilesInDir);
                        } else {
                            logger.info("FileClient.download() remotesFilesInDir is null.");
                        }
                        for (String remoteFileInDir : remoteFilesInDir) {
                            if (FilenameUtils.wildcardMatch(FilenameUtils.getName(remoteFileInDir), FilenameUtils.getName(remoteFileSpec))) {
                                resolvedRemoteFiles.add(remoteFileInDir);
                                if (remoteFileInDir != null && remoteFileSpec != null) {
                                    logger.info("FileClient.download() wildcardMatch is true. remoteFileInDir:" + FilenameUtils.getName(remoteFileInDir) + " remoteFileSpec:" + FilenameUtils.getName(remoteFileSpec));
                                }
                            } else {
                                if (remoteFileInDir != null && remoteFileSpec != null) {
                                    logger.info("FileClient.download() wildcardMatch is false remoteFileInDir:" + FilenameUtils.getName(remoteFileInDir) + " remoteFileSpec:" + FilenameUtils.getName(remoteFileSpec));
                                }
                            }
                        }
                    } else {
                        List<String> remoteFilesInDir = list(ftp, FilenameUtils.getFullPath(remoteFileSpec));
                        for (String remoteFileInDir : remoteFilesInDir) {
                            if (FilenameUtils.getName(remoteFileInDir).equals(FilenameUtils.getName(remoteFileSpec)))
                                resolvedRemoteFiles.add(remoteFileSpec);
                        }
                    }
                }

                if (resolvedRemoteFiles.isEmpty())
                    throw new Exception("No files found to download that match the spec");

                if (overwrite == false) {
                    List<File> filesToBeOverwritten = new ArrayList<File>();
                    for (String remoteFile : resolvedRemoteFiles) {
                        String namePart = new File(remoteFile).getName();
                        File toCheck = new File(toDirectory, namePart);
                        if (toCheck.exists())
                            filesToBeOverwritten.add(toCheck);
                    }

                    if (filesToBeOverwritten.isEmpty() == false)
                        throw new Exception("Skipping download - File(s) would be overwritten, but overwrite = false for files: " + filesToBeOverwritten);
                }

                try {
                    for (String remoteFile : resolvedRemoteFiles) {
                        doDownload(ftp, remoteFile, toDirectory);
                    }
                } catch (Exception e) {
                    throw new Exception("Error downloading file (" + remoteFiles + ") to location (" + toDirectory + ").", e);
                }
            } catch (Exception e) {
                throw e;
            } finally {
                closeConnection(ftp);
            }
        }
    }

    public void removeFile(String[] remoteFiles) throws Exception {
        FTPClient ftp = openConnection();
        try {
            for (String remoteFile : remoteFiles) {
                ftp.deleteFile(remoteFile);
            }
        } catch (Exception e) {
            throw new Exception("Error removing file (" + remoteFiles + ").", e);
        } finally {
            closeConnection(ftp);
        }
    }

    protected List<String> list(FTPClient connection, String remoteDirectory) throws Exception {
        List<String> fileList = new ArrayList<String>();
        FTPFile[] fileArray = connection.listFiles(remoteDirectory);
        for (FTPFile file : fileArray) {
            if (file.isDirectory())
                continue;
            else
                fileList.add(remoteDirectory + file.getName());
        }
        return fileList;
    }

    protected void doDownload(FTPClient connection, String remoteFile, String toDirectory) throws Exception {
        String fileName = new File(remoteFile).getName();
        setFileTransferTypeOnConnection(connection, fileName);
        FileOutputStream fo = new FileOutputStream(new File(toDirectory, fileName));
        connection.retrieveFile(remoteFile, fo);
        fo.flush();
        fo.close();
    }

    protected void doUpload(FTPClient connection, File inputFile, String toDirectory) throws Exception {
        setFileTransferTypeOnConnection(connection, inputFile.getName());
        FileInputStream is = new FileInputStream(inputFile);
        String fileName = inputFile.getName();
        connection.storeFile(toDirectory + "/" + fileName, is);
    }

    private void setFileTransferTypeOnConnection(FTPClient connection, String fileName) throws Exception {
        if (ASCII_LIST.contains(fileName.lastIndexOf(".") > 0 ? fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase() : ""))
            connection.setFileType(FTPClient.ASCII_FILE_TYPE);
        else
            connection.setFileType(FTPClient.BINARY_FILE_TYPE);
    }
}
