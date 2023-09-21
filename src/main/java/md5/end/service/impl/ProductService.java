package md5.end.service.impl;

import md5.end.exception.BadRequestException;
import md5.end.exception.NotFoundException;
import md5.end.model.dto.request.ProductRequest;

import md5.end.model.dto.response.ProductResponse;
import md5.end.model.dto.response.ProductDetailResponse;
import md5.end.model.dto.response.ProductShopResponse;
import md5.end.model.entity.product.Product;
import md5.end.model.entity.product.ProductImage;
import md5.end.repository.IProductRepository;
import md5.end.service.IProductService;
import md5.end.service.amapper.ProductMapper;
import md5.end.service.utils.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService {
    @Autowired
    private FileService fileService;
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;
    @Override
    public List<ProductResponse> findAll() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> productMapper.getResponseFromEntity(product))
                .collect(Collectors.toList());
    }
    @Override
    public List<ProductDetailResponse> findAllForUser()  {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> productMapper.getDetailResponseFromEntity(product))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductShopResponse> findShoppingForUser() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> productMapper.getShoppingResponseFromEntity(product))
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse findById(Long id) throws NotFoundException {
        Optional<Product> productOptional = productRepository.findById(id);
        if(!productOptional.isPresent()){
            throw new NotFoundException("Product's id "+id+" not found.");
        }
        return productMapper.getResponseFromEntity(productOptional.get());
    }

    @Override
    public ProductDetailResponse findForUser(Long id) throws NotFoundException {
        Optional<Product> productOptional = productRepository.findById(id);
        if(!productOptional.isPresent()){
            throw new NotFoundException("Product's id "+id+" not found.");
        }
        return productMapper.getDetailResponseFromEntity(productOptional.get());
    }


    @Override
    public ProductResponse findByName(String name) throws NotFoundException {
        Optional<Product> productOptional = productRepository.findByName(name);
        if(!productOptional.isPresent()){
            throw new NotFoundException("Product's name "+name+" not found.");
        }
        return productMapper.getResponseFromEntity(productOptional.get());
    }
    @Override
    public ProductResponse save(ProductRequest productRequest) throws BadRequestException, NotFoundException {
        Optional<Product> productOptional = productRepository.findByName(productRequest.getName());
        if(productOptional.isPresent()){
            throw new BadRequestException("Product's name: " + productRequest.getName() + " is already existed");
        }

        Product product = productRepository.save(productMapper.getEntityFromRequest(productRequest));
        return productMapper.getResponseFromEntity(product);
    }

    @Override
    public ProductResponse update(ProductRequest productRequest, Long id) throws NotFoundException {
        Optional<Product> productOptional = productRepository.findById(id);
        if (!productOptional.isPresent()) {
            throw new NotFoundException("Product's id "+id+" not found.");
        }

        Product product = productMapper.getEntityFromRequest(productRequest);
        product.setId(id);
        return productMapper.getResponseFromEntity(productRepository.save(product));
    }

    @Override
    public ProductResponse deleteById(Long id) throws NotFoundException {
        Optional<Product> productOptional = productRepository.findById(id);
        if (!productOptional.isPresent()) {
            throw new NotFoundException("Product's id "+id+" not found.");
        }
            productRepository.deleteById(id);
            return productMapper.getResponseFromEntity(productOptional.get());
    }



    public ProductResponse insertImage(Long productId, List<MultipartFile> imageFiles) throws NotFoundException {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (!productOptional.isPresent()) {
            throw new NotFoundException("Product's id "+productId+" not found.");
        }
            Product product = productOptional.get();
            product.setId(productId);
            for (MultipartFile imageFile : imageFiles) {
                String imageUrl = fileService.uploadFile(imageFile);
                ProductImage productImage = new ProductImage();
                productImage.setUrl(imageUrl);
                productImage.setProduct(product);
                product.getImages().add(productImage);
            }
            return productMapper.getResponseFromEntity(productRepository.save(product));
        }

}
