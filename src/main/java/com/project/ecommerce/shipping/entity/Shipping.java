package com.project.ecommerce.shipping.entity;

import com.project.ecommerce.order.entity.Order;

import java.util.Date;
import java.util.UUID;

public class Shipping {
    private Long id;
    private Order order;
    private String address;
    private String city;
    private int postalCode;
    private String country;
    private ShippingStatus status;
    private UUID trackingNumber;
    private Date createdAt;
}
