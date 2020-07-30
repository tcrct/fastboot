package org.fastboot.generate;

import cn.hutool.core.io.FileUtil;
import org.fastboot.common.utils.ToolsKit;
import org.fastboot.redis.RedisFactory;
import org.fastboot.redis.core.CacheKeyModel;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.Charset;

public class CacheGenerate {
    private String basePackage;
    private String path;
    private String fileName;
    private  Class<?> beanClass;
    private String simpleName;
    private StringBuilder body = new StringBuilder();

    public CacheGenerate(String basePackage, String path, Class<?> beanClass) {
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

        String cacheServiceFileName = fileName+"CacheService.java";
        FileUtil.mkdir(path);
        File file = new File(path+ File.separator+ cacheServiceFileName);
        FileUtil.writeString(body.toString(), file.getPath(), Charset.forName("UTF-8"));
        System.out.println("=====["+cacheServiceFileName+"]文件创建成功: " + file.getPath());
    }

    private String builderPackage() {
        return "package "+basePackage+".cache;";
    }
    private String builderImport() {
        StringBuilder importStr = new StringBuilder();
        importStr.append("\n\n");
        importStr.append("import org.fastboot.redis.RedisFactory;").append("\n");
        importStr.append("import org.fastboot.redis.core.CacheKeyModel;").append("\n");
        importStr.append("import org.fastboot.redis.crud.ICurdCacheService;").append("\n");
        importStr.append("import org.springframework.stereotype.Service;").append("\n");
        importStr.append("import ").append(basePackage).append(".cache.enums.").append(fileName).append("CacheKeyEnum;").append("\n");
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
        bodyStr.append("*/").append("\n");
        bodyStr.append("@Service").append("\n");
        bodyStr.append("public class ").append(fileName).append("CacheService implements ICurdCacheService<").append(simpleName).append("> {").append("\n");

        bodyStr.append("\t").append("/**").append("\n");
        bodyStr.append("\t").append("* 保存记录到缓存").append("\n");
        bodyStr.append("\t").append("*").append("\n");
        bodyStr.append("\t").append("* @param entity").append("\n");
        bodyStr.append("\t").append("* @return 保存成功返回true").append("\n");
        bodyStr.append("\t").append("*/").append("\n");
        bodyStr.append("\t").append("@Override").append("\n");
        bodyStr.append("\t").append("public Integer save(").append(simpleName).append(" entity) {").append("\n");
        bodyStr.append("\t\t").append("CacheKeyModel cacheKeyModel = new CacheKeyModel.Builder(").append(fileName).append("CacheKeyEnum.HSET_KEY).build();").append("\n");
        bodyStr.append("\t\t").append("return RedisFactory.getCache().hset(cacheKeyModel, entity.getId(), entity).intValue();").append("\n");
        bodyStr.append("\t").append("}").append("\n\n");

        bodyStr.append("\t").append("/**").append("\n");
        bodyStr.append("\t").append("* 根据id查找缓存记录").append("\n");
        bodyStr.append("\t").append("*").append("\n");
        bodyStr.append("\t").append("* @param entity").append("\n");
        bodyStr.append("\t").append("* @return 存在返回记录对象").append("\n");
        bodyStr.append("\t").append("*/").append("\n");
        bodyStr.append("\t").append("@Override").append("\n");
        bodyStr.append("\t").append("public ").append(simpleName).append(" findById(").append("String id) {").append("\n");
        bodyStr.append("\t\t").append("CacheKeyModel cacheKeyModel = new CacheKeyModel.Builder(").append(fileName).append("CacheKeyEnum.HSET_KEY).build();").append("\n");
        bodyStr.append("\t\t").append("return RedisFactory.getCache().hget(cacheKeyModel, id);").append("\n");
        bodyStr.append("\t").append("}").append("\n\n");


        bodyStr.append("\t").append("/**").append("\n");
        bodyStr.append("\t").append("* 根据id删除缓存记录").append("\n");
        bodyStr.append("\t").append("*").append("\n");
        bodyStr.append("\t").append("* @param id 记录ID").append("\n");
        bodyStr.append("\t").append("* @return 删除成功返回记录个数").append("\n");
        bodyStr.append("\t").append("*/").append("\n");
        bodyStr.append("\t").append("@Override").append("\n");
        bodyStr.append("\t").append("public Integer").append(" deleteById(").append("String id) {").append("\n");
        bodyStr.append("\t\t").append("CacheKeyModel cacheKeyModel = new CacheKeyModel.Builder(").append(fileName).append("CacheKeyEnum.HSET_KEY).build();").append("\n");
        bodyStr.append("\t\t").append("return RedisFactory.getCache().hdel(cacheKeyModel, id).intValue();").append("\n");
        bodyStr.append("\t").append("}").append("\n\n");

        bodyStr.append("}").append("\n");
        return bodyStr.toString();
    }
}
