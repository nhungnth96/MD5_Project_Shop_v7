package md5.end.service;

import md5.end.exception.NotFoundException;
import md5.end.model.dto.request.CategoryRequest;
import md5.end.model.dto.response.CategoryResponse;
import md5.end.model.entity.product.Category;


import java.util.List;
import java.util.Optional;

public interface ICategoryService extends IGenericService<CategoryRequest, CategoryResponse> {
    CategoryResponse findByName(String name) throws NotFoundException;
}