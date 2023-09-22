package md5.end.controller;

import md5.end.exception.BadRequestException;
import md5.end.exception.NotFoundException;
import md5.end.model.dto.request.BrandRequest;
import md5.end.model.dto.request.ProductRequest;
import md5.end.model.dto.response.BrandResponse;
import md5.end.model.dto.response.ProductResponse;

import md5.end.model.entity.user.User;
import md5.end.security.principal.UserDetailService;
import md5.end.service.IProductService;
import md5.end.service.impl.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")

public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private UserDetailService userDetailService;

    @GetMapping("")
    public ResponseEntity<Page<?>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        return new ResponseEntity<>(productService.findShoppingForUser(page,size), HttpStatus.OK);
    }

    @GetMapping("/search-by-name")
    public ResponseEntity<?> searchByName(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) throws NotFoundException {
        return new ResponseEntity<>(productService.findByName(name,page,size), HttpStatus.OK);

    }
    @GetMapping("/search-by-status")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> searchByStatus(
            @RequestParam(defaultValue = "") int status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) throws NotFoundException {
        return new ResponseEntity<>(productService.findByStatus(status,page,size), HttpStatus.OK);
    }

    // detail phân 2 dto
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) throws NotFoundException {
        User user = userDetailService.getCurrentUser();
        if(user.getRoles().size()==3) {
            return new ResponseEntity<>(productService.findById(id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(productService.findForUser(id), HttpStatus.OK);
        }
    }

    @PostMapping("")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ProductResponse> add(
            @Valid
            @RequestBody ProductRequest productRequest
    ) throws BadRequestException, NotFoundException {
        return new ResponseEntity<>(productService.save(productRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ProductResponse> edit(
            @Valid
            @RequestBody ProductRequest productRequest,
            @PathVariable Long id
            ) throws NotFoundException {
        return new ResponseEntity<>(productService.update(productRequest,id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ProductResponse> delete(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(productService.deleteById(id), HttpStatus.OK);

    }

    @PutMapping("/{productId}/upload-image")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ProductResponse> uploadImage(@PathVariable Long productId, @RequestParam(value = "files") List<MultipartFile> files) throws NotFoundException {
        return new ResponseEntity<>(productService.insertImage(productId,files), HttpStatus.OK);
    }

}
