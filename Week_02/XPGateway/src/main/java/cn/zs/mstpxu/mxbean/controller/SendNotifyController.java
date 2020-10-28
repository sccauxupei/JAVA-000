package cn.zs.mstpxu.mxbean.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.zs.mstpxu.mxbean.interf.SpittleNotifier;

@Controller
@RequestMapping("/biz5")
public class SendNotifyController {
    @Autowired
    SpittleNotifier spittleNotifier;
    
    // 默认每个页面的大小
    public static final int DEFAULT_SPITTLES_PER_PAGE = 25;

    // 每页的大小
    private int spittlesPerPage = DEFAULT_SPITTLES_PER_PAGE;

    public int getSpittlesPerPage() {
        return spittlesPerPage;
    }
    
    public void setSpittlesPerPage(int spittlesPerPage) {
        this.spittlesPerPage = spittlesPerPage;
    }
    
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        String result = spittlesPerPage + " - test()";
        System.out.println(result);
        return "home";
    }

    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public void send() {
        spittleNotifier.millionthSpittlePosted();
    }
    
    @Bean
    public MBeanExporter mbeanExporter(SendNotifyController SendNotifyController) {
        MBeanExporter exporter = new MBeanExporter();
        Map<String, Object> beans = new HashMap<String, Object>();
        beans.put("spitter:name=SpittleController", SendNotifyController);
        exporter.setBeans(beans);
        return exporter;
    }
}
