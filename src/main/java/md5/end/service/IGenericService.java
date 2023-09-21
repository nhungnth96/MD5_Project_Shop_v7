package md5.end.service;

import md5.end.exception.BadRequestException;
import md5.end.exception.NotFoundException;

import java.util.List;

public interface IGenericService<K,V> {
    // K request, V response
    List<V> findAll();
    V findById (Long id) throws NotFoundException;
    V save (K k) throws BadRequestException, NotFoundException;
    V update (K k,Long id) throws NotFoundException;
    V deleteById (Long id) throws NotFoundException;
}
