package com.ssta.company.spider.basic;

import com.ssta.company.spider.basic.common.eneity.CompanyInfo;
import org.jsoup.Connection;

import java.io.IOException;
import java.util.List;

public interface ICSpider {
    Connection getConnect(String conUrl);

    int getMaxPage();

    List<CompanyInfo> getCompanyInfoByPage(Integer page)  throws IOException;


    // 核心代码
    void setDeatil(CompanyInfo companyInfo);

    default List<CompanyInfo> setDetailInfos(List<CompanyInfo> companyInfos) {
        companyInfos.forEach(this::setDeatil);
        return companyInfos;
    }
}
