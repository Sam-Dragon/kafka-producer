package com.kafka.product.rest.product;

import com.kafka.product.constants.KafkaConstants;
import com.kafka.product.entity.Product;
import com.kafka.product.model.ProductRequest;
import com.kafka.product.model.pubsub.ProductCreatedEvent;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private ModelMapper mapper;

    @Autowired
    private KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    List<Product> products = new ArrayList<>();

    @Override
    public String createAsynchronously(ProductRequest request) {
        String productId = UUID.randomUUID()
                .toString();
        var event = mapper.map(request, ProductCreatedEvent.class);
        event.setId(productId);

        // Produce kafka event
        var future = kafkaTemplate.send(KafkaConstants.CREATE_PRODUCT_TOPIC, productId, event);
        future.whenComplete((result, exception) -> {
            if (exception != null) {
                System.out.println("Producer Exception :: {}" + exception.getMessage());
            }
            System.out.println("Producer Response :: {}" + result.getRecordMetadata());
        });

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
                LOGGER.error("Producer Exception :: {}", exception.getMessage());
            }
            LOGGER.info("Producer Response :: {}", response.getRecordMetadata());
        });

        // TO make it synchronous operation
        future.join();

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

        LOGGER.info("Product Response :: {}", response);

        var product = mapper.map(event, Product.class);
        products.add(product);

        return productId;
    }

    @Override
    public List<Product> findAll() {
        return products;
    }
}
