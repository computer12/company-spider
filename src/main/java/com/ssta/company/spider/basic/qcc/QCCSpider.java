package com.ssta.company.spider.basic.qcc;

import com.ssta.company.spider.basic.ICSpider;
import com.ssta.company.spider.basic.common.GlobalParam;
import com.ssta.company.spider.basic.common.eneity.CompanyInfo;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QCCSpider implements ICSpider {
    public static final String USER_AGENT = "user-agent";
    public static final String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3493.3 Safari/537.36";
    public static final String URL_PREFIX = "https://www.qichacha.com";

    private String baseUrl;
    private static final Integer pageSize = 10;
    private final Integer dataNum;

    public QCCSpider(String baseUrl,Integer dataNum){
        this.baseUrl = baseUrl;
        this.dataNum = dataNum;
    }

    @Override
    public int getMaxPage(){
        return dataNum/pageSize;
    }

    @Override
    public Connection getConnect(String conUrl) {
        final Connection con = Jsoup.connect(conUrl).header(USER_AGENT, USER_AGENT_VALUE)
                .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .header("accept-encoding", "gzip, deflate, br")
                .header("accept-language", "zh-CN,zh;q=0.9")
                .header("cache-control", "max-age=0")
                .header("upgrade-insecure-requests", "1");

        con.cookies(GlobalParam.QCC_MAP);

        return con;
    }

    @Override
    public List<CompanyInfo> getCompanyInfoByPage(Integer page) throws IOException {

        String url = baseUrl + "_" + page;
        final Connection con = getConnect(url);

        Connection.Response execute = con.execute();
        List<CompanyInfo> companyInfos = new ArrayList<>();
        final String body = execute.body();

        Document document = Jsoup.parse(body);
        final Elements select = document.select("a.list-group-item.clearfix");
        for (Element element : select) {
            String id = element.attr("href").replace("/firm_", "").replace(".html", "");
            companyInfos.add(
                    new CompanyInfo(
                            element.selectFirst("span.name").text(),
                            page,
                            URL_PREFIX + element.attr("href"),
                            id));
        }
        return companyInfos;
    }

    @Override
    public void setDeatil(CompanyInfo companyInfo) {
        try {
            final Connection.Response execute = getConnect(companyInfo.getDetailUrl()).execute();
            final String body = execute.body();
            Document document = Jsoup.parse(body);
            final Elements select = document.select("table.ntable tbody tr td");
            final Iterator<Element> iterator = select.iterator();
            while (iterator.hasNext()) {
                final Element next = iterator.next();
                if (next.text().startsWith("注册号：") && iterator.hasNext()) {
                    companyInfo.setRegistNumber(iterator.next().text());
                } else if (next.text().equals("组织机构代码：") && iterator.hasNext()) {
                    companyInfo.setOrgCode(iterator.next().text());
                } else if (next.text().equals("所属行业：") && iterator.hasNext()) {
                    companyInfo.setIndustry(iterator.next().text());
                } else if (next.text().equals("统一社会信用代码：") && iterator.hasNext()) {
                    companyInfo.setCreditCode(iterator.next().text());
                } else if (next.text().equals("纳税人识别号：") && iterator.hasNext()) {
                    companyInfo.setRatepayerIdentifyNumber(iterator.next().text());
                } else if (next.text().equals("注册资本：") && iterator.hasNext()) {
                    companyInfo.setRegisteredAssets(iterator.next().text());
                } else if (next.text().equals("营业期限") && iterator.hasNext()) {
                    companyInfo.setRunRage(iterator.next().text());
                } else if (next.text().equals("公司类型：") && iterator.hasNext()) {
                    companyInfo.setComanyType(iterator.next().text());
                } else if (next.text().equals("核准日期：") && iterator.hasNext()) {
                    companyInfo.setApproveDate(iterator.next().text());
                } else if (next.text().equals("企业地址：") && iterator.hasNext()) {
                    companyInfo.setAddress(iterator.next().text());
                } else if (next.text().equals("登记机关：") && iterator.hasNext()) {
                    companyInfo.setRegistrationAuthority(iterator.next().text());
                } else if (next.text().equals("参保人数") && iterator.hasNext()) {
                    Integer insuredNum = null;
                    try {
                        insuredNum = Integer.valueOf(iterator.next().text());
                    } catch (NumberFormatException e) {
                        System.out.println("参保人数无数据！");
                    }
                    companyInfo.setInsuredNum(insuredNum);
                } else if (next.text().equals("英文名：") && iterator.hasNext()) {
                    companyInfo.setEnglishName(iterator.next().text());
                } else if (next.text().equals("人员规模") && iterator.hasNext()) {
                    companyInfo.setStaffSize(iterator.next().text());
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
