package com.ssta.company.spider.basic.common;

import com.ssta.company.spider.basic.ICSpider;
import com.ssta.company.spider.basic.common.eneity.CompanyInfo;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Log
public class Spiders {

    static JdbcTemplate template;

    @Autowired
    public void beforeInit(JdbcTemplate template) {
        Spiders.template = template;
    }

    public static SpiderResult toRun(ICSpider spider) throws IOException {
        boolean impose = false;
        List<CompanyInfo> allcompanyInfos = new ArrayList<>();

        for (int i = 1; i <= spider.getMaxPage(); i++) {
            List<CompanyInfo> companyInfos = spider.getCompanyInfoByPage(i);
            if(companyInfos.size() == 0){
                log.warning("明细页获取信息异常，停止！发生页【" + i + "】");
                break;
            }
            if (!impose)
                spider.setDetailInfos(companyInfos);

            impose = !companyInfos.stream()
                    .map(CompanyInfo::getOrgCode)
                    .allMatch(StringUtils::isNotEmpty);
            if (impose) {
                companyInfos = companyInfos.stream().filter(
                        e -> StringUtils.isNotBlank(e.getOrgCode())
                ).collect(Collectors.toList());
            }
            allcompanyInfos.addAll(companyInfos);
        }
        return SpiderResult.newInstance(allcompanyInfos,!impose);
    }

    public static void save(final List<CompanyInfo> infos) throws SQLException {
        String sql = "insert into I_company(id,name,page,detail_url,regist_number,org_code,industry,credit_code," +
                "ratepayer_identify_number,registered_assets,run_rage,comany_type,approve_date,address," +
                "registration_authority,insured_num,english_name,staff_size,target_web,execute_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)" +
                "on duplicate key update name = ?,page = ?,detail_url = ?,regist_number = ?,org_code = ?,industry = ?," +
                "credit_code = ?,ratepayer_identify_number = ?,registered_assets = ?,run_rage = ?,comany_type = ?," +
                "approve_date = ?,address = ?,registration_authority = ?,insured_num = ?,english_name = ?,staff_size = ?"+
                ",execute_id = ?";

        template.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                CompanyInfo info = infos.get(i);

                ps.setString(1, info.getId());
                ps.setString(2, info.getName());
                ps.setInt(3, info.getPage());
                ps.setString(4, info.getDetailUrl());
                ps.setString(5, info.getRegistNumber());
                ps.setString(6, info.getOrgCode());
                ps.setString(7, info.getIndustry());
                ps.setString(8, info.getCreditCode());
                ps.setString(9, info.getRatepayerIdentifyNumber());
                ps.setString(10, info.getRegisteredAssets());
                ps.setString(11, info.getRunRage());
                ps.setString(12, info.getComanyType());
                ps.setString(13, info.getApproveDate());
                ps.setString(14, info.getAddress());
                ps.setString(15, info.getRegistrationAuthority());

                // null 处理
                final Integer insuredNum = info.getInsuredNum();
                if (insuredNum != null) {
                    ps.setInt(16, insuredNum);
                } else {
                    ps.setNull(16, Types.INTEGER);
                }
                ps.setString(17, info.getEnglishName());
                ps.setString(18, info.getStaffSize());
                ps.setString(19, info.getTargetWeb());
                ps.setInt(20, info.getExecuteId());

                ps.setString(21, info.getName());
                ps.setInt(22, info.getPage());
                ps.setString(23, info.getDetailUrl());
                ps.setString(24, info.getRegistNumber());
                ps.setString(25, info.getOrgCode());
                ps.setString(26, info.getIndustry());
                ps.setString(27, info.getCreditCode());
                ps.setString(28, info.getRatepayerIdentifyNumber());
                ps.setString(29, info.getRegisteredAssets());
                ps.setString(30, info.getRunRage());
                ps.setString(31, info.getComanyType());
                ps.setString(32, info.getApproveDate());
                ps.setString(33, info.getAddress());
                ps.setString(34, info.getRegistrationAuthority());

                // null 处理
                if (insuredNum != null) {
                    ps.setInt(35, insuredNum);
                } else {
                    ps.setNull(35, Types.INTEGER);
                }
                ps.setString(36, info.getEnglishName());
                ps.setString(37, info.getStaffSize());

                ps.setInt(38, info.getExecuteId());

            }
            @Override
            public int getBatchSize() {
                return infos.size();
            }
        });
    }
}
