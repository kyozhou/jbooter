package com.kyozhou.jbooter.service.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.MongoBulkWriteException;
import com.mongodb.client.model.InsertManyOptions;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReportDao {

    @Autowired
    private MongoTemplate mongoTemplate;
    private static final Logger logger = LoggerFactory.getLogger(ReportDao.class);

    public ObjectNode getBeacon2(String sourceUUID, String day) {
        Criteria criteria = new Criteria();
        criteria.and("source_uuid").is(sourceUUID);
        criteria.and("report_date").is(day);
        Query query = new Query(criteria);
        String beacon2Report = mongoTemplate.findOne(query, String.class, "beacon2_report");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(beacon2Report).deepCopy();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public void saveBeacon2Report(List<ObjectNode> reportList) {
        ObjectMapper mapper = new ObjectMapper();
        List<Document> documentList = new ArrayList<>();
        for(ObjectNode beacon2Report : reportList) {
            Document document = null;
            try {
                document = Document.parse(mapper.writeValueAsString(beacon2Report));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            documentList.add(document);
        }
        if (!documentList.isEmpty()) {
            InsertManyOptions insertManyOptions = new InsertManyOptions();
            insertManyOptions.ordered(false);
            try {
                mongoTemplate.getCollection("beacon2_report").insertMany(documentList, insertManyOptions);
            } catch (MongoBulkWriteException e) {
                logger.warn(e.getMessage());
            }
        }
    }
}
