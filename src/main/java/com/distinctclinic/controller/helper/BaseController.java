package com.distinctclinic.controller.helper;

import com.alibaba.fastjson.JSONObject;
import com.distinctclinic.constant.DistinctConfig;
import com.weejinfu.common.exception.BaseException;
import com.weejinfu.common.rpc.IRemoteService;
import com.weejinfu.common.rpc.RemoteCommonService;
import com.weejinfu.common.rpc.ServiceFactory;
import com.weejinfu.common.util.PropertyReader;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 * <p>
 * Created by js.lee on 4/18/15.
 */
public class BaseController {


    @Autowired
    private HttpServletRequest request;

    protected static PropertyReader propertyReader = null;
    protected static JSONObject ueString = null;
    private static Map<String, IRemoteService> serviceMap = new ConcurrentHashMap<String, IRemoteService>();

    static {
        propertyReader = new PropertyReader("config.properties");
    }



    protected IRemoteService scrawlService = getScrawlService();


    protected IRemoteService getScrawlService() {
        IRemoteService remoteService = null;

//        remoteService = ServiceFactory.getInstance(System.getenv(DistinctConfig.SCRAWL_HOST), DistinctConfig.SCRAWL_PORT);
        remoteService = ServiceFactory.getInstance(DistinctConfig.SCRAWL_HOST, DistinctConfig.SCRAWL_PORT);

        return remoteService;
    }

   }
