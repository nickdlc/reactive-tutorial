package com.nicc.ReactiveTutorial.restcontroller;

import com.nicc.ReactiveTutorial.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController()
@RequestMapping("/api")
public class DataController {
    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @PostMapping("/customer")
    public Mono<Customer> createCustomer(@RequestBody Customer customer) {
        return reactiveMongoTemplate.save(customer)
                .log();
    }

    @GetMapping("/customer/{customerId}")
    public Mono<Customer> getCustomerById(@PathVariable String customerId) {
        return fetchCustomerById(customerId)
                .log();
    }

    private Mono<Customer> fetchCustomerById(String customerId) {
        Criteria criteria = Criteria.where("id").is(customerId);
        Query query = Query.query(criteria);

        // find returns more than one result, so use findOne
        return reactiveMongoTemplate.findOne(query, Customer.class)
                .log();
    }
}
