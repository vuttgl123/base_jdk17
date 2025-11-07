package com.base.service.impl;

import com.base.model.entity.BaseEntity;
import com.base.model.mapper.BaseMapper;
import com.base.repository.BaseRepository;
import com.base.service.inter.BaseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Transactional(readOnly = true)
public abstract class BaseServiceImpl<T extends BaseEntity, RQ, RS>
        implements BaseService<T, RQ, RS> {


    protected final BaseRepository<T> repository;
    protected final BaseMapper<T, RQ, RS> mapper;


    @Transactional
    @Override
    public RS create(RQ request) {
        T entity = mapper.toEntity(request);
        T saved = repository.save(entity);
        return mapper.toResponse(saved);
    }


    @Override
    public java.util.Optional<RS> getById(Long id) {
        return repository.findById(id)
                .filter(e -> !e.isDeleted())
                .map(mapper::toResponse);
    }


    @Override
    public Page<RS> getPage(Pageable pageable, Specification<T> spec) {
        return repository.findAll(spec, pageable).map(mapper::toResponse);
    }


    @Transactional
    @Override
    public RS update(Long id, RQ request) {
        T entity = repository.findById(id)
                .filter(e -> !e.isDeleted())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bản ghi id=" + id));
        mapper.updateEntity(entity, request);
        T saved = repository.save(entity);
        return mapper.toResponse(saved);
    }


    @Transactional
    @Override
    public void softDelete(Long id) {
        T entity = repository.findById(id)
                .filter(e -> !e.isDeleted())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bản ghi id=" + id));
        entity.setDeleted(true);
        repository.save(entity);
    }


    @Transactional
    @Override
    public void restore(Long id) {
        T entity = repository.findById(id)
                .filter(e -> e.isDeleted())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bản ghi đã xoá id=" + id));
        entity.setDeleted(false);
        repository.save(entity);
    }
}
