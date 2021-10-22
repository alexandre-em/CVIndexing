package com.indexation.cv.service;

import com.indexation.cv.controller.CVResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Allow logging actions the API
 */
public class CVLogger {
    private final static String apiEnv="PROD";
    private final static Logger logger = LoggerFactory.getLogger(CVResource.class);

    public static void info(String message) {
        if (Objects.equals(apiEnv, "PROD")) {
            logger.info(message);
        } else {
            System.out.println(message);
        }
    }
    public static void warn(String message) {
        if (Objects.equals(apiEnv, "PROD")) {
            logger.warn(message);
        } else {
            System.out.println(message);
        }
    }
    public static void error(String message) {
        if (Objects.equals(apiEnv, "PROD")) {
            logger.error(message);
        } else {
            System.err.println(message);
        }
    }
}
