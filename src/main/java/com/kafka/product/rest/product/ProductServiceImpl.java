package com.kafka.product.rest.product;

import com.kafka.product.constants.KafkaConstants;
import com.kafka.product.entity.Product;
import com.kafka.product.model.ProductRequest;
import com.kafka.product.model.pubsub.ProductCreatedEvent;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private final ModelMapper mapper;

    @Autowired
    private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    List<Product> products = new ArrayList<>();

    public ProductServiceImpl(ModelMapper mapper, KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate) {
        this.mapper = mapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String createAsynchronously(ProductRequest request) {
        String productId = UUID.randomUUID()
                .toString();
        var event = mapper.map(request, ProductCreatedEvent.class);
        event.setId(productId);

        // Producer Record along with headers having unique key
        ProducerRecord<String, ProductCreatedEvent> record = new ProducerRecord<>(KafkaConstants.CREATE_PRODUCT_TOPIC, productId, event);
        var messageId = UUID.randomUUID().toString().getBytes();

        // Hardcoded the messaged id for testing duplicate message id
        // messageId = "12345".getBytes();

        record.headers().add("messageId", messageId);

        // Produce kafka event
        var future = kafkaTemplate.send(record);
        future.whenComplete((result, exception) -> {
            if (exception != null) {
                System.out.println("Producer Exception :: {}" + exception.getMessage());
            }
            System.out.println("Producer Response :: {}" + result.getRecordMetadata());
        });

        System.out.println("Returning Product Id :: " + productId);

        var product = mapper.map(event, Product.class);
        products.add(product);

        return productId;
    }

    @Override
    public String createSynchronouslyUsingFutureJoin(ProductRequest request) {
        String productId = UUID.randomUUID()
                .toString();
        var event = mapper.map(request, ProductCreatedEvent.class);
        event.setId(productId);

        // Produce kafka event
        var future = kafkaTemplate.send(KafkaConstants.CREATE_PRODUCT_TOPIC, productId, event);
        future.whenComplete((response, exception) -> {
            if (exception != null) {
                System.out.println("Producer Exception :: " + exception.getMessage());
            }
            System.out.println("Producer Response :: " + response.getRecordMetadata());
        });

        System.out.println("Called Join Method");

        // TO make it synchronous operation
        future.join();

        System.out.println("Returning Product Id :: " + productId);

        var product = mapper.map(event, Product.class);
        products.add(product);

        return productId;
    }

    @Override
    public String createSynchronouslyUsingGet(ProductRequest request) throws Exception {
        String productId = UUID.randomUUID()
                .toString();
        var event = mapper.map(request, ProductCreatedEvent.class);
        event.setId(productId);

        // Produce kafka event
        var response = kafkaTemplate.send(KafkaConstants.CREATE_PRODUCT_TOPIC, productId, event)
                .get(5, TimeUnit.SECONDS);


        System.out.println("Producer Response > Partition :: " + response.getRecordMetadata().partition());
        System.out.println("Producer Response > Topic :: " + response.getRecordMetadata().topic());
        System.out.println("Producer Response > Offset :: " + response.getRecordMetadata().offset());

        System.out.println("Product Response :: " + response);
        System.out.println("Returning Product Id :: " + productId);

        var product = mapper.map(event, Product.class);
        products.add(product);

        return productId;
    }

    @Override
    public List<Product> findAll() {
        return products;
    }
}
