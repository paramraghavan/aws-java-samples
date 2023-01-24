package com.sample.dynamodb.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.sample.dynamodb.domain.Book;

import java.util.List;

public class BookDao {
    private final DynamoDBMapper mapper;

    public BookDao(AmazonDynamoDB dynamoDb) {
        this.mapper = new DynamoDBMapper(dynamoDb);
    }

    public Book put(Book message) {
        mapper.save(message);
        return message;
    }

    public Book get(String id) {
        return mapper.load(Book.class, id);
    }

    public void delete(String id) {
        Book message = new Book();
        message.setId(id);

        mapper.delete(message);
    }

    public List<Book> getAll() {
        return mapper.scan(Book.class, new DynamoDBScanExpression());
    }
}
