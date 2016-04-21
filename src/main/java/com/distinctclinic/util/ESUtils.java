package com.distinctclinic.util;

import com.weejinfu.common.util.PropertyReader;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;


/**
* Created by gaoqi on 2015/6/18.
*/
public class ESUtils {

    private static Client client;

    private ESUtils(){}

    protected static PropertyReader propertyReader = null;

    static {
        propertyReader = new PropertyReader("config.properties");
    }

    public static Client getInstance(){

        if (client == null) {
            Settings settings = ImmutableSettings.settingsBuilder()
                    //指定集群名称
                    .put("cluster.name", "elasticsearch")
                            //探测集群中机器状态
                    .put("client.transport.sniff", true).build();
            String ip = propertyReader.getValueAsString("es_ip");
            String port = propertyReader.getValueAsString("es_port");
//            client = new TransportClient(settings)
//                    .addTransportAddress(new InetSocketTransportAddress(System.getenv(propertyReader.getValueAsString("es_ip")),
//                            9300));
            client = new TransportClient().addTransportAddress(
                    new InetSocketTransportAddress(ip, 9301));
        }

        return client;
    }
}
