package md5.end.service;

import md5.end.exception.NotFoundException;
import md5.end.model.dto.request.BrandRequest;
import md5.end.model.dto.response.BrandResponse;

import java.util.Optional;

public interface IBrandService extends IGenericService<BrandRequest, BrandResponse>{
    BrandResponse findByName(String name) throws NotFoundException;
}
