package com.indexation.cv.utils;

import com.indexation.cv.controller.CVResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Allow logging actions the API
 * @author <a href="mailto:alexandre.em@pm.me">Alexandre Em</a>
 */
public class CVLogger {
    private final static Logger logger = LoggerFactory.getLogger(CVResource.class);

    public static void info(String message, String[] env) {
        if (Arrays.asList(env).contains("prod")) {
            logger.info(message);
        } else {
            System.out.println("[dev] "+message);
        }
    }
    public static void warn(String message, String[] env) {
        if (Arrays.asList(env).contains("prod")) {
            logger.warn(message);
        } else {
            System.out.println("[dev] "+message);
        }
    }
    public static void error(String message, String[] env) {
        if (Arrays.asList(env).contains("prod")) {
            logger.error(message);
        } else {
            System.err.println("[dev] "+message);
        }
    }
}
