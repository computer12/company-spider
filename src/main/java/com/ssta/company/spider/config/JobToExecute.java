package com.ssta.company.spider.config;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class JobToExecute {
    public static final ConcurrentHashMap<Integer,Timer> jobTimes
            = new ConcurrentHashMap<>();

    public static void newJob(Integer id, TimerTask task,Date date,int itime){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task,date,itime * 60 * 60 *1000);
        jobTimes.put(id,timer);
        System.out.println(id + String.valueOf(date) + itime);
    }

    public static void cancel(Integer id){
        Timer timer = jobTimes.get(id);
        if(timer != null){
            timer.cancel();
            System.out.println("id:" + id + ",定时任务取消！");
            jobTimes.remove(id);
        }
        
    }
}
