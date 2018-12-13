package org.spring.springboot.task;

import org.spring.springboot.util.DateUtil;

/**
 * @author yuyunfeng
 * @create_time 2018/12/3
 * @describe ${class}
 */
public class Common {

    public static int count =0 ;

    public static int COUNTUPLOAD =0;

    public static int MQSEND = 0;

    public static String UPLOADFILEPATH = "/opt/MT/phone_number_file/";

    public static String TODAY= DateUtil.getToday();
}
