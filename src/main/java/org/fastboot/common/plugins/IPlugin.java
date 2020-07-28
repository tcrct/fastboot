package org.fastboot.common.plugins;

/**
 * 插件接口
 *
 * @author Laotang
 * @since 1.0
 */
public interface IPlugin {

    /**
     * 在springboot启动 前 执行
     */
    void before();

    /**
     * 在springboot启动 后 执行
     */
    void after();

}
