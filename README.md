# Kafka-Producer

> Producer needs following things to produce a message
- topic: the topic on which message will be produced [It can be stored in **constants**]
- message: It can be done in two ways
  - simple message: value serializer [It can be defined in **application properties**] <br>
    Property: "spring.kafka.producer.value-serializer" <br>
    
  - key/value pair message: key & value serializer [It can be defined in **application properties**] <br>
    Property: "spring.kafka.producer.key-serializer" <br>
    Property: "spring.kafka.producer.value-serializer" <br>
