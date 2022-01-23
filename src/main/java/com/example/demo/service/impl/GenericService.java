package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestAlertException;
import com.example.demo.repository.GenericRepository;
import com.example.demo.service.mapper.EntityMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

public abstract class GenericService<E, D> {

    protected GenericRepository<E> repository;

    protected EntityMapper<D, E> mapper;

    public GenericService(GenericRepository<E> repository, EntityMapper<D, E> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public GenericService() {
    }

    @Transactional(readOnly = true)
    public List<D> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<D> findAll(Pageable pageable, Specification<E> specification) {
        return repository.findAll(specification, pageable)
                .map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public D findById(Long id) {
        return mapper.toDto(repository.findById(id)
                .orElseThrow(() -> new BadRequestAlertException("It doesn't exists", "service", "wrong.id")));
    }

    @Transactional
    public void save(D dto) {
        repository.save(mapper.toEntity(dto));
    }

    @Transactional
    public D update(D dto, Long id) {
        E entity = repository.findById(id)
                .orElseThrow(() -> new BadRequestAlertException("It doesn't exists", "service", "wrong.id"));
        mapper.partialUpdate(entity, dto);
        return mapper.toDto(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
