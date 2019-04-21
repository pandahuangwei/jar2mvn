package com.mvn.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取CPU核数
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2014.12.19 10:57.
 */
public class SystemUtil {
    private static final int DEFAULT_PROCESSORS_NUM = 4;
    private static Logger logger = LoggerFactory.getLogger(TaskManager.class);

    public static int getProcessorCount() {
        String number = System.getenv("NUMBER_OF_PROCESSORS");
        try {
            if (number != null) {
                return Integer.parseInt(number);
            }
        } catch (Exception e) {
            logger.warn("can't getProcessorCount,use defaut:" + DEFAULT_PROCESSORS_NUM);
        }

        return DEFAULT_PROCESSORS_NUM;
    }
}
