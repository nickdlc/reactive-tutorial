package com.nicc.ReactiveTutorial.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Document
public class Order {
    @Id
    private String id;
    private String customerId;
    private BigDecimal total;
    private BigDecimal discount;

    public Order() {
    }

    public Order(String id, String customerId, BigDecimal total, BigDecimal discount) {
        this.id = UUID.randomUUID().toString();
        this.customerId = customerId;
        this.total = total;
        this.discount = discount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(customerId, order.customerId) && Objects.equals(total, order.total) && Objects.equals(discount, order.discount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, total, discount);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", customerId='" + customerId + '\'' +
                ", total=" + total +
                ", discount=" + discount +
                '}';
    }
}
