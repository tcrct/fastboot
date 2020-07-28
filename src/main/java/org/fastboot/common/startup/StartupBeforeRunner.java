package org.fastboot.common.startup;

import org.fastboot.common.plugins.IPlugin;
import org.fastboot.common.plugins.PluginFactory;
import org.fastboot.common.utils.LogUtils;
import org.fastboot.common.utils.ToolsKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * springboot启动前执行
 *
 * @author Laotang
 * @since 1.0
 */
@Configuration
public class StartupBeforeRunner implements BeanPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartupBeforeRunner.class);

    @PostConstruct
    public void beforeRun() {

        List<IPlugin> pluginList = PluginFactory.getPluginList();
        if (ToolsKit.isEmpty(pluginList)) {
            return;
        }

        try {
            for (IPlugin plugin : pluginList) {
                plugin.before();
            }
        } catch (Exception e) {
            LogUtils.log(LOGGER, "启动插件时出错: " + e.getMessage(), e);
        }


    }

}
