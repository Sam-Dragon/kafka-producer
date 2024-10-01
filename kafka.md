# Kafka Notes

> Definition
- Distributed event streaming platform that is used for collect, process, store & integrate data at scale
- It provides **loose coupling** between client & server applications
- Kafka is used for **asynchronous** communication
- Majorly used for distributing messages across multiple other microservices
- Follows **event-driven architecture**
- It is publisher-subscriber model [pub-sub model]
- It is useful in scaling

# Components

> Event
- An activity which has happened 
- It is immutable
- Naming Convention
<Noun><Action>Event --> ProductCreatedEvent

  > Ordering an event
  - In case of message key is absent, the data is load balanced & it can be stored in **any of the partition**. order is not followed and may raise conflicts
  - In case of message key is provided, the data is always stored in '**single partition**'
  - We can send multiple messages from a file

> Message vs Event
- Message is an envelope which contains an event in it which can be different formats [Json, String, Avro or Null]
- It can be in **key-value** pair
- It contains key, value, timestamps, [Optional] Headers - Metadata, tokens 

> Partition
- It is small **storage unit** which stores the data.
- It stores the data in files called logs, logs will be divided into smaller pieces called segments & each segment has size which is configurable
- Events are in order within the partition. Order across partition is not maintained
- Syntax : Partition-Index [It has number of offsets] 

> Topics
- It is under broker which stores the events produced to a specific topic and save it in partition
- It is nothing but list of partitions in which event is stored
- It is appended only, new event is always added at the end 
- Its default retention time is 7 days, but it is configurable
- It provides support for parallel processing and load balancing
- partitions can be increased in topic but doesn't decrease 

> Producer
- The one who publishes/produces the event to broker
- It serializes the data to be sent over the network
- It must specify topic name where it wants to produce the message to
- To enable auto creation of topic [auto.create.topics.enable=true]
- It can produce message in synchronous/asynchronous communication style

    > Acknowledgement
    - It is way to make kafka system more reliable & durable by configuring number of brokers to sent acknowledgement. It is **configurable property**
    - producer.acks = all, makes our system highly reliable but performance goes down. Ex: bank transactions
    - producer.acks = 0, makes our system less reliable but performance is good. Ex: Real time feed weather, stock
    - producer.acks = 1 **[Not Recommended]**, makes our system less reliable but performance is good. Ex: internal system calls
    - producer.acks = all with min.insync.replicas = 2 **[Highly recommended]**, makes system better and gives good performance<br><br>

    > Retries
    - It is a process of retrying the messages sent to the broker. It can happen if broker is not available, network issue etc. It is **configurable property**
    - It is applicable only for retriable errors
    - producer.retries = 10, retries for 10 times
    - producer.retries works better in combination with **producer.retries.backoff.ms** for better performance<br><br>
 
    > Delivery Timeout
    - max time for producer to wait for entire send time [Send Request + Acknowledgement + Retry]
    - It is replacement for **kafka retires**
    - producer.properties.delivery.timeout.ms = 120000 [2 minutes]
    - delivery.timeout.ms >= linger.ms + request.timeout.ms<br><br>

    > Linger
    - Mainly used for buffering messages before sending them. Basically, compresses the large messages for increasing throughput
    - producer.properties.linger.ms = 0<br><br>

    > Request Timeout
    - max time for producer can wait for response after single request
    - producer.properties.request.timeout.ms = 30000<br><br>
 
    > Idempotent
    - It is useful in preventing duplicate messages in the presence of failures or retries. It is required in banking applications
    - enable.idempotence=true, by default it is true
    - max.in.flight.requests.per.connection <= 5 must be set

> Broker
- It can be physical computer or virtual machine which runs kafka processes.
- Server which accepts events from producer & stores in its hard disk
- It manages Kafka topics, handles the storage of data into topic partitions, 
  manages replication of data for fault tolerance, and serves client requests (from both Producers and Consumers).
- It follows leader-follower mechanism, there is no single broker who will always be leader. Each broker can be leader/follower

> Consumer
- The one who consumes an event from broker(s) produced by producer
- It can consume latest message or all messages from beginning
- Messages in single partition is read in order but order b/w partitions is not guaranteed <br><br>

  > Deserialization problem
    - If producer produces data in some format whereas consumer expects in different format data
    - To solve this, we must use "error handling deserializer class" & specify value deserializer type <br><br>
 
  > Retries
    - It is a process of retrying the messages sent to the broker. It can happen if broker is not available, network issue etc
    - It is applicable only for retryable errors
    - we need to create new "exception classes" for retryable exception & add new properties for retries & interval 
    - consumer.retries = 10, consumer.interval.in.seconds = 5 <br><br>

  > **Dead Letter Topic [DLT]**
    - It records all the messages which have failed due to deserialization issues [producers and consumers] & can be viewed later for re-processing
    - This topic must end with extension [.DLT]
    - It requires all the producer configuration as kafka template wraps the messages & is useful in capturing deserialization errors directly


# Architecture
- Generally, we need more than broker. It works on leader-follower server. 
- In case leader goes down, then one of the follower becomes leader and continue to work
- leader replicates the data into follower servers
- Every kafka broker can be leader and follower at same time.
- Each partition is assigned to a broker when topic is created/modified

> Disadvantages of stopping producers/consumers abruptly
- Avoid losing messages
- Avoid errors
- To gracefully shutdown kafka services is by calling kafka-server-stop.bat or kafka-server-stop.sh script

<br>Note : 
- Queue is similar to kafka topic but difference is event is not deleted once consumed. it stays on memory till cleaning process in not scheduled 
- Partitions can be increased even after configured, but it cannot be decreased
