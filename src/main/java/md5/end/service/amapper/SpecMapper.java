package md5.end.service.amapper;

import md5.end.model.dto.request.BrandRequest;
import md5.end.model.dto.request.SpecRequest;
import md5.end.model.dto.response.BrandResponse;
import md5.end.model.dto.response.SpecResponse;
import md5.end.model.entity.product.Brand;
import md5.end.model.entity.product.Product;
import md5.end.model.entity.product.Specification;
import org.springframework.stereotype.Component;



@Component
public class SpecMapper implements IGenericMapper<Specification, SpecRequest, SpecResponse> {
    @Override
    public Specification getEntityFromRequest(SpecRequest specRequest) {

        return Specification.builder()
                .name(specRequest.getName())
                .build();
    }

    @Override
    public SpecResponse getResponseFromEntity(Specification specification) {
        return SpecResponse.builder()
                .id(specification.getId())
                .name(specification.getName())
                .build();
    }
}
