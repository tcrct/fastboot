package org.fastboot.common.startup;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * spring boot启动完成后执行
 *
 * @author Laotang
 * @since 1.0
 */
@Component
@Order(value = 1)
public class DuangStartupRunner implements CommandLineRunner {

    /**
     * 请求处理器集合
     */
    private static final List<IStartupRun> STARTUP_RUN_LIST = new ArrayList<>();

    @Override
    public void run(String... args) throws Exception {

        initStartupRun();

        if (STARTUP_RUN_LIST.isEmpty()) {
            return;
        }

        for (IStartupRun startupRun : STARTUP_RUN_LIST) {
            startupRun.run();
        }
    }


    private static void initStartupRun() {

    }
}
