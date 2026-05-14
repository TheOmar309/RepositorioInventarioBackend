package com.company.inventory.response;

import java.util.List;
import com.company.inventory.model.Product;

public class ProductResponse {
    private List<Product> product;

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }
}