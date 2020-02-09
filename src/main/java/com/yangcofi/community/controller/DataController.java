package com.yangcofi.community.controller;

import com.yangcofi.community.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.crypto.Data;
import java.util.Date;

/**
 * @ClassName DataController
 * @Description TODO
 * @Author YangC
 * @Date 2019/12/8 16:15
 **/
@Controller
public class DataController {

    @Autowired
    private DataService dataService;

    //访问统计页面
    @RequestMapping(path = "/data", method = {RequestMethod.GET, RequestMethod.POST})
    public String getDataPage(){
        return "/site/admin/data";
    }

    //处理网站UV @DateTimeFormat告诉服务器传的日期格式是什么
    @RequestMapping(path = "/data/uv", method = RequestMethod.POST)
    public String getUV(@DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                        @DateTimeFormat(pattern = "yyyy-MM-dd") Date end, Model model){
        long uv = dataService.calculateUV(start, end);
        model.addAttribute("uvResult", uv);
        model.addAttribute("uvStartDate", start);
        model.addAttribute("uvEndDate", end);
        return "forward:/data";     //forward就是我处理一半交给/data接着处理转发是在一个请求完成的，请求类型不能变 如果直接返回"/site/admin/data"时返回一个模板 也就是先给dispatcherServlet，再进行后续处理
    }

    //统计活跃用户 @DateTimeFormat告诉服务器传的日期格式是什么
    @RequestMapping(path = "/data/dau", method = RequestMethod.POST)
    public String getDAU(@DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                        @DateTimeFormat(pattern = "yyyy-MM-dd") Date end, Model model){
        long dau = dataService.calculateDAU(start, end);
        model.addAttribute("dauResult", dau);
        model.addAttribute("dauStartDate", start);
        model.addAttribute("dauEndDate", end);
        return "forward:/data";     //forward就是我处理一半交给/data接着处理转发是在一个请求完成的，请求类型不能变 如果直接返回"/site/admin/data"时返回一个模板 也就是先给dispatcherServlet，再进行后续处理
    }
}

