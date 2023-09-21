package md5.end.service.impl;

import md5.end.exception.BadRequestException;
import md5.end.exception.NotFoundException;
import md5.end.model.dto.request.BrandRequest;
import md5.end.model.dto.request.CategoryRequest;
import md5.end.model.dto.response.BrandResponse;
import md5.end.model.dto.response.CategoryResponse;
import md5.end.model.entity.product.Brand;
import md5.end.model.entity.product.Category;
import md5.end.repository.IBrandRepository;
import md5.end.repository.ICategoryRepository;
import md5.end.service.ICategoryService;
import md5.end.service.amapper.BrandMapper;
import md5.end.service.amapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService implements ICategoryService {
    @Autowired
    private ICategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper categoryMapper;


    @Override
    public List<CategoryResponse> findAll() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> categoryMapper.getResponseFromEntity(category))
                .collect(Collectors.toList());
    }




    @Override
    public CategoryResponse findById(Long id) throws NotFoundException {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (!categoryOptional.isPresent()) {
            throw new NotFoundException("Category's id "+id+" not found.");
        }
        return categoryMapper.getResponseFromEntity(categoryOptional.get());
    }


    @Override
    public CategoryResponse findByName(String name) throws NotFoundException {
        Optional<Category> categoryOptional = categoryRepository.findByName(name);
        if (!categoryOptional.isPresent()) {
            throw new NotFoundException("Category's name "+name+" not found.");

        }
        return categoryMapper.getResponseFromEntity(categoryOptional.get());

    }

    @Override
    public CategoryResponse save(CategoryRequest categoryRequest) throws BadRequestException {
        Optional<Category> categoryOptional = categoryRepository.findByName(categoryRequest.getName());
        if(categoryOptional.isPresent()){
            throw new BadRequestException("Brand's name: " + categoryRequest.getName() + " is already existed");
        }
        Category category = categoryRepository.save(categoryMapper.getEntityFromRequest(categoryRequest));
        return categoryMapper.getResponseFromEntity(category);
    }

    @Override
    public CategoryResponse update(CategoryRequest categoryRequest, Long id) throws NotFoundException {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (!categoryOptional.isPresent()) {
            throw new NotFoundException("Category's id "+id+" not found.");
        }
        Category category = categoryMapper.getEntityFromRequest(categoryRequest);
        category.setId(id);
        return categoryMapper.getResponseFromEntity(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse deleteById(Long id) throws NotFoundException {
        Optional<Category> brandOptional = categoryRepository.findById(id);
        if (!brandOptional.isPresent()) {
            throw new NotFoundException("Category's id "+id+" not found.");
        }
        categoryRepository.deleteById(id);
        return categoryMapper.getResponseFromEntity(brandOptional.get());
    }


}
