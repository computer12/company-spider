package com.ssta.company.spider.basic.common.eneity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "i_spider_job")
public class SpiderJob {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private String targetWeb;
    private String startTime;
    private Integer intervalTime;
    private Integer dataNum;
    private String baseUrl;
    private String eOrder;
    @Column(insertable = false,updatable = false)
    private String insertTime;
    @Column(insertable = false)
    private String updateTime;
    @Column(insertable = false)
    private int status;
}
