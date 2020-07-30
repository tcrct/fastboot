package org.fastboot.generate;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.nio.charset.Charset;

public class ServiceGenerate {
    private String basePackage;
    private String path;
    private String fileName;
    private  Class<?> beanClass;
    private String simpleName;
    private StringBuilder body = new StringBuilder();

    public ServiceGenerate(String basePackage, String path, Class<?> beanClass) {
        this.basePackage = basePackage;
        this.path = path;
        this.beanClass = beanClass;
        simpleName = beanClass.getSimpleName();
        getFileName(beanClass.getSimpleName());
    }

    private void getFileName(String className) {
        String classLowerName = className.toLowerCase();
        if (classLowerName.endsWith("entity")) {
            this.fileName = className.replace("entity", "");
        }
        else if (classLowerName.endsWith("dto")) {
            this.fileName = className.replace("dto", "");
        } else {
            this.fileName = className;
        }

    }

    public void gen() {
        body.append(builderPackage())
        .append(builderImport())
        .append(builderBody());

        String serviceFileName = fileName+"Service.java";
        FileUtil.mkdir(path);
        File file = new File(path+ File.separator+ serviceFileName);
        FileUtil.writeString(body.toString(), file.getPath(), Charset.forName("UTF-8"));
        System.out.println("=====["+serviceFileName+"]文件创建成功: " + file.getPath());
    }

    private String builderPackage() {
        return "package "+basePackage+".service;";
    }
    private String builderImport() {
        StringBuilder importStr = new StringBuilder();
        importStr.append("\n\n");
        importStr.append("import org.fastboot.common.base.BaseController;").append("\n");
        importStr.append("import org.fastboot.db.curd.CurdService").append("\n");
        importStr.append("import org.fastboot.exception.common.ServiceException;").append("\n");
        importStr.append("import org.springframework.beans.factory.annotation.Autowired;").append("\n");
        importStr.append("import org.springframework.stereotype.Service;").append("\n");
        importStr.append("import ").append(basePackage).append(".dao.").append(simpleName).append("Dao;").append(";\n");
        importStr.append("import ").append(basePackage).append(".cache.").append(simpleName).append("CacheService;").append(";\n\n");
        return importStr.toString();
    }
    private String builderBody() {
        StringBuilder bodyStr = new StringBuilder();

        bodyStr.append("/**").append("\n");
        bodyStr.append("* ").append("\n");
        bodyStr.append("*").append("\n");
        bodyStr.append("* @author zat").append("\n");
        bodyStr.append("* @since 1.0").append("\n");
        bodyStr.append("*").append("\n");
        bodyStr.append("*/").append("\n");
        bodyStr.append("@Service").append("\n");
        bodyStr.append("public class ").append(fileName).append("Service extends CurdService<").append(simpleName).append("> {").append("\n");
        bodyStr.append("\n");
        bodyStr.append("\t@Autowired").append("\n");
        bodyStr.append("\tprivate ").append(fileName).append("CacheService ").append(simpleName.toLowerCase()).append("CacheService;").append("\n");
        bodyStr.append("\t@Autowired").append("\n");
        bodyStr.append("\tprivate ").append(fileName).append("Dao ").append(simpleName.toLowerCase()).append("Dao;").append("\n");
        bodyStr.append("\n");
        bodyStr.append("\tpublic String demo(").append(simpleName).append(" dto) {").append("\n");
        bodyStr.append("\t\t").append("return ToolsKit.toJsonString(dto);").append("\n");
        bodyStr.append("\t}").append("\n");
        bodyStr.append("}").append("\n");
        return bodyStr.toString();
    }
}
