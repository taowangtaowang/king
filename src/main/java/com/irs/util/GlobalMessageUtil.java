package com.irs.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalMessageUtil {

    public static void getString(String className,String info, String message) {
        Logger logger =LoggerFactory.getLogger(className);
        logger.info(info,message);
    }
}