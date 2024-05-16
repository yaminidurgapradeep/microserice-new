package com.learningmicroservices.productservice.repository;

import com.learningmicroservices.productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
