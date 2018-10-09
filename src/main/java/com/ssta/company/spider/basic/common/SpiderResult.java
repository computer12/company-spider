package com.ssta.company.spider.basic.common;

import com.ssta.company.spider.basic.common.eneity.CompanyInfo;

import java.util.List;

public class SpiderResult {
    // 企业信息
    private List<CompanyInfo> companyInfos;
    // 被墙标记
    private Boolean isForbidden;

    public static SpiderResult newInstance(List<CompanyInfo> companyInfos, Boolean isForbidden) {
        return new SpiderResult(companyInfos, isForbidden);
    }

    public SpiderResult(List<CompanyInfo> companyInfos, Boolean isForbidden) {
        this.companyInfos = companyInfos;
        this.isForbidden = isForbidden;
    }

    public List<CompanyInfo> getCompanyInfos() {
        return companyInfos;
    }

    public void setCompanyInfos(List<CompanyInfo> companyInfos) {
        this.companyInfos = companyInfos;
    }

    public Boolean getForbidden() {
        if (isForbidden == null) {
            return true;
        }
        return isForbidden;
    }

    public void setForbidden(Boolean forbidden) {
        isForbidden = forbidden;
    }
}
