package md5.end.service;

import md5.end.exception.NotFoundException;
import md5.end.model.dto.request.ProductRequest;


import md5.end.model.dto.response.ProductResponse;
import md5.end.model.dto.response.ProductDetailResponse;
import md5.end.model.dto.response.ProductShopResponse;
import md5.end.model.dto.response.UserResponse;
import md5.end.model.entity.order.OrderStatus;
import md5.end.model.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductService extends IGenericService<ProductRequest, ProductResponse> {
    Page<ProductResponse> findAll(int page, int size);
    Page<ProductShopResponse> findByName(String name,int page,int size) throws NotFoundException;
    Page<ProductShopResponse> findByStatus(int status, int page, int size)throws NotFoundException;
    Page<ProductShopResponse> findShoppingForUser(int page, int size);

    ProductResponse findByName(String name) throws NotFoundException;

    ProductDetailResponse findForUser(Long id) throws NotFoundException;
    ProductResponse insertImage(Long productId, List<MultipartFile> imageFiles) throws NotFoundException;
}
