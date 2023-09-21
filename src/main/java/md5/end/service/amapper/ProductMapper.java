package md5.end.service.amapper;

import md5.end.exception.NotFoundException;
import md5.end.model.dto.request.ProductRequest;
import md5.end.model.dto.response.ProductResponse;
import md5.end.model.dto.response.ProductDetailResponse;
import md5.end.model.dto.response.ProductShopResponse;
import md5.end.model.entity.product.*;
import md5.end.repository.IBrandRepository;
import md5.end.repository.ICategoryRepository;
import md5.end.repository.ISpecRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class ProductMapper implements IGenericMapper<Product, ProductRequest, ProductResponse> {
    @Autowired
    ICategoryRepository categoryRepository;
    @Autowired
    IBrandRepository brandRepository;
    @Autowired
    ISpecRepository specRepository;

    @Override
    public Product getEntityFromRequest(ProductRequest productRequest) throws NotFoundException {
        Product product = new Product();
        product.setCreatedAt(LocalDateTime.now().toString());
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setImportPrice(productRequest.getImportPrice());
        product.setExportPrice(productRequest.getExportPrice());
        product.setStock(productRequest.getStock());
        product.setStatus(productRequest.getStatus());
        product.setBrand(brandRepository.findById(productRequest.getBrandId()).get());
        product.setCategory(categoryRepository.findById(productRequest.getCategoryId()).get());

        List<ProductSpec> specs = new ArrayList<>();
        if (productRequest.getSpecs() != null && !productRequest.getSpecs().isEmpty()) {
            for (Map.Entry<Long, String> entry : productRequest.getSpecs().entrySet()) {
                ProductSpec productSpec = new ProductSpec();
                productSpec.setProduct(product);
                Specification specification = specRepository.findById(entry.getKey()).orElse(null);
                productSpec.setSpecification(specification);
                productSpec.setValue(entry.getValue());
                specs.add(productSpec);
            }
        }
        product.setSpecifications(specs);

        return product;

    }

    @Override
    public ProductResponse getResponseFromEntity(Product product) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setBrandName(product.getBrand().getName());
        productResponse.setCategoryName(product.getCategory().getName());
        productResponse.setProductName(product.getName());
        productResponse.setDescription(product.getDescription());
        productResponse.setImportPrice(NumberFormat.getInstance().format(product.getImportPrice())+"$");
        productResponse.setExportPrice(NumberFormat.getInstance().format(product.getExportPrice())+"$");
        productResponse.setStock(product.getStock());
        if (product.getStatus() == 1) {
            productResponse.setStatus("On stock");
        } else if (product.getStatus() == 2) {
            productResponse.setStatus("Out of stock");
        } else {
            productResponse.setStatus("Stop business");
        }

        Map<String, String> specsMap = new HashMap<>();
        if (product.getSpecifications() != null && !product.getSpecifications().isEmpty()) {
            for (ProductSpec productSpec : product.getSpecifications()) {
                if (productSpec.getSpecification() != null) {
                    String specName = productSpec.getSpecification().getName();
                    String specValue = productSpec.getValue();
                    specsMap.put(specName, specValue);
                }
            }
        }
        productResponse.setSpecifications(specsMap);

        List<String> imageUrls = new ArrayList<>();
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            for (ProductImage productImage : product.getImages()) {
                if (productImage.getUrl() != null) {
                    imageUrls.add(productImage.getUrl());
                }
            }
        }
        productResponse.setSubImages(imageUrls);
        if(!imageUrls.isEmpty()){
            productResponse.setImage(imageUrls.get(0));
        }
        return productResponse;
    }

    public ProductDetailResponse getDetailResponseFromEntity(Product product) {
        ProductDetailResponse productDetailResponse = new ProductDetailResponse();
        productDetailResponse.setId(product.getId());
        productDetailResponse.setBrandName(product.getBrand().getName());
        productDetailResponse.setCategoryName(product.getCategory().getName());
        productDetailResponse.setProductName(product.getName());
        productDetailResponse.setDescription(product.getDescription());

        productDetailResponse.setPrice(NumberFormat.getInstance().format(product.getExportPrice())+"$");


        Map<String, String> specsMap = new HashMap<>();
        if (product.getSpecifications() != null && !product.getSpecifications().isEmpty()) {
            for (ProductSpec productSpec : product.getSpecifications()) {
                if (productSpec.getSpecification() != null) {
                    String specName = productSpec.getSpecification().getName();
                    String specValue = productSpec.getValue();
                    specsMap.put(specName, specValue);
                }
            }
        }
        productDetailResponse.setSpecifications(specsMap);

        List<String> imageUrls = new ArrayList<>();
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            for (ProductImage productImage : product.getImages()) {
                if (productImage.getUrl() != null) {
                    imageUrls.add(productImage.getUrl());
                }
            }
        }
        productDetailResponse.setSubImages(imageUrls);
        if(!imageUrls.isEmpty()){
            productDetailResponse.setImage(imageUrls.get(0));
        }
        return productDetailResponse;
    }
    public ProductShopResponse getShoppingResponseFromEntity(Product product) {
        ProductShopResponse productShopResponse = new ProductShopResponse();
        productShopResponse.setId(product.getId());
        productShopResponse.setBrandName(product.getBrand().getName());
        productShopResponse.setCategoryName(product.getCategory().getName());
        productShopResponse.setProductName(product.getName());
        productShopResponse.setPrice(NumberFormat.getInstance().format(product.getExportPrice())+"$");
        return productShopResponse;
    }

}