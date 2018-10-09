package com.ssta.company.spider.controller;

import com.ssta.company.spider.basic.common.eneity.SpiderJob;
import com.ssta.company.spider.basic.common.eneity.SpiderJobExecute;
import com.ssta.company.spider.builder.MsgBuilder;
import com.ssta.company.spider.builder.Utils;
import com.ssta.company.spider.config.JobToExecute;
import com.ssta.company.spider.service.SpiderJobExecuteService;
import com.ssta.company.spider.service.SpiderJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/spiderJob")
public class SpiderJobController {

    @Autowired
    SpiderJobService spiderJobService;

    @Autowired
    SpiderJobExecuteService spiderJobExecuteService;


    @PostMapping("/put")
    public Map save(SpiderJob spiderJob){
        if(spiderJob.getId() != null){
            return MsgBuilder.buildReturnErrorMessage("无效添加！");
        }
        SpiderJob insertSj = spiderJobService.insert(spiderJob);
        spiderJobExecuteService.runNewJob(insertSj);
        return MsgBuilder.buildReturnMessage(insertSj);
    }

    @PostMapping("/delete/{id}")
    public Map delete(@PathVariable Integer id){
        SpiderJob delete = spiderJobService.delete(id);
        if(delete == null){
            return MsgBuilder.buildReturnErrorMessage("删除目标任务不存在！");
        }
        JobToExecute.cancel(id);
        return MsgBuilder.buildReturnMessage("已删除!");
    }

    @PostMapping("/post")
    public Map update(SpiderJob spiderJob){
        if(spiderJob.getId() == null){
            return MsgBuilder.buildReturnErrorMessage("保存爬虫任务不存在！");
        }
        // 取消旧的
        JobToExecute.cancel(spiderJob.getId());

        spiderJob.setUpdateTime(Utils.getCurrentTime());
        spiderJob.setStatus(1);
        SpiderJob insertjob = spiderJobService.insert(spiderJob);

        // 开始新的
        spiderJobExecuteService.runNewJob(insertjob);

        return MsgBuilder.buildReturnMessage(insertjob);
    }

    @GetMapping("/get")
    public Map getByinfo(SpiderJob sj) {
        sj.setStatus(1);

        return MsgBuilder.buildReturnMessage(spiderJobService.findByEntity(sj));
    }

    @GetMapping("/execute/get")
    public Map getExecutes(SpiderJobExecute sje){
        List<SpiderJobExecute> sjes = spiderJobExecuteService.findByInfo(sje);
        return MsgBuilder.buildReturnMessage(sjes);
    }

    @GetMapping("/mark/get")
    public Map findMark(){
        return MsgBuilder.buildReturnMessage(spiderJobExecuteService.getMark());
    }

    @GetMapping("/mark/delete")
    public Map cancelMark(String targetWeb){
        spiderJobExecuteService.cancelMark(targetWeb);
        return MsgBuilder.buildReturnMessage("取消");
    }
}
