package com.ssta.company.spider.controller;

import com.ssta.company.spider.basic.common.eneity.CompanyInfo;
import com.ssta.company.spider.builder.MsgBuilder;
import com.ssta.company.spider.service.CompanyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/companyInfo")
public class CompanyInfoController {
    @Autowired
    CompanyInfoService companyInfoService;

    @GetMapping("get")
    public Map get(CompanyInfo info){
        List<CompanyInfo> companyInfos = companyInfoService.findByExample(info);
        return MsgBuilder.buildReturnMessage(companyInfos);
    }
}
