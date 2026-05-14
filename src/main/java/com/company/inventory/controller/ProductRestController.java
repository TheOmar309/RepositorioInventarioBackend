package com.company.inventory.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.company.inventory.model.Product;
import com.company.inventory.response.ProductResponseRest;
import com.company.inventory.services.IProductService;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/v1")
public class ProductRestController {

    @Autowired
    private IProductService productService;

    /**
     * Get all products
     * @return
     */
    @GetMapping("/products")
    public ResponseEntity<ProductResponseRest> searchProducts() {
        ResponseEntity<ProductResponseRest> response = productService.search();
        return response;
    }

    /**
     * Get product by id
     * @param id
     * @return
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponseRest> searchProductById(@PathVariable Long id) {
        ResponseEntity<ProductResponseRest> response = productService.searchById(id);
        return response;
    }

    /**
     * Save product
     * @param picture
     * @param name
     * @param price
     * @param account
     * @param categoryId
     * @return
     * @throws IOException
     */
    @PostMapping("/products")
    public ResponseEntity<ProductResponseRest> save(
            @RequestParam("picture") MultipartFile picture,
            @RequestParam("name") String name,
            @RequestParam("price") int price,
            @RequestParam("account") int account,
            @RequestParam("categoryId") Long categoryId) throws IOException {
        
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setAccount(account);
        product.setPicture(picture.getBytes()); // Convertimos la imagen recibida a un arreglo de bytes

        ResponseEntity<ProductResponseRest> response = productService.save(product, categoryId);
        return response;
    }

    /**
     * Update product
     * @param picture
     * @param name
     * @param price
     * @param account
     * @param categoryId
     * @param id
     * @return
     * @throws IOException
     */
    @PutMapping("/products/{id}")
    public ResponseEntity<ProductResponseRest> update(
            @RequestParam(value = "picture", required = false) MultipartFile picture,
            @RequestParam("name") String name,
            @RequestParam("price") int price,
            @RequestParam("account") int account,
            @RequestParam("categoryId") Long categoryId,
            @PathVariable Long id) throws IOException {
        
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setAccount(account);
        
        // Solo asignamos la imagen si el frontend envía una nueva
        if (picture != null) {
            product.setPicture(picture.getBytes());
        }

        ResponseEntity<ProductResponseRest> response = productService.update(product, categoryId, id);
        return response;
    }

    /**
     * Delete product
     * @param id
     * @return
     */
    @DeleteMapping("/products/{id}")
    public ResponseEntity<ProductResponseRest> delete(@PathVariable Long id) {
        ResponseEntity<ProductResponseRest> response = productService.deleteById(id);
        return response;
    }
}