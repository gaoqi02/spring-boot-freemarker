package com.distinctclinic.constant;

import com.weejinfu.common.util.PropertyReader;

/**
 * Created by gaoqi on 2015/12/2.
 */
public class DistinctConfig {

    public static String SCRAWL_HOST;
    public static int SCRAWL_PORT;

    public static final String topicPattern = "、";

    static {
        PropertyReader reader = new PropertyReader("config.properties");
//        SCRAWL_HOST = System.getenv(reader.getValueAsString("scrawl_ip"));
        SCRAWL_HOST = reader.getValueAsString("scrawl_ip");
        SCRAWL_PORT = reader.getValueAsInt("scrawl_port");
    }

}
