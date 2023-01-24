package com.sample.dynamodb.bll;

import java.text.SimpleDateFormat;
import java.util.*;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.sample.dynamodb.Utils;
import com.sample.dynamodb.dao.BookDao;
import com.sample.dynamodb.dao.GenericDao;
import com.sample.dynamodb.domain.Book;

/*
* Ref: https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBMapper.BatchWriteExample.html
* */

public class DynamoDBMapperBatchWriteExample {

    static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2")).build();
    static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public static void main(String[] args) throws Exception {
        try {

            DynamoDBMapper mapper = new DynamoDBMapper(client);

            Book book3 = testBatchSave(mapper);
            //testBatchDelete(mapper);
            testBatchWrite(mapper, book3.getId());
            //getBooks(client);
            getDomainItems(client, Book.class);
            getDomainItems(client, DynamoDBMapperBatchWriteExample.Forum.class);
            getDomainItems(client, Thread.class);
            System.out.println("Example complete!");

        }
        catch (Throwable t) {
            System.err.println("Error running the DynamoDBMapperBatchWriteExample: " + t);
            t.printStackTrace();
        }
    }


    private static <T> void getDomainItems(AmazonDynamoDB client, Class<T> clazz) {
        GenericDao messageDao = new GenericDao(client);
        List items =  messageDao.getAll(clazz);
        Iterator<T> it = items.iterator();
        while (it.hasNext()) {
            T item =  it.next();
            System.out.println(item);
        }

    }
    private static void getBooks(AmazonDynamoDB client) {
        BookDao messageDao = new BookDao(client);
        List books =  messageDao.getAll();
        Iterator<Book> it = books.iterator();

        while (it.hasNext()) {
            System.out.println(it.next());
        }

    }
    private static Book testBatchSave(DynamoDBMapper mapper) {
        Utils.createTable(Book.class, client);
        Book book1 = new Book(true, "902-11-11-1111", "Book", "My book created in batch write",100, 10 );
        Book book2 = new Book(true, "902-11-12-1111", "Book", "My second book created in batch write",200, 20 );
        Book book3 = new Book(false, "902-11-13-1111", "Book", "My third book created in batch write",300, 25 );

        System.out.println("Adding three books to ProductCatalog table.");
        mapper.batchSave(Arrays.asList(book1, book2, book3));
        return book3;
    }

    private static void testBatchDelete(DynamoDBMapper mapper) {

        Book book1 = mapper.load(Book.class, 901);
        Book book2 = mapper.load(Book.class, 902);
        System.out.println("Deleting two books from the ProductCatalog table.");
        mapper.batchDelete(Arrays.asList(book1, book2));
    }

    private static void testBatchWrite(DynamoDBMapper mapper, String id) {
        Utils.createTable(Forum.class, client);
        // Create Forum item to save
        Forum forumItem = new Forum();
        forumItem.name = "Test BatchWrite Forum";
        forumItem.threads = 0;
        forumItem.category = "Amazon Web Services";

        Utils.createTable(Thread.class, client);
        // Create Thread item to save
        Thread threadItem = new Thread();
        threadItem.forumName = "AmazonDynamoDB";
        threadItem.subject = "My sample question";
        threadItem.message = "BatchWrite message";
        List<String> tags = new ArrayList<String>();
        tags.add("batch operations");
        tags.add("write");
        threadItem.tags = new HashSet<String>(tags);

        // Load ProductCatalog item to delete
        Book book3 = mapper.load(Book.class, id);

        List<Object> objectsToWrite = Arrays.asList(forumItem, threadItem);
        List<Book> objectsToDelete = Arrays.asList(book3);

        DynamoDBMapperConfig config = DynamoDBMapperConfig.builder()
            .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.CLOBBER)
        .build();

        try {
            mapper.batchWrite(objectsToWrite, objectsToDelete, config);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    @DynamoDBTable(tableName = "Reply")
    public static class Reply {
        private String id;
        private String replyDateTime;
        private String message;
        private String postedBy;

        // Partition key
        @DynamoDBHashKey(attributeName = "Id")
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        // Sort key
        @DynamoDBRangeKey(attributeName = "ReplyDateTime")
        public String getReplyDateTime() {
            return replyDateTime;
        }

        public void setReplyDateTime(String replyDateTime) {
            this.replyDateTime = replyDateTime;
        }

        @DynamoDBAttribute(attributeName = "Message")
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @DynamoDBAttribute(attributeName = "PostedBy")
        public String getPostedBy() {
            return postedBy;
        }

        public void setPostedBy(String postedBy) {
            this.postedBy = postedBy;
        }
    }

    @DynamoDBTable(tableName = "Thread")
    public static class Thread {
        private String forumName;
        private String subject;
        private String message;
        private String lastPostedDateTime;
        private String lastPostedBy;
        private Set<String> tags;
        private int answered;
        private int views;
        private int replies;

        // Partition key
        @DynamoDBHashKey(attributeName = "ForumName")
        public String getForumName() {
            return forumName;
        }

        public void setForumName(String forumName) {
            this.forumName = forumName;
        }

        // Sort key
        @DynamoDBRangeKey(attributeName = "Subject")
        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        @DynamoDBAttribute(attributeName = "Message")
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @DynamoDBAttribute(attributeName = "LastPostedDateTime")
        public String getLastPostedDateTime() {
            return lastPostedDateTime;
        }

        public void setLastPostedDateTime(String lastPostedDateTime) {
            this.lastPostedDateTime = lastPostedDateTime;
        }

        @DynamoDBAttribute(attributeName = "LastPostedBy")
        public String getLastPostedBy() {
            return lastPostedBy;
        }

        public void setLastPostedBy(String lastPostedBy) {
            this.lastPostedBy = lastPostedBy;
        }

        @DynamoDBAttribute(attributeName = "Tags")
        public Set<String> getTags() {
            return tags;
        }

        public void setTags(Set<String> tags) {
            this.tags = tags;
        }

        @DynamoDBAttribute(attributeName = "Answered")
        public int getAnswered() {
            return answered;
        }

        public void setAnswered(int answered) {
            this.answered = answered;
        }

        @DynamoDBAttribute(attributeName = "Views")
        public int getViews() {
            return views;
        }

        public void setViews(int views) {
            this.views = views;
        }

        @DynamoDBAttribute(attributeName = "Replies")
        public int getReplies() {
            return replies;
        }

        public void setReplies(int replies) {
            this.replies = replies;
        }

        @Override
        public String toString() {
            return "Thread{" +
                    "forumName='" + forumName + '\'' +
                    ", subject='" + subject + '\'' +
                    ", message='" + message + '\'' +
                    ", lastPostedDateTime='" + lastPostedDateTime + '\'' +
                    ", lastPostedBy='" + lastPostedBy + '\'' +
                    ", tags=" + tags +
                    ", answered=" + answered +
                    ", views=" + views +
                    ", replies=" + replies +
                    '}';
        }
    }

    @DynamoDBTable(tableName = "Forum")
    public static class Forum {
        private String name;
        private String category;
        private int threads;

        // Partition key
        @DynamoDBHashKey(attributeName = "Name")
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @DynamoDBAttribute(attributeName = "Category")
        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        @DynamoDBAttribute(attributeName = "Threads")
        public int getThreads() {
            return threads;
        }

        public void setThreads(int threads) {
            this.threads = threads;
        }

        @Override
        public String toString() {
            return "Forum{" +
                    "name='" + name + '\'' +
                    ", category='" + category + '\'' +
                    ", threads=" + threads +
                    '}';
        }
    }
}


