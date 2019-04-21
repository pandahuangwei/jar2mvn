package com.mvn.utils;

/**
 * 获取CPU核数
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2014.12.19 10:57.
 */
public class SystemUtil {
    private static final int DEFAULT_PROCESSORS_NUM = 4;

    public static int getProcessorCount() {
        String number = System.getenv("NUMBER_OF_PROCESSORS");
        try {
            if (number != null) {
                return Integer.parseInt(number);
            }
        } catch (Exception e) {
            System.out.println("can't getProcessorCount,use defaut:" + DEFAULT_PROCESSORS_NUM);
        }

        return DEFAULT_PROCESSORS_NUM;
    }
}
