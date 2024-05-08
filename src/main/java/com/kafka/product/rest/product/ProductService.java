package com.kafka.product.rest.product;

import com.kafka.product.entity.Product;
import com.kafka.product.model.ProductRequest;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ProductService {

    String createAsynchronously(ProductRequest request);

    String createSynchronouslyUsingFutureJoin(ProductRequest request);

    String createSynchronouslyUsingGet(ProductRequest request) throws Exception;

    List<Product> findAll();
}
