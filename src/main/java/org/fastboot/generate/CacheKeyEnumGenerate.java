package org.fastboot.generate;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.nio.charset.Charset;

public class CacheKeyEnumGenerate {
    private String basePackage;
    private String path;
    private String fileName;
    private  Class<?> beanClass;
    private String simpleName;
    private StringBuilder body = new StringBuilder();

    public CacheKeyEnumGenerate(String basePackage, String path, Class<?> beanClass) {
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

        String cacheKeyEnumFileName = fileName+"CacheKeyEnum.java";
        FileUtil.mkdir(path);
        File file = new File(path+ File.separator+ cacheKeyEnumFileName);
        FileUtil.writeString(body.toString(), file.getPath(), Charset.forName("UTF-8"));
        System.out.println("=====["+cacheKeyEnumFileName+"]文件创建成功: " + file.getPath());
    }

    private String builderPackage() {
        return "package "+basePackage+".cache.enums;";
    }
    private String builderImport() {
        StringBuilder importStr = new StringBuilder();
        importStr.append("\n\n");
        importStr.append("import org.fastboot.redis.core.ICacheKeyEnums;").append("\n");
        return importStr.toString();
    }
    private String builderBody() {
        StringBuilder bodyStr = new StringBuilder();
        bodyStr.append("/**").append("\n");
        bodyStr.append("* ").append(fileName).append("缓存KEY枚举").append("\n");
        bodyStr.append("*").append("\n");
        bodyStr.append("* @author zat").append("\n");
        bodyStr.append("* @since 1.0").append("\n");
        bodyStr.append("*").append("\n");
        bodyStr.append("*/").append("\n");
        bodyStr.append("public enum ").append(fileName).append("CacheKeyEnum implements ICacheKeyEnums {").append("\n");
        bodyStr.append("\t").append("HSET_KEY(\"mpay:xxx:xxxx:\", ICacheKeyEnums.ONE_DAY_TTL, \"记录缓存到hset的key\"),").append("\n\n");
        bodyStr.append("\t;").append("\n");
        bodyStr.append("\tprivate String keyPrefix;\n")
                .append("\tprivate int  ttl;\n")
                .append("\tprivate String keyDesc;\n")
                .append("\t/**").append("\n")
                .append("\t*@param keyPrefix 缓存关键字前缀").append("\n")
                .append("\t*@param ttl 缓存有效时间").append("\n")
                .append("\t*@param keyDesc 缓存关键字说明").append("\n")
                .append("\t*/").append("\n")
                .append("\tprivate ").append(fileName).append("CacheKeyEnum(String keyPrefix, int ttl, String keyDesc) {\n")
                .append("\t\tthis.keyPrefix = keyPrefix;").append("\n")
                .append("\t\tthis.ttl = ttl;").append("\n")
                .append("\t\tthis.keyDesc = keyDesc;").append("\n")
                .append("\t}");

        bodyStr.append("\n\tpublic String getKeyPrefix() {").append("\n")
                .append("\t\treturn keyPrefix;").append("\n")
                .append("\t}");

        bodyStr.append("\n\tpublic int getKeyTTL() {").append("\n")
                .append("\t\treturn ttl;").append("\n")
                .append("\t}");

        bodyStr.append("\n\tpublic String getKeyDesc() {").append("\n")
                .append("\t\treturn keyDesc;").append("\n")
                .append("\t}\n");

        bodyStr.append("}").append("\n");
        return bodyStr.toString();
    }
}
