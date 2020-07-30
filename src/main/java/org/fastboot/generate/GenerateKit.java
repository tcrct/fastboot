package org.fastboot.generate;

import cn.hutool.core.io.FileUtil;
import org.fastboot.App;
import org.fastboot.common.enums.ConstEnums;
import org.fastboot.common.utils.ToolsKit;
import org.fastboot.db.model.BaseEntity;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class GenerateKit {

    private static class KitHolder {
        private static final GenerateKit INSTANCE = new GenerateKit();
    }
    private GenerateKit() {
    }
    public static final GenerateKit duang() {
        return KitHolder.INSTANCE;
    }

    // ======================================================= //
    /**父目录*/
    private String baseDir;
    /**Dto或Entity的class*/
    private Class<?> clazz;
    private String basePackage;
    private String controllerDir;
    private String serviceDir;
    private String cacheDir;
    private String cacheKeyEnumDir;
    private String dtoDir;
    private String daoDir;

    public GenerateKit clazz(Class<?> clazz) {
        this.clazz = clazz;
        return this;
    }

    public GenerateKit baseDir(String baseDir) {
        this.baseDir = baseDir;
        return this;
    }

    public GenerateKit basePackage(String basePackage) {
        this.basePackage = basePackage;
        return this;
    }


    private void buildSubDir() {
        if (ToolsKit.isEmpty(clazz)) {
            throw new NullPointerException("dto or entity class is not null");
        }
//        try {
//            String rootClassPath = clazz.getClassLoader().getResource("").toURI().getPath();
//            File rootDir = new File(rootClassPath);
//            baseDir = rootDir.getParentFile().getParentFile().getAbsolutePath();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        controllerDir = buildSubDir("controller");
        serviceDir = buildSubDir("service");
        cacheDir = buildSubDir("cache");
        daoDir = buildSubDir("dao");
        dtoDir = buildSubDir("dto");

    }

    private String buildSubDir(String subDirName) {
        StringBuilder sb = new StringBuilder();
        if (baseDir.endsWith("/") || baseDir.endsWith("\\") || baseDir.endsWith(File.separator)) {
            baseDir = baseDir.substring(0, baseDir.length()-1);
        }
        sb.append(baseDir).append(File.separator).append(subDirName).append(File.separator)
                .append("src").append(File.separator).append("main").append(File.separator).append("java")
        .append(File.separator).append(basePackage.replace(".", File.separator)).append(File.separator)
        .append(subDirName);
        return sb.toString();
    }

    public void run() {
        buildSubDir();
        new ControllerGenerate(basePackage, controllerDir, clazz).gen();
        new ServiceGenerate(basePackage, serviceDir, clazz).gen();
        new DaoGenerate(basePackage, daoDir, clazz).gen();
        new CacheGenerate(basePackage, cacheDir, clazz).gen();
        new CacheKeyEnumGenerate(basePackage, cacheDir, clazz).gen();
    }

    public static void main(String[] args) {
        GenerateKit.duang().basePackage("com.push4j").clazz(App.class).run();
    }

}
