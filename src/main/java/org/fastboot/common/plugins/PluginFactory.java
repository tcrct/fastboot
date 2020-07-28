package org.fastboot.common.plugins;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.setting.Setting;
import cn.hutool.setting.SettingUtil;
import org.fastboot.common.annotation.Plugin;
import org.fastboot.common.utils.LogUtils;
import org.fastboot.common.utils.ToolsKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

/**
 * 插件工厂
 *
 * @author Laotang
 * @since 1.0
 */
public class PluginFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(PluginFactory.class);

    /**
     * 请求处理器集合
     */
    private static List<IPlugin> PLUGIN_LIST = null;


    public static List<IPlugin> getPluginList() {
        if (null == PLUGIN_LIST  || PLUGIN_LIST.isEmpty()) {
            PLUGIN_LIST = new ArrayList<>();
            initPlugins();
        }
        return PLUGIN_LIST;
    }

    private static void initPlugins() {

        Setting setting = SettingUtil.get("application.properties");
        if (ToolsKit.isEmpty(setting)) {
            LogUtils.log(LOGGER,"application.properties不存在");
            return;
        }
        String scanPackage = setting.get("base.package");
        Set<Class<?>> pluginSet = ClassUtil.scanPackageByAnnotation(scanPackage, Plugin.class);
        if (ToolsKit.isEmpty(pluginSet)) {
            LogUtils.log(LOGGER, "没有发现启动插件类");
            return;
        }
        try {
            TreeMap<Integer, IPlugin> treeMap = new TreeMap<>();
            for (Class<?> clazz : pluginSet) {
                if (!IPlugin.class.equals(clazz.getInterfaces()[0])) {
                    throw new RuntimeException(String.format("[{}]没有实现[{}]接口，请检查！", clazz.getName(), Plugin.class.getSimpleName()));
                }
                Plugin PluginAnn = clazz.getAnnotation(Plugin.class);
                if (null == PluginAnn) {
                    continue;
                }
                IPlugin plugin = (IPlugin) ReflectUtil.newInstance(clazz);
                if (ToolsKit.isNotEmpty(plugin)) {
                    treeMap.put(PluginAnn.sort(), plugin);
                }
            }

            if(!treeMap.isEmpty()) {
                PLUGIN_LIST.addAll(treeMap.values());
            }
        } catch (Exception e) {
            LOGGER.warn("初始化插件时出错时出错: {}，清空PLUGIN_LIST集合后退出", e.getMessage(), e);
            PLUGIN_LIST.clear();
        }
    }


}
