package md5.end.service.amapper;

import md5.end.model.dto.request.BrandRequest;
import md5.end.model.dto.request.CategoryRequest;
import md5.end.model.dto.response.BrandResponse;
import md5.end.model.dto.response.CategoryResponse;
import md5.end.model.entity.product.Brand;
import md5.end.model.entity.product.Category;
import md5.end.model.entity.product.Product;
import md5.end.repository.ICategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper implements IGenericMapper<Category, CategoryRequest, CategoryResponse> {
    @Autowired
    ICategoryRepository categoryRepository;
    @Override
    public Category getEntityFromRequest(CategoryRequest categoryRequest) {

        return Category.builder()
                .name(categoryRequest.getName())
                .build();
    }

    @Override
    public CategoryResponse getResponseFromEntity(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .productNames(category.getProducts()!= null
                        ? category.getProducts().stream()
                        .map(Product::getName)
                        .collect(Collectors.toList())
                        : Collections.emptyList())
                .build();
    }

    }

