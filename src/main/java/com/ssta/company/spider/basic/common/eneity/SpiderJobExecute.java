package com.ssta.company.spider.basic.common.eneity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "i_spider_job_execute")
public class SpiderJobExecute {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private Integer jobId;
    private String targetWeb;
    @Column(insertable = false,updatable = false)
    private String startTime;
    private String endTime;
    @Column(insertable = false)
    private Integer runStatus;
    private String endMessage;
    @Column(insertable = false)
    private Integer DataNum;
}
