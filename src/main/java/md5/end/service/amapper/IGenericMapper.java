package md5.end.service.amapper;

import md5.end.exception.BadRequestException;
import md5.end.exception.NotFoundException;

public interface IGenericMapper<T,K,V> {
    // T entity, K request, V response
    T getEntityFromRequest(K k) throws NotFoundException, BadRequestException;
    V getResponseFromEntity(T t);
}