package com.company.inventory.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.inventory.dao.ICategoryDao;
import com.company.inventory.dao.IProductDao;
import com.company.inventory.model.Category;
import com.company.inventory.model.Product;
import com.company.inventory.response.ProductResponseRest;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ICategoryDao categoryDao;

    @Autowired
    private IProductDao productDao;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ProductResponseRest> search() {
        ProductResponseRest response = new ProductResponseRest();
        try {
            List<Product> product = (List<Product>) productDao.findAll();
            response.getProduct().setProduct(product);
            response.setMetadata("Respuesta ok", "00", "Respuesta Exitosa");
        } catch (Exception e) {
            response.setMetadata("Respuesta NO OK", "-1", "Error al consultar productos");
            e.printStackTrace(); 
            return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ProductResponseRest> searchById(Long id) {
        ProductResponseRest response = new ProductResponseRest();
        List<Product> list = new ArrayList<>();
        try {
            Optional<Product> product = productDao.findById(id);
            if (product.isPresent()) {
                list.add(product.get());
                response.getProduct().setProduct(list);
                response.setMetadata("Respuesta OK", "00", "Producto encontrado");
            } else {
                response.setMetadata("Respuesta NO OK", "-1", "Error producto no encontrado");
                return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.setMetadata("Respuesta NO OK", "-1", "Error al consultar por ID");
            e.printStackTrace(); 
            return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<ProductResponseRest> save(Product product, Long categoryId) {
        ProductResponseRest response = new ProductResponseRest();
        List<Product> list = new ArrayList<>();

        try {
            Optional<Category> category = categoryDao.findById(categoryId);

            if (category.isPresent()) {
                product.setCategory(category.get());
            } else {
                response.setMetadata("Respuesta nok", "-1", "Categoría no encontrada");
                return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
            }

            Product productSaved = productDao.save(product);

            if (productSaved != null) {
                list.add(productSaved);
                response.getProduct().setProduct(list);
                response.setMetadata("Respuesta ok", "00", "Producto guardado");
            } else {
                response.setMetadata("Respuesta nok", "-1", "Producto no guardado");
                return new ResponseEntity<ProductResponseRest>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            response.setMetadata("Respuesta nok", "-1", "Error al guardar el producto");
            e.printStackTrace();
            return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<ProductResponseRest> update(Product product, Long categoryId, Long id) {
        ProductResponseRest response = new ProductResponseRest();
        List<Product> list = new ArrayList<>();
        
        try {
            // 1. Buscamos la categoría primero
            Optional<Category> category = categoryDao.findById(categoryId);
            if (!category.isPresent()) {
                response.setMetadata("Respuesta NO OK", "-1", "Error: Categoría no encontrada");
                return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
            }

            // 2. Buscamos el producto a actualizar
            Optional<Product> productSearch = productDao.findById(id);
            if (productSearch.isPresent()) {
                
                // Actualizamos los campos
                productSearch.get().setName(product.getName());
                productSearch.get().setPrice(product.getPrice());
                productSearch.get().setAccount(product.getAccount());
                productSearch.get().setCategory(category.get());
                
                // Solo actualizamos la imagen si el frontend envía una nueva
                if (product.getPicture() != null) {
                    productSearch.get().setPicture(product.getPicture());
                }

                Product productToUpdate = productDao.save(productSearch.get());
                
                if (productToUpdate != null) {
                    list.add(productToUpdate);
                    response.getProduct().setProduct(list);
                    response.setMetadata("Respuesta OK", "00", "Producto actualizado");
                } else {
                    response.setMetadata("Respuesta NO OK", "-1", "Producto no actualizado");
                    return new ResponseEntity<ProductResponseRest>(response, HttpStatus.BAD_REQUEST);                            
                }        
            } else {
                response.setMetadata("Respuesta NO OK", "-1", "Error producto no encontrado");
                return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.setMetadata("Respuesta NO OK", "-1", "Error al actualizar producto");
            e.printStackTrace(); 
            return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);    
    }

    @Override
    @Transactional
    public ResponseEntity<ProductResponseRest> deleteById(Long id) {
        ProductResponseRest response = new ProductResponseRest();
        try {
            productDao.deleteById(id);
            response.setMetadata("Respuesta ok", "00", "Registro eliminado");
        } catch (Exception e) {
            response.setMetadata("Respuesta NO OK", "-1", "Error al eliminar");
            e.printStackTrace(); 
            return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
    }
}