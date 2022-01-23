package com.example.demo.controller;

import com.example.demo.service.impl.GenericService;
import com.example.demo.util.PaginationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URISyntaxException;
import java.util.List;

@SuppressWarnings({"unchecked", "rawtypes"})
@ResponseBody
public abstract class GenericController<E, D> {

    private final GenericService<E, D> service;

    public GenericController(GenericService<E, D> service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<D> getOne(@PathVariable Long id) {
        D dto = service.findById(id);
        return ResponseEntity.ok()
                .body(dto);
    }

    @GetMapping()
    public ResponseEntity<List<D>> getAll(Pageable pageable, Specification<E> specification) {
        Page<D> page = service.findAll(pageable, specification);
        HttpHeaders headers = PaginationUtils.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok()
                .headers(headers)
                .body(page.getContent());
    }

    @GetMapping("/all")
    public ResponseEntity<List<D>> getAll() {
        List<D> result = service.findAll();
        return ResponseEntity.ok()
                .body(result);
    }

    @PostMapping
    ResponseEntity<Void> save(@RequestBody D dto) throws URISyntaxException {
        service.save(dto);
        return ResponseEntity.noContent()
                .build();
    }

    @PutMapping("/{id}")
    ResponseEntity<D> update(@PathVariable Long id, @RequestBody D dto) {
        D d =service.update(dto, id);
        return ResponseEntity.ok()
                .body(d);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent()
                .build();
    }
}
