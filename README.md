# Kafka-Producer
- Producer needs following things to produce a message <br>

  > Topic
    - The topic on which message will be produced [It can be stored in **constants**] <br><br>
      
  > Bootstrap-Server
    - Server on which kafka messages are send [It can be defined in **application properties**] <br><br>
  
  > Message
    - **simple message**
      - value serializer [It can be defined in **application properties**] <br>
        Property: "spring.kafka.producer.value-serializer" <br>
     
    - **key/value pair message** [RECOMMENDED]:
      - key & value serializer [It can be defined in **application properties**] <br>
        Property: "spring.kafka.producer.key-serializer" <br>
        Property: "spring.kafka.producer.value-serializer" <br><br>

  > Acknowledgement
    - To make system fault tolerant [It can be defined in **application properties**] <br>
    - It will depend on minimum insync replicas <br>
      Property: "spring.kafka.producer.acks" <br><br>

  > Retries
    - **[NOT RECOMMEDED]**
      - how many times the retry request will be sent [It can be defined in **application properties**] <br>
        Property: "spring.kafka.producer.retries" <br>
      - how much time it will wait before retrying [It can be defined in **application properties**] <br>
        Property: "spring.kafka.producer.properties.retry.backoff.ms" <br>

    - **[RECOMMEDED]**
      - Total delivery time = request sent + request acknowledge + retry [It can be defined in **application properties**] <br>
        Property: "spring.kafka.producer.properties.delivery.timeout.ms" <br>
      - It is used for buffering the messages based on specified time and send it inform of batch <br>
        Property: "spring.kafka.producer.properties.linger.ms" <br>
      - It is maximum waiting time of the messages which are sent <br>
        Property: "spring.kafka.producer.properties.request.timeout.ms" <br>        
        spring.kafka.producer.properties.delivery.timeout.ms = spring.kafka.producer.properties.linger.ms + spring.kafka.producer.properties.request.timeout.ms

> Idempotent Producer
  - **[RECOMMEDED]**
  - scenarios where events are very critical like banking systems. send duplicate messages is nightmare. To avoid this idempotency was introduced 
  - In latest version, this property is default to TRUE [Just check in config is set] <br>
    Property: "spring.kafka.producer.properties.enable.idempotence" <br>

  - It can be disabled if acks!=all, retries!=default value and max.in.fligh.connection !=5

> Transactions
- **[RECOMMENDED]**
-  scenarios where events are very critical like banking systems. send duplicate messages is nightmare. To avoid this transactions are introduced <br>
- It can be enable with the below property. It accepts string prefix
   Property: "spring.kafka.producer.transaction-id-prefix" <br>
