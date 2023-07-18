package com.kyozhou.jbooter.service.dao;

import com.kyozhou.jbooter.service.po.StandardRawDataPo;
import com.kyozhou.jbooter.system.utils.CommonUtility;
import com.mongodb.Mongo;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import com.mongodb.client.result.DeleteResult;

import java.util.List;

@Component
public class DataDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<StandardRawDataPo> getStandardRawData(String orgUUID, String sourceUUID, Long timeStart, Long timeEnd) {
        Criteria criteria = new Criteria();
        criteria.and("source_uuid").is(sourceUUID);
        criteria.and("org_uuid").is(orgUUID);
        criteria.and("timestamp").gte(timeStart).lt(timeEnd);
        Query query = new Query(criteria);
//        query.fields().include("breath_rate").include("gatem").include("gateo").include("heart_rate")
//                .include("sn_pw").include("snore").include("snore_count").include("celsius").include("timestamp");
        query.fields().exclude("_class").exclude("_id");
        query.with(new Sort(Sort.Direction.ASC, "timestamp"));
        return mongoTemplate.find(query, StandardRawDataPo.class);
    }

    public void insertMulti(List<StandardRawDataPo> standardRawDataPoList) {
        mongoTemplate.insertAll(standardRawDataPoList);
    }

    public Long clearStandardRawData() {
        Long timePoint = CommonUtility.getCurrentTimestamp() - 86400 * 7;
        Criteria criteria = new Criteria();
        criteria.and("timestamp").lt(timePoint);
        Query query = new Query(criteria);
        DeleteResult deleteResult = mongoTemplate.remove(query, "standard_raw_data");
        Long deletedCount = deleteResult.getDeletedCount();
        return deletedCount;
    }

}