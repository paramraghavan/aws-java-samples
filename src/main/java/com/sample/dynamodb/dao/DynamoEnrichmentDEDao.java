package com.sample.dynamodb.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.sample.dynamodb.domain.DynamoEnrichmentDE;


import java.util.List;

public class DynamoEnrichmentDEDao {

    private final DynamoDBMapper mapper;

    public DynamoEnrichmentDEDao(AmazonDynamoDB dynamoDb) {
        this.mapper = new DynamoDBMapper(dynamoDb);
    }

    public DynamoEnrichmentDE put(DynamoEnrichmentDE item) {
        mapper.save(item);
        return item;
    }

    public DynamoEnrichmentDE get(String pk) {
        return mapper.load(DynamoEnrichmentDE.class, pk);
    }

    public void delete(String pk) {
        DynamoEnrichmentDE item = new DynamoEnrichmentDE();
        item.setId(pk);

        DynamoDBMapperConfig overrideBehaviour = DynamoDBMapperConfig
                .builder()
                .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.CLOBBER)
                .build();

        mapper.delete(item, overrideBehaviour);
    }

    public List<DynamoEnrichmentDE> getAll() {
        return mapper.scan(DynamoEnrichmentDE.class, new DynamoDBScanExpression());
    }
}

