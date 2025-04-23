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
        Property: "spring.kafka.producer.value-serializer" <br>
