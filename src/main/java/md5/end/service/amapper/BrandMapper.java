package md5.end.service.amapper;

import md5.end.model.dto.request.BrandRequest;
import md5.end.model.dto.response.BrandResponse;
import md5.end.model.entity.product.Brand;
import md5.end.model.entity.product.Product;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class BrandMapper implements IGenericMapper<Brand, BrandRequest, BrandResponse> {
    @Override
    public Brand getEntityFromRequest(BrandRequest brandRequest) {

        return Brand.builder()
                .name(brandRequest.getName())
                .build();
    }

    @Override
    public BrandResponse getResponseFromEntity(Brand brand) {
        return BrandResponse.builder()
                .id(brand.getId())
                .name(brand.getName())
                .productNames(brand.getProducts()!= null
                        ? brand.getProducts().stream()
                        .map(Product::getName)
                        .collect(Collectors.toList())
                        : Collections.emptyList())
                .build();
    }
}