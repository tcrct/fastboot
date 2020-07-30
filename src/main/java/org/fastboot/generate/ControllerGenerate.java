package org.fastboot.generate;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.nio.charset.Charset;

public class ControllerGenerate {
    private String basePackage;
    private String path;
    private String fileName;
    private  Class<?> beanClass;
    private String simpleName;
    private StringBuilder body = new StringBuilder();

    public ControllerGenerate(String basePackage, String path, Class<?> beanClass) {
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

        String controllerFileName = fileName+"Controller.java";
        FileUtil.mkdir(path);
        File file = new File(path+ File.separator+ controllerFileName);
        FileUtil.writeString(body.toString(), file.getPath(), Charset.forName("UTF-8"));
        System.out.println("=====["+controllerFileName+"]文件创建成功: " + file.getPath());
    }

    private String builderPackage() {
        return "package "+basePackage+".controller;";
    }
    private String builderImport() {
        StringBuilder importStr = new StringBuilder();
        importStr.append("\n\n");
        importStr.append("import org.fastboot.common.base.BaseController;").append("\n");
        importStr.append("import org.fastboot.common.dto.R;").append("\n");
        importStr.append("import ").append(basePackage+".service.").append(fileName).append("Service;").append("\n");
        importStr.append("import org.springframework.beans.factory.annotation.Autowired;").append("\n");
        importStr.append("import org.springframework.http.MediaType;").append("\n");
        importStr.append("import org.springframework.stereotype.Controller;").append("\n");
        importStr.append("import org.springframework.validation.annotation.Validated;").append("\n");
        importStr.append("import org.springframework.web.bind.annotation.*;").append("\n");
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
        bodyStr.append("@Controller").append("\n");
        bodyStr.append("@RequestMapping(value = ").append("\"/").append(fileName.toLowerCase()).append("\"").append(")").append("\n");
        bodyStr.append("public class ").append(fileName).append("Controller extends BaseController<").append(simpleName).append("> {").append("\n");
        bodyStr.append("\n");
        bodyStr.append("\t@Autowired").append("\n");
        bodyStr.append("\tprivate ").append(fileName).append("Service ").append(fileName.toLowerCase()).append("Service;").append("\n");
        bodyStr.append("\n");
        bodyStr.append("\t@RequestMapping(value = ").append("\"/demo\"").append(",  method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)").append("\n");
        bodyStr.append("\t@ResponseBody").append("\n");
        bodyStr.append("\tpublic R demo(@Validated @RequestBody ").append(simpleName).append(" dto) {").append("\n");
        bodyStr.append("\t\t").append("try {").append("\n");
        bodyStr.append("\t\t\t").append("return R.success(").append(fileName.toLowerCase()).append("Service.demo(dto));").append("\n");
        bodyStr.append("\t\t").append("} catch (Exception e) {").append("\n");
        bodyStr.append("\t\t\t").append("return R.error(123, e);").append("\n");
        bodyStr.append("\t\t}").append("\n");

        bodyStr.append("\t}").append("\n");
        bodyStr.append("}").append("\n");
        return bodyStr.toString();
    }
}
