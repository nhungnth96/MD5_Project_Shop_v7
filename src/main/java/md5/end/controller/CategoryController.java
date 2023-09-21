package md5.end.controller;

import md5.end.exception.BadRequestException;
import md5.end.exception.NotFoundException;
import md5.end.model.dto.request.BrandRequest;
import md5.end.model.dto.request.CategoryRequest;
import md5.end.model.dto.response.BrandResponse;
import md5.end.model.dto.response.CategoryResponse;
import md5.end.service.IBrandService;
import md5.end.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    @PostMapping("")
    public ResponseEntity<CategoryResponse> add(
            @Valid
            @RequestBody CategoryRequest categoryRequest) throws BadRequestException, NotFoundException {
        return new ResponseEntity<>(categoryService.save(categoryRequest), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<CategoryResponse>> getAll() {
        return new ResponseEntity<>(categoryService.findAll(), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getOne(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(categoryService.findById(id), HttpStatus.OK);

    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> edit(
            @Valid
            @RequestBody CategoryRequest categoryRequest,
            @PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(categoryService.update(categoryRequest, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryResponse> delete(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(categoryService.deleteById(id), HttpStatus.OK);

    }


}