package com.ssta.company.spider.service;

import com.ssta.company.spider.basic.common.GlobalParam;
import com.ssta.company.spider.basic.common.eneity.AuthConfig;
import com.ssta.company.spider.basic.common.repository.AuthConfigRepository;
import com.ssta.company.spider.builder.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class AuthConfService {

    @Autowired
    JdbcTemplate template;

    @Autowired
    AuthConfigRepository repository;

    public AuthConfig insert(AuthConfig ac){
        AuthConfig save = repository.save(ac);
        setToStatic();
        return save;
    }

    public List<AuthConfig> findByEntity(AuthConfig authConfig){
        Example<AuthConfig> ep = Example.of(authConfig);
        return repository.findAll(ep);
    }

    public AuthConfig delete(Integer id){
        AuthConfig ac = repository.findById(id).orElse(null);
        if (ac == null){
            return null;
        }
        ac.setUpdateTime(Utils.getCurrentTime());
        ac.setStatus("0");
        AuthConfig save = repository.save(ac);
        setToStatic();

        return save;
    }

    public void setToStatic(){
        AuthConfig ac = new AuthConfig();
        ac.setStatus("1");
        ac.setAuthType("cookie");
        List<AuthConfig> authConfigs = findByEntity(ac);
        GlobalParam.TYC_MAP = new HashMap<>();
        GlobalParam.QCC_MAP = new HashMap<>();

        for (AuthConfig authConfig : authConfigs) {
            if("TYC".equals(authConfig.getTargetWeb())){
                GlobalParam.TYC_MAP.put(authConfig.getAuthKey(),authConfig.getAuthValue());
            }else if("QCC".equals(authConfig.getTargetWeb())){
                GlobalParam.QCC_MAP.put(authConfig.getAuthKey(),authConfig.getAuthValue());
            }
        }
        System.out.println(GlobalParam.TYC_MAP);
        System.out.println(GlobalParam.QCC_MAP);
    }
}
