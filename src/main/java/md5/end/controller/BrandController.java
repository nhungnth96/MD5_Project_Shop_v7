package md5.end.controller;

import md5.end.exception.BadRequestException;
import md5.end.exception.NotFoundException;
import md5.end.model.dto.request.BrandRequest;
import md5.end.model.dto.response.BrandResponse;
import md5.end.service.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/brands")
public class BrandController {
    @Autowired
    private IBrandService brandService;

    @PostMapping("")
    public ResponseEntity<BrandResponse> add(
            @Valid
            @RequestBody BrandRequest brandRequest) throws BadRequestException, NotFoundException {
        return new ResponseEntity<>(brandService.save(brandRequest), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<BrandResponse>> getAll() {
        return new ResponseEntity<>(brandService.findAll(), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandResponse> getOne(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(brandService.findById(id), HttpStatus.OK);

    }

    @PutMapping("/{id}")
    public ResponseEntity<BrandResponse> edit(
            @Valid
            @RequestBody BrandRequest brandRequest,
            @PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(brandService.update(brandRequest, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BrandResponse> delete(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(brandService.deleteById(id), HttpStatus.OK);

    }


}
