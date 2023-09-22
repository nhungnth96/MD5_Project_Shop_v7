package md5.end.service.impl;

import md5.end.exception.BadRequestException;
import md5.end.exception.NotFoundException;
import md5.end.model.dto.request.ProductRequest;

import md5.end.model.dto.response.ProductResponse;
import md5.end.model.dto.response.ProductDetailResponse;
import md5.end.model.dto.response.ProductShopResponse;
import md5.end.model.dto.response.UserResponse;
import md5.end.model.entity.order.OrderStatus;
import md5.end.model.entity.product.Product;
import md5.end.model.entity.product.ProductImage;
import md5.end.model.entity.product.ProductSpec;
import md5.end.model.entity.product.Specification;
import md5.end.model.entity.user.User;
import md5.end.repository.IBrandRepository;
import md5.end.repository.ICategoryRepository;
import md5.end.repository.IProductRepository;
import md5.end.repository.ISpecRepository;
import md5.end.service.IProductService;
import md5.end.service.amapper.ProductMapper;
import md5.end.service.utils.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    @Autowired
    private ICategoryRepository categoryRepository;
    @Autowired
    private IBrandRepository brandRepository;
    @Autowired
    private ISpecRepository specRepository;
    @Override
    public List<ProductResponse> findAll() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> productMapper.getResponseFromEntity(product))
                .collect(Collectors.toList());
    }

    // anymous
    @Override
    public Page<ProductShopResponse> findShoppingForUser(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Product> productPage = productRepository.findAll(pageable);
        Page<ProductShopResponse> productResponsePage = productPage.map(product -> productMapper.getShoppingResponseFromEntity(product));
        return productResponsePage;
    }
    // admin only
    @Override
    public Page<ProductResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Product> productPage = productRepository.findAll(pageable);
        Page<ProductResponse> productResponsePage = productPage.map(product -> productMapper.getResponseFromEntity(product));
        return productResponsePage;
    }

    @Override
    public Page<ProductShopResponse> findByName(String name, int page, int size) throws NotFoundException {
        Pageable pageable = PageRequest.of(page,size);
        Page<Product> productPage = productRepository.findByNameContainingIgnoreCase(name,pageable);
        Page<ProductShopResponse> productShopResponsePage = productPage.map(product -> productMapper.getShoppingResponseFromEntity(product));
        if(productShopResponsePage.isEmpty()){
            throw new NotFoundException("No result found.");
        }
        return productShopResponsePage;
    }

    @Override
    public Page<ProductShopResponse> findByStatus(int status, int page, int size) throws NotFoundException {
        Pageable pageable = PageRequest.of(page,size);
        Page<Product> productPage = productRepository.findByStatus(status,pageable);
        Page<ProductShopResponse> productShopResponsePage = productPage.map(product -> productMapper.getShoppingResponseFromEntity(product));
        if(productShopResponsePage.isEmpty()){
            throw new NotFoundException("No result found.");
        }
        return productShopResponsePage;
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
        Product OldDataProduct = productOptional.get();

        Product NewDataProduct = Product.builder()
                .id(id)
                .rating(OldDataProduct.getRating())
                .createdAt(OldDataProduct.getCreatedAt())
                .updatedAt(LocalDateTime.now().toString())
                .images(OldDataProduct.getImages())
                .name(productRequest.getName() == null ? OldDataProduct.getName() : productRequest.getName())
                .description(productRequest.getDescription() == null ? OldDataProduct.getDescription() : productRequest.getDescription())
                .importPrice(productRequest.getImportPrice() == null ? OldDataProduct.getImportPrice() : productRequest.getImportPrice())
                .exportPrice(productRequest.getExportPrice() == null ? OldDataProduct.getExportPrice() : productRequest.getExportPrice())
                .stock(productRequest.getStock() == null ? OldDataProduct.getStock() : productRequest.getStock())
                .status(productRequest.getStatus() == null ? OldDataProduct.getStatus() : productRequest.getStatus())
                .brand(productRequest.getBrandId() == null ? OldDataProduct.getBrand() : brandRepository.findById(productRequest.getBrandId()).get())
                .category(productRequest.getCategoryId() == null ? OldDataProduct.getCategory() : categoryRepository.findById(productRequest.getCategoryId()).get())
                .build();

        List<ProductSpec> specs = new ArrayList<>();
        if (productRequest.getSpecs() != null && !productRequest.getSpecs().isEmpty()) {
            for (Map.Entry<Long, String> entry : productRequest.getSpecs().entrySet()) {
                ProductSpec productSpec = new ProductSpec();
                productSpec.setProduct(NewDataProduct);
                Specification specification = specRepository.findById(entry.getKey()).orElse(null);
                productSpec.setSpecification(specification);
                productSpec.setValue(entry.getValue());
                specs.add(productSpec);
            }
        }
        NewDataProduct.setSpecifications(productRequest.getSpecs() != null && !productRequest.getSpecs().isEmpty() ? specs : OldDataProduct.getSpecifications());
        return productMapper.getResponseFromEntity(productRepository.save(NewDataProduct));
    }

    @Override
    public ProductResponse deleteById(Long id) throws NotFoundException {
        Optional<Product> productOptional = productRepository.findById(id);
        if (!productOptional.isPresent()) {
            throw new NotFoundException("Product's id "+id+" not found.");
        }
            productOptional.get().setStatus(3);
            productRepository.save(productOptional.get());
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
