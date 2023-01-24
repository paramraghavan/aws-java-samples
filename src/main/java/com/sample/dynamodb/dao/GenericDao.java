package com.sample.dynamodb.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;

import java.lang.reflect.Field;
import java.util.List;

public class GenericDao<T> {
    private final DynamoDBMapper mapper;

    public GenericDao(AmazonDynamoDB dynamoDb) {
        this.mapper = new DynamoDBMapper(dynamoDb);
    }

    public T put(T message) {
        mapper.save(message);
        return message;
    }

    public T get(Class<T> clazz, String id) {
        return mapper.load(clazz, id);
    }

    public void delete(Class<T> clazz, String id) throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        Class<T> clazz1 = clazz;
        clazz1.newInstance();

        Field field = clazz1.getClass().getField("id");
        field.set(clazz1, id);

        mapper.delete(clazz1);
    }

    public List<T> getAll(Class<T> clazz) {
        return (List<T>) mapper.scan(clazz, new DynamoDBScanExpression());
    }
}
