package md5.end.service;

import md5.end.exception.NotFoundException;
import md5.end.model.dto.request.BrandRequest;
import md5.end.model.dto.request.SpecRequest;
import md5.end.model.dto.response.BrandResponse;
import md5.end.model.dto.response.SpecResponse;

public interface ISpecService extends IGenericService<SpecRequest, SpecResponse>{
    SpecResponse findByName(String name) throws NotFoundException;
}
