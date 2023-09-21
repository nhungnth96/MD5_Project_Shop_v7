package md5.end.controller;

import md5.end.exception.BadRequestException;
import md5.end.exception.NotFoundException;
import md5.end.model.dto.request.BrandRequest;
import md5.end.model.dto.request.SpecRequest;
import md5.end.model.dto.response.BrandResponse;
import md5.end.model.dto.response.SpecResponse;

import md5.end.service.ISpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/specifications")
public class SpecController {
    @Autowired
    private ISpecService specService;

    @PostMapping("")
    public ResponseEntity<SpecResponse> add(
            @Valid
            @RequestBody SpecRequest specRequest) throws BadRequestException, NotFoundException {
        return new ResponseEntity<>(specService.save(specRequest), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<SpecResponse>> getAll() {
        return new ResponseEntity<>(specService.findAll(), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<SpecResponse> getOne(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(specService.findById(id), HttpStatus.OK);

    }

    @PutMapping("/{id}")
    public ResponseEntity<SpecResponse> edit(
            @Valid
            @RequestBody SpecRequest specRequest,
            @PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(specService.update(specRequest, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SpecResponse> delete(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(specService.deleteById(id), HttpStatus.OK);

    }


}
