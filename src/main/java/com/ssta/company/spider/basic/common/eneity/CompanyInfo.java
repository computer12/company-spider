package com.ssta.company.spider.basic.common.eneity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @Author Xavier
 * @Description 企业信息实体类
 * @Date 2018/8/24 14:53
 **/
@Getter
@Setter
@Entity
@Table(name = "i_company")
public class CompanyInfo {
    // 数据库id
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private String id;
    // 公司名
    private String name;
    // 所在页码
    private Integer page;
    // 详情页url
    private String detailUrl;
    // 数据来源
    private String targetWeb;
    // 工商注册号
    private String registNumber;
    // 组织机构代码
    private String orgCode;
    // 行业
    private String industry;
    // 统一信用代码
    private String creditCode;
    // 纳税人识别号
    private String ratepayerIdentifyNumber;
    // 以下非重要字段
    // 注册资本
    private String registeredAssets;
    // 营业期限
    private String runRage;
    // 公司类型
    private String comanyType;
    // 核准日期
    private String approveDate;
    // 注册地址（企业地址）
    private String address;
    // 登记机关
    private String registrationAuthority;
    // 参保人数
    private Integer insuredNum;
    // 英文名称
    private String EnglishName;
    // 人员规模
    private String staffSize;
    // 任务执行状态id
    private Integer executeId;
    @Column(insertable = false)
    private String updateTime;

    public CompanyInfo() {
    }

    public CompanyInfo(String name, Integer page, String detailUrl, String id) {
        this.id = id;
        this.name = name;
        this.page = page;
        this.detailUrl = detailUrl;
    }

    @Override
    public String toString() {
        return "CompanyInfo{" +
                "id：'" + id + '\'' +
                "公司名称：'" + name + '\'' +
                ", 分页：'" + page + '\'' +
                ", 工商注册号：'" + registNumber + '\'' +
                ", 组织机构代码：'" + orgCode + '\'' +
                ", 行业：'" + industry + '\'' +
                ", 统一信用代码：'" + creditCode + '\'' +
                ", 纳税人识别号：'" + ratepayerIdentifyNumber + '\'' +
                ", 明细页面地址：'" + detailUrl + '\'' +
                ", 注册资本='" + registeredAssets + '\'' +
                ", 营业期限='" + runRage + '\'' +
                ", 公司类型='" + comanyType + '\'' +
                ", 核准日期='" + approveDate + '\'' +
                ", 企业地址='" + address + '\'' +
                ", 登记机关='" + registrationAuthority + '\'' +
                ", 参保人数=" + insuredNum +
                ", 英文名称='" + EnglishName + '\'' +
                ", 人员规模='" + staffSize + '\'' +
                ", 数据来源='" + targetWeb + '\'' +
                ", 任务执行id='" + executeId + '\'' +
                ", 更新时间='" + updateTime + '\'' +
                '}';
    }
}
