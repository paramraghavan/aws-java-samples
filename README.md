# DynamoDB
- BatchWrite and BatchSave example,
- BatchWriteItem - Group Put and DeleteItem request
- BatchGetItem
- TransactionWriteItems - Write multiple items in a request
- TransactionGetItems
- A TransactWriteItems operation differs from a BatchWriteItem operation in that all the actions it contains must be completed successfully, or no changes are made at all. With a BatchWriteItem operation, it is possible that only some of the actions in the batch succeed while the others do not.
- Fluent Interface
- Number of retries, happens with for example  throttling errors retry happens, but not for authentication failures
- ClientConfiguration config = new ClientConfiguration()config.setMaxErrorRetry(0); // retry set to 0, so no retriesAmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withClientConfiguration(config).build():
- Exceptions, some examples
- TableNotFoundException - Table not found
- ProvisionedThroughputExceededException - Read/write throughput exceeded
- ItemCollectionSizeLimitExceededException - Collection is too large
- Partition Key <===> Hash Key
- Sort Key        <===> Range Key
- <img src="https://user-images.githubusercontent.com/52529498/214911292-19fd8a29-39e3-4f83-ae19-6fec8fa11b9b.png" width=50% height=50%>
- **Transaction API to the rescue**
- transactWriteItems(Consumer<TransactWriteItemsEnhancedRequest.Builder> requestConsumer)
Writes and/or modifies multiple items from one or more tables in a single atomic transaction. TransactGetItem is a composite operation where the request contains a set of up to 25 action requests, each containing a table reference and one of the following requests:
  * Condition check of item - ConditionCheck
  * Delete item - DeleteItemEnhancedRequest
  * Put item - PutItemEnhancedRequest
  * Update item - UpdateItemEnhancedRequest
  * <img src="https://user-images.githubusercontent.com/52529498/214924115-2428654f-b7bb-4ea1-a634-c5602952b5f5.png" width=50% height=50%>



# Java dynamodb
- https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/CodeSamples.Java.html
- https://github.com/awsdocs/aws-doc-sdk-examples
- https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/transaction-example.html
- https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBMapper.BatchWriteExample.html
https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/transaction-apis.html
- https://mkyong.com/java/how-to-check-which-jdk-version-compiled-the-class/
- https://docs.aws.amazon.com/amazondynamodb/latest/APIReference/API_TransactWriteItems.html
-http://docs.glngn.com/latest/api/software.amazon.awssdk.dynamodb/software/amazon/awssdk/services/dynamodb/model/TransactWriteItemsRequest.Builder.html#transactItems-java.util.Collection-
-https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/enhanced/dynamodb/DynamoDbEnhancedClient.html#transactWriteItems(java.util.function.Consumer)

# Check ports open/bound for listening,
- lsof -i -P | grep LISTEN | grep $PORT
- lsof -iTCP -sTCP:LISTEN -n -P
- kill process bound/listening to port 9000, kill -9 $(lsof -t -i:9000)
- javap -verbose ClassFile

# Run dynamodb locally
- https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBLocal.DownloadingAndRunning.html
- https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/CodeSamples.Java.html
- https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/index.html?com/amazonaws/services/dynamodbv2/datamodeling/package-summary.html


# docker-compose.yml, dynamodb local
- docker-compose content
```
version: '3.8'
services:
  dynamodb-local:
    command: "-jar DynamoDBLocal.jar -sharedDb -dbPath ./data"
    image: "amazon/dynamodb-local:latest"
    container_name: dynamodb-local
    ports:
      - "8000:8000"
    volumes:
      - "./docker/dynamodb:/home/dynamodblocal/data"
    working_dir: /home/dynamodblocal
```
- deploy docker, *docker-compose up* 
- https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBLocal.UsageNotes.html

# create local table
```
aws dynamodb create-table \
    --table-name Book \
    --attribute-definitions \
        AttributeName=id,AttributeType=S \
    --key-schema \
        AttributeName=id,KeyType=HASH \
    --provisioned-throughput \
        ReadCapacityUnits=1,WriteCapacityUnits=1 \
    --table-class STANDARD \
    --endpoint-url http://localhost:8000
```

# list local tables
'''
aws dynamodb list-tables --endpoint-url http://localhost:8000
```

# create a table
```
aws dynamodb create-table \
    --table-name Book \
    --attribute-definitions \
        AttributeName=id,AttributeType=S \
    --key-schema \
        AttributeName=id,KeyType=HASH \
    --provisioned-throughput \
        ReadCapacityUnits=1,WriteCapacityUnits=1 \
    --table-class STANDARD \
    --profile=tst
```

# Notes
# Mulitple git used id's on one machine
- https://www.freecodecamp.org/news/how-to-handle-multiple-git-configurations-in-one-machine/
