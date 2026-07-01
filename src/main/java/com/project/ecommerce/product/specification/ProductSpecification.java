package com.project.ecommerce.product.specification;

import com.project.ecommerce.product.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {
    public static Specification<Product> isActive(){
        return (root, query, cb) ->
                cb.isTrue(root.get("active"));
    }
    public static Specification<Product> hasCategory(Long categoryId){
        return (root, query, cb) ->
                cb.equal(root.get("category").get("id"), categoryId);
    }
    public static Specification<Product> nameContains(String name){
        return (root, query, cb) ->{
            if(name == null || name.isBlank()){
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }
    public static Specification<Product> priceGreaterThan(BigDecimal price){
        return (root, query, cb) ->{
            if(price == null){
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("price"), price);
        };
    }
}
