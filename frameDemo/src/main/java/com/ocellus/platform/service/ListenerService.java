package com.ocellus.platform.service;

import com.ocellus.platform.utils.StringUtil;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ListenerService {

    private static final Logger logger = Logger.getLogger(ListenerService.class);

    @Autowired
    private ResourceService resourceService;

    private static Document doc = null;
    private static Element root = null;

    public void init(String xmlPath) {
        SAXReader reader = new SAXReader();
        File xmlFile = new File(xmlPath);
        try {
            doc = reader.read(xmlFile);
            root = doc.getRootElement();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void parseResourceFile(String rootPath) {
        resourceService.inactiveModule();
        File rootFile = new File(rootPath);
        File[] fileArray = rootFile.listFiles();
        String fileName = null;
        String resourceNames = "";
        if (fileArray != null) {
            for (int i = 0; i < fileArray.length; i++) {
                fileName = fileArray[i].getName();
                File file = new File(fileArray[i].toString());
                if (!file.isDirectory()) {
                    if ("xml".contains(fileName.lastIndexOf(".") > 0 ? fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase() : "")) {
                        try {
                            logger.info("Getting file : " + fileName);
                            init(rootPath + "/" + fileName);
                            Node node = root.selectSingleNode("//module");
                            resourceNames += node.valueOf("@name") + ",";
                        } catch (Exception e) {
                            logger.error("Read file " + fileName + " failed!", e);
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (!StringUtil.isEmpty(resourceNames))
                resourceService.activeModule(resourceNames.substring(0, resourceNames.length() - 1).split(","));
            else
                logger.error("配置文件内容为空");
        } else
            logger.error("项目没有加入模块配置文件");
    }
}
