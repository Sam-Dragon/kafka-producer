# Kafka Notes

> Definition
- Distributed event streaming platform that is used for collect, process, store & integrate data at scale
- It provides **loose coupling** between client & server applications
- Kafka is used for **asyncronous** communication
- Majorly used for distributing messages across multiple other microservices
- Follows **even-driven architecture**
- It is publisher-subscriber model [pubsub model]
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
  - We can send mutiple messages from a file

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
- It is append only, new event is always added at the end 
- Its default retention time is 7 days but it is configurable
- It provides support for parallel processing and load balancing
- partitions can be increased in topic but doesnt decrease 

> Producer
- The one who publishes the event to broker
- To enable auto creation of topic [auto.create.topics.enable=true]

> Broker
- It can be physical computer or virtual machine which runs kafka processes.
- Server which accepts events from producer & stores in its hard disk
- It manages Kafka topics, handles the storage of data into topic partitions, 
  manages replication of data for fault tolerance, and serves client requests (from both Producers and Consumers).
- It follows leader-follower mechanism, there is no single broker who will always be leader. Each broker can be leader/follower

> Consumer
- The one who consumes an event from broker
- It can consume new message or all messages from beginning

# Architecture
- Generally, we need more than broker. It works on leader-follwer server. 
- In case leader goes down, then one of the follower becomes leader and continue to work
- leader replicates the data into follower servers
- Every kafka broker can be leader and follower at same time.
- Each partition is assigned to a broker when topic is created/modified


# Disadavantages of stopping producers/consumers abruptly
- Avoid losing messages
- Avoid errors
- To gracefully shutdown kafka services is by calling kafka-server-stop.bat or kafka-server-stop.sh script

# Idempotent
- Producer:
  - To prevent duplicate messages in the logs on failure of network and retry

Note: 
- Queue is similiar to kafka topic but difference is event is not deleted once consumed. it stays on memory till cleaning process in not scheduled 
- Partitions can be increased even after configured but it cannot be decreased

# Kafka Properties
- Delivery Timeout = max time for producer to wait for entire send time [Send Request + Acknoledgement + Retry] - It is replacement for kafka retires
- Request Timeout = max time for producer can wait after single request
