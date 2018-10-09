package com.ssta.company.spider.service;

import com.ssta.company.spider.basic.ICSpider;
import com.ssta.company.spider.basic.common.Spiders;
import com.ssta.company.spider.basic.common.eneity.SpiderJob;
import com.ssta.company.spider.basic.common.eneity.SpiderJobExecute;
import com.ssta.company.spider.basic.common.eneity.SysParam;
import com.ssta.company.spider.basic.common.repository.SpiderJobExecuteRepository;
import com.ssta.company.spider.basic.common.repository.SysParamRepository;
import com.ssta.company.spider.basic.qcc.QCCSpider;
import com.ssta.company.spider.basic.tyc.TYCSpider;
import com.ssta.company.spider.builder.Utils;
import com.ssta.company.spider.config.JobToExecute;
import com.ssta.company.spider.config.SpiderTimerTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SpiderJobExecuteService {

    @Autowired
    SpiderJobExecuteRepository executeRepository;

    @Autowired
    SysParamRepository sysParamRepository;

    @Autowired
    Spiders spiders;

    @Autowired
    JdbcTemplate template;

    public List<SpiderJobExecute> findByInfo(SpiderJobExecute sje){
        Example<SpiderJobExecute> example = Example.of(sje);
        return executeRepository.findAll(example);
    }

    public void runNewJob(SpiderJob spiderJob){
        ICSpider spider = null;
        if("TYC".equals(spiderJob.getTargetWeb())){
            spider = new TYCSpider(
                    spiderJob.getBaseUrl(),
                    spiderJob.getEOrder(),
                    spiderJob.getDataNum()
            );
        }else if("QCC".equals(spiderJob.getTargetWeb())){
            spider = new QCCSpider(spiderJob.getBaseUrl(),spiderJob.getDataNum());
        }
        SpiderTimerTask task = new SpiderTimerTask(spiderJob,spider,spiders,this);
        Date firstTime = Utils.getFirstTime(spiderJob.getStartTime());

        JobToExecute.newJob(
                spiderJob.getId(),
                task,
                firstTime,
                spiderJob.getIntervalTime());
    }

    public SpiderJobExecute save(SpiderJobExecute spiderJobExecute){
        return executeRepository.save(spiderJobExecute);
    }

    public void mark(String target){
        SysParam sysParam = new SysParam();
        sysParam.setName(target);
        sysParam.setType("error");
        sysParam.setValue("true");

        Example<SysParam> example = Example.of(sysParam);
        Optional<SysParam> findOne = sysParamRepository.findOne(example);
        if(findOne.isPresent()){
            return;
        }
        sysParamRepository.save(sysParam);
    }

    public void cancelMark(String target){
        SysParam sysParam = new SysParam();
        sysParam.setName(target);
        sysParam.setType("error");
        sysParam.setValue("true");
        Example<SysParam> example = Example.of(sysParam);

        Optional<SysParam> findOne = sysParamRepository.findOne(example);
        if(findOne.isPresent()){
            SysParam os = findOne.get();
            sysParamRepository.delete(os);
        }
    }

    public List<SysParam> getMark(){
        SysParam sysParam = new SysParam();
        sysParam.setType("error");
        sysParam.setValue("true");
        Example<SysParam> example = Example.of(sysParam);
        return sysParamRepository.findAll(example);
    }
}
