package com.sample.dynamodb.bll;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.sample.dynamodb.dao.DynamoEnrichmentDEDao;
import com.sample.dynamodb.domain.DynamoEnrichmentDE;


public class QueryDynamoEnrichmentDE {
    public static void main(String... args) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .build();
        String pk = "uat:101:0:b33f6ca1a7634ed5029c";
        getById(client, pk);
    }
    private static void getById(AmazonDynamoDB client, String id) {
        DynamoEnrichmentDEDao dynamoEnrichmentDEDao = new DynamoEnrichmentDEDao(client);
        DynamoEnrichmentDE item = dynamoEnrichmentDEDao.get(id);
        System.out.println(item);
    }
}
