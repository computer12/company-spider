package com.ssta.company.spider.basic.common.eneity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "i_spider_auth_conf")
public class AuthConfig {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private String targetWeb;
    private String authKey;
    private String authValue;
    private String authType;
    @Column(insertable = false)
    private String updateTime;
    @Column(insertable = false)
    private String Status;
}
