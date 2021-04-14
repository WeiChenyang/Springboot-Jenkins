package com.cnbm.intf.controller;

import com.cnbm.intf.core.utils.SpringHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @create 2019030114:45
 */
@Controller
@Slf4j
public class IndexController {
    @Autowired
    private SpringHttpUtil springHttpUtil;
    @RequestMapping("/")
    public ModelAndView root() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("index.html");
        mv.addObject("pagePath", "pages/dashboard/dashboard");
        mv.addObject("content", "dashboard");
        return mv;
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/linkMenu")
    public ModelAndView index(@RequestParam String pagePath, @RequestParam String content) {
        // 不带参数请求
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index.html");
        modelAndView.addObject("pagePath", pagePath);
        modelAndView.addObject("content", content);
        return modelAndView;
    }

    @GetMapping("/logout")
    public String logout() {
        Map<String, String> header = new HashMap<>();
        header.put("token","");
        springHttpUtil.setCookie(header);
        return "login";
    }

}
