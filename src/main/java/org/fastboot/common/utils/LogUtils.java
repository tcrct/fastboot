package org.fastboot.common.utils;

import org.slf4j.Logger;

/**
 * 日志工具类
 *
 * @author Laotang
 * @since 1.0
 */
public class LogUtils {


    public static void log(Logger logger, String format) {
        log(logger, format, null, null);
    }

    public static void log(Logger logger, String format, Throwable throwable) {
        log(logger, format, throwable, null);
    }

    public static void log(Logger logger, String format, Object... arguments) {
        log(logger, format, null, arguments);
    }

    private static void log(Logger logger, String format, Throwable throwable, Object... arguments) {
        if (logger.isWarnEnabled()) {
            if (null == throwable && null == arguments) {
                logger.warn(format);
            } else if (null == throwable) {
                logger.warn(format, arguments);
            } else {
                logger.warn(format, throwable);
            }
        }
        else if (logger.isInfoEnabled()) {
            if (null == throwable && null == arguments) {
                logger.info(format);
            } else if (null == throwable) {
                logger.info(format, arguments);
            } else {
                logger.info(format, throwable);
            }
        }
        else if (logger.isDebugEnabled()) {
            if (null == throwable && null == arguments) {
                logger.debug(format);
            } else if (null == throwable) {
                logger.debug(format, arguments);
            } else {
                logger.debug(format, throwable);
            }
        }
        else if (logger.isErrorEnabled()) {
            if (null == throwable && null == arguments) {
                logger.error(format);
            } else if (null == throwable) {
                logger.error(format, arguments);
            } else {
                logger.error(format, throwable);
            }
        }
        else {
            if (null == throwable && null == arguments) {
                logger.trace(format);
            } else if (null == throwable) {
                logger.trace(format, arguments);
            } else {
                logger.trace(format, throwable);
            }
        }
    }
    
}
