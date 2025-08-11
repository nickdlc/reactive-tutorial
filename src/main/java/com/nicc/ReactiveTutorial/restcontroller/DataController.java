package com.nicc.ReactiveTutorial.restcontroller;

import com.nicc.ReactiveTutorial.model.Customer;
import com.nicc.ReactiveTutorial.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/api")
public class DataController {
    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @PostMapping("/customer")
    public Mono<ResponseEntity<Customer>> createCustomer(@RequestBody Customer customer) {
        return reactiveMongoTemplate.save(customer)
                .log()
                .map(savedCustomer -> ResponseEntity.status(201).body(savedCustomer));
    }

    @GetMapping("/customer/{customerId}")
    public Mono<ResponseEntity<Customer>> getCustomerById(@PathVariable String customerId) {
        return fetchCustomerById(customerId)
                .log()
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping("/customer/{customerId}/orders")
    public Flux<Order> getCustomerOrdersById(@PathVariable String customerId) {
        return fetchCustomerOrdersById(customerId)
                .log();
    }

    @PostMapping("/order")
    public Mono<ResponseEntity<Order>> createOrder(@RequestBody Order order) {
        return reactiveMongoTemplate.save(order)
                .log()
                .map(savedOrder -> ResponseEntity.status(201).body(savedOrder));
    }

    @GetMapping("/order/{orderId}")
    public Mono<ResponseEntity<Order>> getOrderById(@PathVariable String orderId) {
        return fetchOrderById(orderId)
                .log()
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping("/sales/summary")
    public Mono<ResponseEntity<Map<String, BigDecimal>>> getSales() {
        return reactiveMongoTemplate.findAll(Customer.class)
                .flatMap(customer -> fetchCustomerOrdersById(customer.getId())
                        .map(order -> Map.entry(
                                customer.getName(),
                                order.getTotal().subtract(order.getDiscount() != null ? order.getDiscount() : BigDecimal.ZERO)
                        )))
                .collectList()
                .map(entries -> entries.stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, BigDecimal::add)))
                .log()
                .map(ResponseEntity::ok);
    }

    private Mono<Customer> fetchCustomerById(String customerId) {
        Criteria criteria = Criteria.where("id").is(customerId);
        Query query = Query.query(criteria);

        // find returns more than one result, so use findOne
        return reactiveMongoTemplate.findOne(query, Customer.class)
                .log();
    }

    private Flux<Order> fetchCustomerOrdersById(String customerId) {
        Criteria criteria = Criteria.where("customerId").is(customerId);
        Query query = Query.query(criteria);

        return reactiveMongoTemplate.find(query, Order.class)
                .log();
    }

    private Mono<Order> fetchOrderById(String orderId) {
        Criteria criteria = Criteria.where("id").is(orderId);
        Query query = Query.query(criteria);

        return reactiveMongoTemplate.findOne(query, Order.class)
                .log();
    }
}
