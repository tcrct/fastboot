package org.fastboot.generate;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.nio.charset.Charset;

public class DaoGenerate {
    private String basePackage;
    private String path;
    private String fileName;
    private  Class<?> beanClass;
    private String simpleName;
    private StringBuilder body = new StringBuilder();

    public DaoGenerate(String basePackage, String path, Class<?> beanClass) {
        this.basePackage = basePackage;
        this.path = path;
        this.beanClass = beanClass;
        simpleName = beanClass.getSimpleName();
        getFileName(beanClass.getSimpleName());
    }

    private void getFileName(String className) {
        String classLowerName = className.toLowerCase();
        if (classLowerName.endsWith("entity")) {
            this.fileName = className.replace("Entity", "");
        }
        else if (classLowerName.endsWith("dto")) {
            this.fileName = className.replace("Dto", "");
        } else {
            this.fileName = className;
        }

    }

    public void gen() {
        body.append(builderPackage())
        .append(builderImport())
        .append(builderBody());

        String daoFileName = fileName+"Dao.java";
        FileUtil.mkdir(path);
        File file = new File(path+ File.separator+ daoFileName);
        FileUtil.writeString(body.toString(), file.getPath(), Charset.forName("UTF-8"));
        System.out.println("=====["+daoFileName+"]文件创建成功: " + file.getPath());
    }

    private String builderPackage() {
        return "package "+basePackage+".dao;";
    }
    private String builderImport() {
        StringBuilder importStr = new StringBuilder();
        importStr.append("\n\n");
        importStr.append("import org.fastboot.db.dao.BaseDao;").append("\n");
        importStr.append("import ").append(beanClass.getName()).append(";\n\n");
        return importStr.toString();
    }
    private String builderBody() {
        StringBuilder bodyStr = new StringBuilder();
        bodyStr.append("/**").append("\n");
        bodyStr.append("* ").append(fileName).append("\n");
        bodyStr.append("*").append("\n");
        bodyStr.append("* @author zat").append("\n");
        bodyStr.append("* @since 1.0").append("\n");
        bodyStr.append("*").append("\n");
        bodyStr.append("*/").append("\n");
        bodyStr.append("public interface ").append(fileName).append("Dao extends BaseDao<").append(simpleName).append("> {").append("\n");
        bodyStr.append("}").append("\n");
        return bodyStr.toString();
    }
}
