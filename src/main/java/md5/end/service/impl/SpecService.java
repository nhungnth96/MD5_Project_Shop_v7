package md5.end.service.impl;

import md5.end.exception.BadRequestException;
import md5.end.exception.NotFoundException;
import md5.end.model.dto.request.BrandRequest;
import md5.end.model.dto.request.SpecRequest;
import md5.end.model.dto.response.BrandResponse;
import md5.end.model.dto.response.SpecResponse;
import md5.end.model.entity.product.Brand;
import md5.end.model.entity.product.Specification;

import md5.end.repository.ISpecRepository;
import md5.end.service.IBrandService;
import md5.end.service.ISpecService;

import md5.end.service.amapper.SpecMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SpecService implements ISpecService {
    @Autowired
    private ISpecRepository specRepository;
    @Autowired
    private SpecMapper specMapper;


    @Override
    public List<SpecResponse> findAll() {
        List<Specification> specifications = specRepository.findAll();
        return specifications.stream()
                .map(specification -> specMapper.getResponseFromEntity(specification))
                .collect(Collectors.toList());
    }

    @Override
    public SpecResponse findById(Long id) throws NotFoundException {
        Optional<Specification> specificationOptional = specRepository.findById(id);
        if(!specificationOptional.isPresent()){
            throw new NotFoundException("Spec's id "+id+" not found.");

        }
        return specMapper.getResponseFromEntity(specificationOptional.get());

    }
    @Override
    public SpecResponse findByName(String name) throws NotFoundException {
        Optional<Specification> specificationOptional = specRepository.findByName(name);
        if(!specificationOptional.isPresent()){
            throw new NotFoundException("Spec's name "+name+" not found.");
        }

        return specMapper.getResponseFromEntity(specificationOptional.get());
    }
    @Override
    public SpecResponse save(SpecRequest specRequest) throws BadRequestException {
        Optional<Specification> specificationOptional = specRepository.findByName(specRequest.getName());
        if(specificationOptional.isPresent()){
            throw new BadRequestException("Specification's name: " + specRequest.getName() + " is already existed");
        }
        Specification specification = specRepository.save(specMapper.getEntityFromRequest(specRequest));
        return specMapper.getResponseFromEntity(specification);
    }

    @Override
    public SpecResponse update(SpecRequest specRequest, Long id) throws NotFoundException {
        Optional<Specification> specificationOptional = specRepository.findById(id);
        if(!specificationOptional.isPresent()){
            throw new NotFoundException("Spec's id "+id+" not found.");

        }
        Specification specification = specMapper.getEntityFromRequest(specRequest);
        specification.setId(id);
        return specMapper.getResponseFromEntity(specRepository.save(specification));
    }

    @Override
    public SpecResponse deleteById(Long id) throws NotFoundException {
        Optional<Specification> specificationOptional = specRepository.findById(id);
        if(!specificationOptional.isPresent()){
            throw new NotFoundException("Spec's id "+id+" not found.");
        }
        specRepository.deleteById(id);
        return specMapper.getResponseFromEntity(specificationOptional.get());
    }
}
