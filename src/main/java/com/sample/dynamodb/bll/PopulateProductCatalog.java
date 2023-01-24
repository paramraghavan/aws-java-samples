package com.sample.dynamodb.bll;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.sample.dynamodb.dao.BookDao;
import com.sample.dynamodb.domain.Book;
import com.sample.dynamodb.Utils;

import java.util.Iterator;
import java.util.List;

// local dynamodb
public class PopulateProductCatalog {
    public static void main(String... args) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2")).build();

        Utils.createTable(Book.class, client);
        createBooks(client);
        getBooks(client);
    }

    private static void createBooks(AmazonDynamoDB client) {
        BookDao messageDao = new BookDao(client);

//        messageDao.put(
//                new Book(true,
//                        "123ABC0011",
//                        "Book",
//                        "Book11",
//                        2001,
//                        2.551));
//
//                messageDao.put(
//                new Book(true,
//                        "123ABC00111",
//                        "Book",
//                        "Book111",
//                        20011,
//                        2.5511));
//
//                messageDao.put(book);
//                  Update example
//                Book book =     new Book(true,
//                    "123ABC00111",
//                    "Book",
//                    "Book111-111",
//                    20011,
//                    2.5511);
//               String id =  "86ff86f5-7a78-48f2-b200-ac45702809a0";
//               book.setId(id);
//               messageDao.put(book);
    }

    private static void getBooks(AmazonDynamoDB client) {
        BookDao messageDao = new BookDao(client);
        List books =  messageDao.getAll();
        Iterator<Book> it = books.iterator();

        while (it.hasNext()) {
            System.out.println(it.next());
        }

    }

}
