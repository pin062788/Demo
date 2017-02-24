package com.ocellus.platform.utils;

import org.apache.log4j.Logger;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class IOUtil {
    private static Logger		logger		= Logger.getLogger(IOUtil.class);

    public static String getInputStreamAsString(InputStream in, boolean close) throws IOException {
        if (in == null) return null;
        StringBuffer result = new StringBuffer();
        int bufferSize = 1000;
        byte[] buffer = new byte[bufferSize];
        int bytesRead = -1;
        while ((bytesRead = in.read(buffer)) != -1)
            result.append(new String(buffer, 0, bytesRead));
        if (close) {
            in.close();
        }

        return result.toString();
    }

    /**
     * 删除文件，可以是文件或文件夹
     *
     * @param fileName
     *            要删除的文件名
     * @return 删除成功返回true，否则返回false
     */
    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            logger.info("删除文件失败:" + fileName + "不存在！");
            return false;
        } else {
            if (file.isFile())
                return deleteFile(fileName);
            else
                return deleteDirectory(fileName);
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName
     *            要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                logger.info("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                logger.info("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            logger.info("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }
    /**
     * 删除目录及目录下的文件
     *
     * @param dir
     *            要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            logger.info("删除目录失败：" + dir + "不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = IOUtil.deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            } else if (files[i].isDirectory()) {// 删除子目录
                flag = IOUtil.deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            logger.info("删除目录失败！");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            logger.info("删除目录" + dir + "成功！");
            return true;
        } else {
            return false;
        }
    }

    public static void copyFile(String inputFile, String outputFile) throws IOException {
        logger.info("copyFile(String inputFile=" + inputFile + ", String outputFile=" + outputFile + ") - start");

        File sFile = new File(inputFile);
        File tFile = new File(outputFile);
        if(!tFile.exists()){
            File pf = tFile.getParentFile();
            if(!pf.exists()){
                pf.mkdirs();
            }
            tFile.createNewFile();
        }
        FileInputStream fis = new FileInputStream(sFile);
        FileOutputStream fos = new FileOutputStream(tFile);
        int temp = 0;
        try {
            while ((temp = fis.read()) != -1) {
                fos.write(temp);
            }
        } catch (IOException e) {
            logger.error("copyFile()", e);
        } finally {
            try {
                fis.close();
                fos.close();
            } catch (IOException e) {
                logger.error("copyFile()", e);
            }
        }
        logger.info("copyFile(inputFile, outputFile) - end");
    }

    public static String getInputStreamAsString(InputStream in) throws IOException {
        return getInputStreamAsString(in, true);
    }


    public static String getFileAsString(String filePath) throws IOException {

        File file = new File(filePath);
        return getInputStreamAsString(new FileInputStream(file));
    }

    public static String getFileAsStringByReader(String filePath) throws IOException {
        StringBuffer context = new StringBuffer();
        String encoding="UTF-8";
        File file=new File(filePath);
        if(file.isFile() && file.exists()){ //判断文件是否存在
            InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file),encoding);//考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while((lineTxt = bufferedReader.readLine()) != null){
                context.append(lineTxt).append("\n");
            }
            read.close();
        }else{
            throw new IOException("找不到指定的文件");
        }
        return context.toString();
    }
    /**
     * 上传文件，切返回上传文件的文件名
     *
     * @param request
     * @param saveFileRootPath
     * @return
     */
    public static String uploadFile(HttpServletRequest request, String saveFileRootPath) throws IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        String ctxPath = saveFileRootPath; //文件上传存储路径
        if(StringUtil.isEmpty(ctxPath)){
            ctxPath = SystemConfigUtil.getProperty(SystemConfigUtil.UPLOAD_DEFAULT_PATH);
        }
        ctxPath += File.separator;
        // 创建文件夹
        File file = new File(ctxPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName = null;
        String newName = null;
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            // 上传文件
            MultipartFile mf = entity.getValue();
            fileName = mf.getOriginalFilename();//获取原文件名
            //获得当前时间的最小精度
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            newName = format.format(new Date());
            //获得三位随机数
            Random random = new Random();
            for (int i = 0; i < 3; i++) {
                newName = newName + random.nextInt(9);
            }
            File uploadFile = new File(ctxPath + newName + fileName.substring(fileName.lastIndexOf(".")));
            FileCopyUtils.copy(mf.getBytes(), uploadFile);
        }
        String importFilePath = newName + fileName.substring(fileName.lastIndexOf("."));
        return importFilePath;
    }

    public static String getNewFileName(String fileExt){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String newName = format.format(new Date());
        //获得三位随机数
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            newName = newName + random.nextInt(9);
        }
        return newName+fileExt;
    }

    public static List<Map> uploadFileAndGetRowData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String importFile = IOUtil.uploadFile(request, "");
        importFile = importFile.replaceAll("\\\\", "/");
        ImportExcelFile importExcelFile = new ImportExcelFile();
        List<Map> dataList = importExcelFile.getExcelList(importFile);
        return dataList;
    }
}
