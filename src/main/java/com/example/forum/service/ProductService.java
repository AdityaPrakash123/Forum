package com.example.forum.service;


import com.example.forum.model.Product;
import com.example.forum.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    private S3Service s3Service;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product createProduct(Product product, MultipartFile image) throws IOException {

        product.setListingDate(LocalDate.now().toString());
        product.setStatus(Product.Status.ACTIVE);


        if(image != null && !image.isEmpty()){
            String imageUrl = s3Service.uploadFile(image);
            product.setImageUrl(imageUrl);
        }

        return productRepository.save(product);


    }

    public Product deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

        if(product.getImageUrl() != null){
            s3Service.deleteFile(product.getImageUrl());
        }

        productRepository.delete(product);
        return product;
    }


    public Product updateProduct(Long id, Product product, MultipartFile image) throws IOException {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));




        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setSize(product.getSize());
        existingProduct.setBrand(product.getBrand());
        existingProduct.setCondition(product.getCondition());

        if(image != null && !image.isEmpty()){

            if(existingProduct.getImageUrl() != null){ // Delete old image and upload the new image
                s3Service.deleteFile(existingProduct.getImageUrl());
            }


            String imageUrl = s3Service.uploadFile(image);
            existingProduct.setImageUrl(imageUrl);
        }

        return productRepository.save(existingProduct);
    }




}
