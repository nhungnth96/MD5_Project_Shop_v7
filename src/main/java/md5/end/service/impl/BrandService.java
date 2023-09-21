package md5.end.service.impl;

import md5.end.exception.BadRequestException;
import md5.end.exception.NotFoundException;
import md5.end.model.dto.request.BrandRequest;
import md5.end.model.dto.response.BrandResponse;
import md5.end.model.entity.product.Brand;
import md5.end.model.entity.product.Product;
import md5.end.repository.IBrandRepository;
import md5.end.service.IBrandService;
import md5.end.service.amapper.BrandMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BrandService implements IBrandService {
    @Autowired
    private IBrandRepository brandRepository;
    @Autowired
    private BrandMapper brandMapper;


    @Override
    public List<BrandResponse> findAll() {
        List<Brand> brands = brandRepository.findAll();
        return brands.stream()
                .map(brand -> brandMapper.getResponseFromEntity(brand))
                .collect(Collectors.toList());
    }

    @Override
    public BrandResponse findById(Long id) throws NotFoundException {
        Optional<Brand> brandOptional = brandRepository.findById(id);
        if(!brandOptional.isPresent()){
            throw new NotFoundException("Brand's id "+id+" not found.");

        }
        return brandMapper.getResponseFromEntity(brandOptional.get());

    }
    @Override
    public BrandResponse findByName(String name) throws NotFoundException {
        Optional<Brand> brandOptional = brandRepository.findByName(name);
        if(!brandOptional.isPresent()){
            throw new NotFoundException("Brand's name "+name+" not found.");

        }
        return brandMapper.getResponseFromEntity(brandOptional.get());

    }
    @Override
    public BrandResponse save(BrandRequest brandRequest) throws BadRequestException {
        Optional<Brand> brandOptional = brandRepository.findByName(brandRequest.getName());
        if(brandOptional.isPresent()){
            throw new BadRequestException("Brand's name: " + brandRequest.getName() + " is already existed");
        }
        Brand brand = brandRepository.save(brandMapper.getEntityFromRequest(brandRequest));
        return brandMapper.getResponseFromEntity(brand);
    }

    @Override
    public BrandResponse update(BrandRequest brandRequest, Long id) throws NotFoundException {
        Optional<Brand> brandOptional = brandRepository.findById(id);
        if (!brandOptional.isPresent()) {
            throw new NotFoundException("Brand's id "+id+" not found.");
        }
        Brand brand = brandMapper.getEntityFromRequest(brandRequest);
        brand.setId(id);
        return brandMapper.getResponseFromEntity(brandRepository.save(brand));
    }

    @Override
    public BrandResponse deleteById(Long id) throws NotFoundException {
        Optional<Brand> brandOptional = brandRepository.findById(id);
        if(!brandOptional.isPresent()){
            throw new NotFoundException("Brand's id "+id+" not found.");
        }
        brandRepository.deleteById(id);
        return brandMapper.getResponseFromEntity(brandOptional.get());
    }
}
