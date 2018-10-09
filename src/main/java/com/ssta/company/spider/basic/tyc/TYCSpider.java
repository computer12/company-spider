package com.ssta.company.spider.basic.tyc;

import com.ssta.company.spider.basic.ICSpider;
import com.ssta.company.spider.basic.common.GlobalParam;
import com.ssta.company.spider.basic.common.eneity.CompanyInfo;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class TYCSpider implements ICSpider {

    private static final String USER_AGENT = "User-Agent";
    private static final String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3493.3 Safari/537.36";

    private String baseUrl;

    private static final int pageSize = 10;
    private Integer dataNum;

    public TYCSpider(String baseUrl,String order,Integer dataNum) {
        if(order == null){
            order = "";
        }
        this.dataNum = dataNum;
        this.baseUrl = baseUrl.replace("{order}",order);
    }

    @Override
    public Connection getConnect(String conUrl) {
        final Connection con = Jsoup.connect(conUrl).header(USER_AGENT, USER_AGENT_VALUE);
        con.cookies(GlobalParam.TYC_MAP);
        return con;
    }

    @Override
    public List<CompanyInfo> getCompanyInfoByPage(Integer page) throws IOException {
        final String pageUrl = baseUrl.replace("{page}", String.valueOf(page));
        final Connection netCon = getConnect(pageUrl);

        Document document = Jsoup.parse(netCon.execute().body());
        final Elements select = document.select("a.name");

        final List<CompanyInfo> infos = select.stream()
                .map(e -> {
                            String href = e.attr("href");
                            String id = href.substring(href.lastIndexOf("/") + 1);

                            return new CompanyInfo(e.text(), page, href, id);
                        }
                ).collect(Collectors.toList());

        return infos;
    }

    @Override
    public int getMaxPage(){
        return dataNum/pageSize;
    }

    @Override
    public void setDeatil(CompanyInfo companyInfo) {
        try {
            final Connection.Response execute = getConnect(companyInfo.getDetailUrl()).execute();
            final String body = execute.body();
            Document document = Jsoup.parse(body);
            final Elements select = document.select("table.table.-striped-col.-border-top-none tr td");
            final Iterator<Element> iterator = select.iterator();
            while (iterator.hasNext()) {
                final Element next = iterator.next();
                if (next.text().startsWith("工商注册号") && iterator.hasNext()) {
                    companyInfo.setRegistNumber(iterator.next().text());
                } else if (next.text().startsWith("组织机构代码") && iterator.hasNext()) {
                    companyInfo.setOrgCode(iterator.next().text());
                } else if (next.text().startsWith("行业") && iterator.hasNext()) {
                    companyInfo.setIndustry(iterator.next().text());
                } else if (next.text().startsWith("统一信用代码") && iterator.hasNext()) {
                    companyInfo.setCreditCode(iterator.next().text());
                } else if (next.text().startsWith("纳税人识别号") && iterator.hasNext()) {
                    companyInfo.setRatepayerIdentifyNumber(iterator.next().text());
                } else if (next.text().startsWith("营业期限") && iterator.hasNext()) {
                    companyInfo.setRunRage(iterator.next().text());
                } else if (next.text().startsWith("公司类型") && iterator.hasNext()) {
                    companyInfo.setComanyType(iterator.next().text());
                } else if (next.text().startsWith("核准日期") && iterator.hasNext()) {
                    companyInfo.setApproveDate(iterator.next().text());
                } else if (next.text().startsWith("注册地址") && iterator.hasNext()) {
                    companyInfo.setAddress(iterator.next().text());
                } else if (next.text().startsWith("登记机关") && iterator.hasNext()) {
                    companyInfo.setRegistrationAuthority(iterator.next().text());
                } else if (next.text().startsWith("参保人数") && iterator.hasNext()) {
                    Integer insuredNum = null;
                    String strInsuredNum = iterator.next().text();
                    if (StringUtils.isNumericSpace(strInsuredNum)) {
                        insuredNum = Integer.valueOf(strInsuredNum);
                    }
                    companyInfo.setInsuredNum(insuredNum);
                } else if (next.text().startsWith("英文名称") && iterator.hasNext()) {
                    companyInfo.setEnglishName(iterator.next().text());
                } else if (next.text().startsWith("人员规模") && iterator.hasNext()) {
                    companyInfo.setStaffSize(iterator.next().text());
                }
            }
            Elements assets = document
                    .select("#_container_baseInfo table.table:first-child tbody tr:first-child td:nth-of-type(2)>div:nth-of-type(2)");
            // 注册资本数据
            for (Element element : assets) {
                companyInfo.setRegisteredAssets(element.attr("title"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
