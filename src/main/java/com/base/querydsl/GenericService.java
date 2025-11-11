package com.base.querydsl;

import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public abstract class GenericService<E extends AbstractBaseEntity, CREATE_REQ, UPDATE_REQ, RES> {

    protected final GenericRepository<E> repository;
    protected final GenericMapper<E, CREATE_REQ, UPDATE_REQ, RES> mapper;
    protected final DynamicQueryBuilder<E> queryBuilder;

    @Transactional
    public RES create(CREATE_REQ request) {
        log.info("Creating new entity");
        E entity = mapper.toEntity(request);
        beforeCreate(entity, request);
        E saved = repository.save(entity);
        afterCreate(saved, request);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public Optional<RES> findById(Long id) {
        log.info("Finding entity by id: {}", id);
        return repository.findById(id).map(mapper::toResponse);
    }

    @Transactional
    public RES update(Long id, UPDATE_REQ request) {
        log.info("Updating entity id: {}", id);
        E entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entity not found: " + id));

        beforeUpdate(entity, request);
        mapper.updateEntity(request, entity);
        E updated = repository.save(entity);
        afterUpdate(updated, request);
        return mapper.toResponse(updated);
    }

    @Transactional
    public void softDelete(Long id) {
        log.info("Soft deleting entity id: {}", id);
        E entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entity not found: " + id));
        entity.markAsDeleted();
        repository.save(entity);
    }

    @Transactional
    public void hardDelete(Long id) {
        log.info("Hard deleting entity id: {}", id);
        repository.deleteById(id);
    }

    @Transactional
    public RES restore(Long id) {
        log.info("Restoring entity id: {}", id);
        E entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entity not found: " + id));
        entity.restoreEntity();
        E restored = repository.save(entity);
        return mapper.toResponse(restored);
    }

    @Transactional(readOnly = true)
    public PageResponse<RES> search(SearchRequest searchRequest) {
        log.info("Searching entities with request: {}", searchRequest);

        Predicate predicate = queryBuilder.buildPredicate(searchRequest);
        Pageable pageable = buildPageable(searchRequest);

        Page<E> entityPage = repository.findAll(predicate, pageable);
        List<RES> responses = mapper.toResponseList(entityPage.getContent());

        return PageResponse.<RES>builder()
                .content(responses)
                .page(entityPage.getNumber())
                .size(entityPage.getSize())
                .totalElements(entityPage.getTotalElements())
                .totalPages(entityPage.getTotalPages())
                .hasNext(entityPage.hasNext())
                .hasPrevious(entityPage.hasPrevious())
                .build();
    }

    @Transactional(readOnly = true)
    public List<RES> findAll(SearchRequest searchRequest) {
        Predicate predicate = queryBuilder.buildPredicate(searchRequest);
        Sort sort = buildSort(searchRequest);

        List<E> entities = (List<E>) repository.findAll(predicate, sort);
        return mapper.toResponseList(entities);
    }

    @Transactional(readOnly = true)
    public long count(SearchRequest searchRequest) {
        Predicate predicate = queryBuilder.buildPredicate(searchRequest);
        return repository.count(predicate);
    }

    private Pageable buildPageable(SearchRequest request) {
        Sort sort = buildSort(request);
        return PageRequest.of(request.getPage(), request.getSize(), sort);
    }

    private Sort buildSort(SearchRequest request) {
        if (request.getSortBy() != null && !request.getSortBy().isEmpty()) {
            Sort.Direction direction = "ASC".equalsIgnoreCase(request.getSortDirection())
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC;
            return Sort.by(direction, request.getSortBy());
        }
        return Sort.by(Sort.Direction.DESC, "id");
    }

    // Hook methods for subclasses
    protected void beforeCreate(E entity, CREATE_REQ request) {}
    protected void afterCreate(E entity, CREATE_REQ request) {}
    protected void beforeUpdate(E entity, UPDATE_REQ request) {}
    protected void afterUpdate(E entity, UPDATE_REQ request) {}
}
