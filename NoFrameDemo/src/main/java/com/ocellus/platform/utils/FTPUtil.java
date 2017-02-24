package com.ocellus.platform.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FTPUtil {
    private static final Logger logger = Logger.getLogger(FTPUtil.class);
    private static List ASCII_LIST = new ArrayList();

    public static final String MODE_ASCII = "Ascii";
    public static final String MODE_BINARY = "Binary";
    public static final String PASSIVE_CONNECTION_TYPE = "Passive";
    public static final String ACTIVE_CONNECTION_TYPE = "Active";

    static {
        ASCII_LIST.add("xml");
        // ASCII_LIST.add("cgi");
        // ASCII_LIST.add("css");
        // ASCII_LIST.add("htm");
        // ASCII_LIST.add("html");
        // ASCII_LIST.add("js");
        // ASCII_LIST.add("php");
        // ASCII_LIST.add("pl");
        // ASCII_LIST.add("shtml");
        // ASCII_LIST.add("text");
        // ASCII_LIST.add("txt");
    }

    private FTPUtil() {
    }

    public static List<String> getFile(String url, int port, String username, String password,
                                       String localDir, String directory, String fileSpec, String movedToDirectory,
                                       String fileMode, String siteCommand, boolean overwrite,
                                       boolean deleteFile) throws Exception {
        return getFile(url, port, username, password, localDir, directory, fileSpec, movedToDirectory,
                fileMode, siteCommand, overwrite, deleteFile, null);

    }

    public static List<String> getFile(String url, int port, String username, String password,
                                       String localDir, String directory, String fileSpec, String movedToDirectory,
                                       String fileMode, String siteCommand, boolean overwrite,
                                       boolean deleteFile, String connectionType) throws Exception {
        FTPClient ftp = connect(url, port, username, password, directory, siteCommand, connectionType);
        List<String> locFilePath = new ArrayList<String>();
        if (ftp != null && ftp.isConnected()) {
            OutputStream outputStream = null;
            try {
                logger.info("ftp connected to host: " + url);
                if (!new File(localDir).isDirectory())
                    localDir = new File(localDir).getParent();

                FTPFile[] files = ftp.listFiles(fileSpec);
                if (files.length == 0)
                    throw new Exception("Files not found for " + fileSpec);

                logger.info("files to get: " + (files.length - (files[files.length - 1] == null ? 1 : 0)));
                for (int i = 0; i < files.length; i++) {
                    FTPFile f = files[i];
                    if (f != null && f.isFile()) {
                        String fileName = f.getName();
                        if (fileName.indexOf("/") != -1) {
                            fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
                            logger.info("adjusted filename: " + fileName);
                        }
                        File localFile = new File(localDir, fileName);
                        if (files.length == 1 && fileMode != null && !"".equals(fileMode)) {
                            if (MODE_ASCII.equalsIgnoreCase(fileMode))
                                ftp.setFileType(FTPClient.ASCII_FILE_TYPE);
                            else if (MODE_BINARY.equalsIgnoreCase(fileMode))
                                ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                        } else {
                            if (ASCII_LIST.contains(fileName.lastIndexOf(".") > 0 ? fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase() : ""))
                                ftp.setFileType(FTPClient.ASCII_FILE_TYPE);
                            else
                                ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                        }

                        if (!overwrite && localFile.exists())
                            throw new Exception("File already exists and overwrite is false");

                        logger.info("getting file: " + fileName);
                        outputStream = new BufferedOutputStream(new FileOutputStream(localFile));
                        if (!ftp.retrieveFile(fileName, outputStream)) {
                            outputStream.close();
                            localFile.delete();
                            throw new Exception("retrieve failed: " + ftp.getReplyString());
                        } else {
                            outputStream.close();
                            locFilePath.add(localDir + "/" + fileName);
                            logger.info("got file: " + fileName);

                            if (!ftp.makeDirectory(movedToDirectory))
                                logger.info("Directory: " + movedToDirectory + "is existed");
                            boolean move = ftp.rename(fileName, movedToDirectory + File.separator + fileName);
                            if (move)
                                logger.info("move from " + directory + File.separator + fileName + " to " + movedToDirectory + File.separator + fileName);
                            else
                                throw new Exception("move failed: " + ftp.getReplyString());
                            if (deleteFile) {
                                String deleteFilePath = ftp.printWorkingDirectory() + "/" + f.getName();
                                try {
                                    boolean deletedFile = ftp.deleteFile(deleteFilePath);
                                    if (!deletedFile)
                                        logger.info("deleting of downloaded file: " + f.getName() + " failed.");
                                    else
                                        logger.info("After download, deleted the file: " + deleteFilePath);
                                } catch (Exception e2) {
                                    e2.printStackTrace();
                                }
                            }

                        }
                    }
                }
            } catch (Exception ivose) {
                throw ivose;
            } finally {
                try {
                    IOUtils.closeQuietly(outputStream);
                    ftp.disconnect();
                } catch (IOException f) {
                } // do nothing
            }
        } else
            throw new Exception("Unable to connect to: " + url + " with configured login settting.");
        return locFilePath;
    }

    public static FTPClient connect(String url, int port, String username,
                                    String password, String directory, String siteCommand)
            throws Exception {
        return connect(url, port, username, password, directory, siteCommand, null);
    }

    public static FTPClient connect(String url, int port, String username,
                                    String password, String directory, String siteCommand,
                                    String connectionType) throws Exception {
        FTPClient ftp = new FTPClient();

        try {
            logger.info("connecting: " + url);
            ftp.connect(url, port);

            // After connection attempt, you should check the reply code to
            // verify
            // success.
            int reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                logger.error("FTP server refused connection: " + ftp.getReplyString());
                throw new Exception("FTP server refused connection: " + ftp.getReplyString());
            }

            logger.info("login with: " + username);
            if (!ftp.login(username, password))
                throw new Exception("Unable to login: " + ftp.getReplyString());
            logger.info("logged in: " + username);
            if (PASSIVE_CONNECTION_TYPE.equals(connectionType)) {
                ftp.enterLocalPassiveMode();
                if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                    logger.info("Negative response");
                } else {
                    logger.info("FTP In Passive Mode...");
                }
            }

            logger.info("remote directory: " + directory);
            if (directory != null && !"".equals(directory.trim())) {
                ftp.changeWorkingDirectory(directory);
                if (!FTPReply.isPositiveCompletion(ftp.getReplyCode()))
                    throw new Exception("could not change remote directory: " + ftp.getReplyString());
                logger.info("changed to directory: " + directory);
            }

            if (siteCommand != null && !"".equals(siteCommand)) {
                logger.info("send site command: " + siteCommand);
                ftp.sendSiteCommand(siteCommand);
                {
                    logger.info("reply code for site command: "
                            + new Integer(ftp.getReplyCode()).toString());
                    logger.info("reply string for site command: "
                            + ftp.getReplyString());
                }
            }
        } catch (IOException e) {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException f) {
                } // do nothing
            }
            e.printStackTrace();
            throw new Exception(e);
        }

        return ftp;
    }

    public static boolean fileExists(String[] filenames, String filename) {
        for (int i = 0; i < filenames.length; i++) {
            if (filename.equalsIgnoreCase(filenames[i]))
                return true;
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
        String url = "10.112.4.97";
        String username = "chen.shaojia";
        String password = "p@ssworD";
        int port = 22;
        // sendFile("66.206.172.99", "cendant", "avis", "devTest", new
        // File("C:/Documents and Settings/rbeeler/Desktop/ftp/ftp site.txt"),
        // true);

        // "devTest", new
        // File("C:/Documents and Settings/rbeeler/Desktop/ftp/ftp site.txt"),
        // true));
        getFile(url, port, username, password, "D:/", "Temp/tms", "", DateUtil.format(new Date(), "yyyyMMdd"), "", "", true, false);
    }
}