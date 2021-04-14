package com.cnbm.intf.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cnbm.intf.core.utils.EncryptUtil;
import com.cnbm.intf.core.utils.KeyUtils;
import com.cnbm.intf.core.utils.SpringHttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @create 2019031216:54
 */
@RestController
public class LoginController {

    @Autowired
    private SpringHttpUtil springHttpUtil;

    @PostMapping("/checkLogin")
    public JSONObject checkLoin(@RequestParam("name") String name, @RequestParam("password") String password) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("isFlag", "true");
        if (StringUtils.isNotBlank(name) && name.equals("cnbm") && StringUtils.isNotBlank(password) && password.equals("cnbm2020!@#")) {
            setLoginInfo(name, true);
            jsonObject.put("isAdmin", "Y");
        } else if (StringUtils.isNotBlank(name) && name.equals("test") && StringUtils.isNotBlank(password) && password.equals("cnbm20200317#")) {
            setLoginInfo(name, false);
            jsonObject.put("isAdmin", "N");
        } else {
            jsonObject.put("isFlag", "false");
        }
        return jsonObject;
    }

    protected void setLoginInfo(String name, boolean isAdmin) {
        Map<String, String> bean = new HashMap<>();
        bean.put("name", name);
        bean.put("isAdmin", Boolean.toString(isAdmin));
        bean.put("t",Long.toString(System.currentTimeMillis()));
        Map<String, String> header = new HashMap<>();
        header.put("token", EncryptUtil.buildRSAEncryptByPublicKey(JSON.toJSONString(bean), KeyUtils.publicKey));
        springHttpUtil.setHeader(header);
        springHttpUtil.setCookie(header);
    }

}
