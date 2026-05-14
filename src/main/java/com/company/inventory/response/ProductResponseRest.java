package com.company.inventory.response;

public class ProductResponseRest extends ResponseRest {
    
    private ProductResponse productResponse = new ProductResponse();

    // Este es el método que te está pidiendo Eclipse
    public ProductResponse getProduct() {
        return productResponse;
    }

    public void setProduct(ProductResponse productResponse) {
        this.productResponse = productResponse;
    }
}