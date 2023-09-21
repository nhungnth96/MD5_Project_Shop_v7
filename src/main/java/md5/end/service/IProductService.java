package md5.end.service;

import md5.end.exception.NotFoundException;
import md5.end.model.dto.request.ProductRequest;


import md5.end.model.dto.response.ProductResponse;
import md5.end.model.dto.response.ProductDetailResponse;
import md5.end.model.dto.response.ProductShopResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductService extends IGenericService<ProductRequest, ProductResponse> {
    ProductResponse findByName(String name) throws NotFoundException;
    List<ProductDetailResponse> findAllForUser();
    List<ProductShopResponse> findShoppingForUser();
    ProductDetailResponse findForUser(Long id) throws NotFoundException;
    ProductResponse insertImage(Long productId, List<MultipartFile> imageFiles) throws NotFoundException;
}
