package com.ssta.company.spider.config;

import com.ssta.company.spider.basic.common.Spiders;
import com.ssta.company.spider.basic.common.eneity.SpiderJob;
import com.ssta.company.spider.service.AuthConfService;
import com.ssta.company.spider.service.SpiderJobExecuteService;
import com.ssta.company.spider.service.SpiderJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(value = 1)
public class AppSetUp implements CommandLineRunner {
    @Autowired
    SpiderJobService spiderJobService;

    @Autowired
    SpiderJobExecuteService spiderJobExecuteService;

    @Autowired
    AuthConfService authConfService;

    @Autowired
    Spiders spiders;

    @Override
    public void run(String... args) throws Exception {
        authConfService.setToStatic();



        SpiderJob sj = new SpiderJob();
        sj.setStatus(1);
        List<SpiderJob> spiderJobs = spiderJobService.findByEntity(sj);

        for (SpiderJob spiderJob : spiderJobs) {
            spiderJobExecuteService.runNewJob(spiderJob);
        }
    }
}
