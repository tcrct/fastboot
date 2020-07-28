package org.fastboot.common.startup;

import org.fastboot.common.plugins.IPlugin;
import org.fastboot.common.plugins.PluginFactory;
import org.fastboot.common.utils.LogUtils;
import org.fastboot.common.utils.ToolsKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * spring boot启动后执行
 *
 * @author Laotang
 * @since 1.0
 */
@Component
@Order(value = 1)
public class StartupAfterRunner implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartupAfterRunner.class);


    @Override
    public void run(String... args) throws Exception {

        List<IPlugin> pluginList = PluginFactory.getPluginList();
        if (ToolsKit.isEmpty(pluginList)) {
            return;
        }

        try {
            for (IPlugin plugin : pluginList) {
                plugin.after();
            }
        } catch (Exception e) {
            LogUtils.log(LOGGER, "启动插件时出错: " + e.getMessage(), e);
        }
    }



}
