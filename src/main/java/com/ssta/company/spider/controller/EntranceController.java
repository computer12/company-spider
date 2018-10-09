package com.ssta.company.spider.controller;

import com.ssta.company.spider.basic.tyc.CompanyOrder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("/home")
@RequestMapping("/")
public class EntranceController {

    @GetMapping("index")
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView();

        ArrayList<String> times = new ArrayList();
        times.add("00:00");
        times.add("01:00");
        times.add("02:00");
        times.add("03:00");
        times.add("04:00");
        times.add("05:00");
        times.add("06:00");
        times.add("07:00");
        times.add("08:00");
        times.add("09:00");
        times.add("10:00");
        times.add("11:00");
        times.add("12:00");
        times.add("13:00");
        times.add("14:00");
        times.add("15:00");
        times.add("16:00");
        times.add("17:00");
        times.add("18:00");
        times.add("19:00");
        times.add("20:00");
        times.add("21:00");
        times.add("22:00");
        times.add("23:00");
        mv.addObject("times",times);

        List<Map<String,String>> order = new ArrayList<>();
        for(CompanyOrder co : CompanyOrder.values()){
            Map<String,String> map = new HashMap<>();
            map.put("value",co.getOrderType());
            map.put("name",co.getMsg());
            order.add(map);
        }
        mv.addObject("tyc_order",order);

        mv.setViewName("index");

        return mv;
    }
}
