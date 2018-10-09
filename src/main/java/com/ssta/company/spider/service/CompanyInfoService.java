package com.ssta.company.spider.service;

import com.ssta.company.spider.basic.common.eneity.CompanyInfo;
import com.ssta.company.spider.basic.common.repository.CompanyInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyInfoService {

    @Autowired
    JdbcTemplate template;

    @Autowired
    CompanyInfoRepository companyInfoRepository;

    public List<CompanyInfo> findByExample(CompanyInfo info){

        // ExampleMatcher
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("registrationAuthority", ExampleMatcher.GenericPropertyMatchers.contains())
                .withIgnorePaths("id");

        Example<CompanyInfo> ep = Example.of(info,exampleMatcher);

        return companyInfoRepository.findAll(ep);
    }

    public List<CompanyInfo> save(List<CompanyInfo> companyInfos){
        return companyInfoRepository.saveAll(companyInfos);
    }

}
