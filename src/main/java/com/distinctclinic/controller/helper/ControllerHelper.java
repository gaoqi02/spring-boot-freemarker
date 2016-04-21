package com.distinctclinic.controller.helper;

import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

/**
 * Created by zhengxu on 14-3-7.
 */
@Service
public class ControllerHelper extends BaseController {

    public static String returnString(ModelMap modelMap, String msg) {
        modelMap.put("s", msg);
        return "string";
    }

}
