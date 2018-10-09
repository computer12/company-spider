package com.ssta.company.spider.controller;

import com.ssta.company.spider.basic.common.eneity.AuthConfig;
import com.ssta.company.spider.builder.MsgBuilder;
import com.ssta.company.spider.builder.Utils;
import com.ssta.company.spider.service.AuthConfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/authConf")
public class AuthConfigController {
    @Autowired
    AuthConfService authConfService;

    @PostMapping("/put")
    public Map save(AuthConfig ac) {
        if(ac.getId() != null){
            return MsgBuilder.buildReturnErrorMessage("无效添加！");
        }
        return MsgBuilder.buildReturnMessage(authConfService.insert(ac));
    }

    @PostMapping("/delete/{id}")
    public Map delete(@PathVariable Integer id) {
        AuthConfig delete = authConfService.delete(id);
        if(delete == null){
            return MsgBuilder.buildReturnErrorMessage("删除失败！");
        }
        return MsgBuilder.buildReturnMessage("已删除!");
    }

    @PostMapping("/post")
    public Map update(AuthConfig ac) {
        if(ac.getId() == null){
            return MsgBuilder.buildReturnErrorMessage("保存配置信息不存在！");
        }
        ac.setStatus("1");
        ac.setUpdateTime(Utils.getCurrentTime());
        ac = authConfService.insert(ac);
        return MsgBuilder.buildReturnMessage(ac);
    }

    @GetMapping("/get")
    public Map getByinfo(AuthConfig ac) {
        // 只能查询有效的
        ac.setStatus("1");
        return MsgBuilder.buildReturnMessage(authConfService.findByEntity(ac));
    }
}
