package com.ssta.company.spider.config;

import com.ssta.company.spider.basic.ICSpider;
import com.ssta.company.spider.basic.common.SpiderResult;
import com.ssta.company.spider.basic.common.Spiders;
import com.ssta.company.spider.basic.common.eneity.CompanyInfo;
import com.ssta.company.spider.basic.common.eneity.SpiderJob;
import com.ssta.company.spider.basic.common.eneity.SpiderJobExecute;
import com.ssta.company.spider.builder.Utils;
import com.ssta.company.spider.service.SpiderJobExecuteService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.TimerTask;

public class SpiderTimerTask extends TimerTask {
    private SpiderJob spiderJob;
    private ICSpider spider;
    private Spiders spiders;
    private SpiderJobExecuteService spiderJobExecuteService;

    public SpiderTimerTask(SpiderJob spiderJob,ICSpider spider, Spiders spiders,SpiderJobExecuteService spiderJobExecuteService){
        this.spiderJob = spiderJob;
        this.spider = spider;
        this.spiders = spiders;
        this.spiderJobExecuteService = spiderJobExecuteService;
    }

    @Override
    public void run() {
        SpiderJobExecute sje = new SpiderJobExecute();
        sje.setJobId(spiderJob.getId());
        sje.setTargetWeb(spiderJob.getTargetWeb());
        sje.setDataNum(0);

        SpiderJobExecute sjeSave = spiderJobExecuteService.save(sje);
        try {
//            List<CompanyInfo> companyInfos = Spiders.toRun(spider);
            SpiderResult spiderResult = Spiders.toRun(spider);
            List<CompanyInfo> companyInfos = spiderResult.getCompanyInfos();
            companyInfos.forEach(
                    cc->{
                        cc.setExecuteId(sjeSave.getId());
                        cc.setTargetWeb(spiderJob.getTargetWeb());
                    }
            );

            spiders.save(companyInfos);
            sje.setRunStatus(1);
            sje.setDataNum(companyInfos.size());
            if(spiderResult.getForbidden()){
                sje.setRunStatus(9);
                sje.setDataNum(0);
                sje.setEndMessage("访问被限制");
                spiderJobExecuteService.mark(spiderJob.getTargetWeb());
            }
        } catch (IOException|SQLException e) {
            e.printStackTrace();
            sje.setRunStatus(9);
            sje.setDataNum(0);
            sje.setEndMessage(e.getMessage());
            spiderJobExecuteService.mark(spiderJob.getTargetWeb());
        }
        sje.setEndTime(Utils.getCurrentTime());
        spiderJobExecuteService.save(sje);
    }
}
