package com.ssta.company.spider.service;

import com.ssta.company.spider.basic.common.eneity.SpiderJob;
import com.ssta.company.spider.basic.common.repository.SpiderJobRepository;
import com.ssta.company.spider.builder.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpiderJobService {
    @Autowired
    SpiderJobRepository spiderJobRepository;
    @Autowired
    JdbcTemplate template;

    public List<SpiderJob> findByEntity(SpiderJob spiderJob){
        Example<SpiderJob> sj = Example.of(spiderJob);
        return spiderJobRepository.findAll(sj);
    }

    public SpiderJob insert(SpiderJob spiderJob){
        return spiderJobRepository.save(spiderJob);
    }

    public SpiderJob delete(Integer id){
        SpiderJob sj = spiderJobRepository.findById(id).orElse(null);
        if (sj == null){
            return null;
        }
        sj.setUpdateTime(Utils.getCurrentTime());
        sj.setStatus(0);

        return spiderJobRepository.save(sj);
    }
}
