package com.project.doctorhub.base.service;


import com.project.doctorhub.base.exception.NotFoundException;
import com.project.doctorhub.base.model.BaseEntity;
import com.project.doctorhub.base.repository.AbstractRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;


public class AbstractCrudService<ENTITY extends BaseEntity<PK>,
        PK extends Serializable, REPOSITORY extends AbstractRepository<ENTITY, PK>> implements CrudService<ENTITY, PK> {

    private final REPOSITORY abstractRepository;
    protected Class<ENTITY> entityClass;

    public AbstractCrudService(REPOSITORY abstractRepository) {
        this.abstractRepository = abstractRepository;
        if (getClass().getGenericSuperclass() == getClass().getGenericSuperclass()) {
            ParameterizedType genericSuperClass = (ParameterizedType) getClass().getGenericSuperclass();
            if (genericSuperClass != null && genericSuperClass.getActualTypeArguments() != null
                    && genericSuperClass.getActualTypeArguments().length > 0) {
                if (genericSuperClass.getActualTypeArguments()[0] instanceof Class) {
                    entityClass = (Class<ENTITY>) genericSuperClass.getActualTypeArguments()[0];
                }
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ENTITY> findAll(Pageable pageable) {
        return abstractRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ENTITY> findAll() {
        return abstractRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ENTITY> findAllNotDeleted(Pageable pageable) {
        return abstractRepository.findAllByIsDeleted(false, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ENTITY> findAllNotDeleted() {
        return abstractRepository.findAllByIsDeleted(false);
    }

    @Override
    public ENTITY save(ENTITY object) {
        return abstractRepository.save(object);
    }


    @Override
    @Transactional(readOnly = true)
    public Long countAll() {
        return abstractRepository.count();
    }


    @Override
    @Transactional(readOnly = true)
    public ENTITY getById(PK id) {
        return abstractRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("object %d from %s not found", id, entityClass))
        );
    }

    @Override
    public void delete(ENTITY object) {
        abstractRepository.delete(object);
    }

    @Override
    public void deleteById(PK id) {
        ENTITY object = getById(id);
        abstractRepository.delete(object);
    }

    @Override
    public void safeDelete(ENTITY object) {
        object.setIsDeleted(true);
        save(object);
    }

    @Transactional(readOnly = true)
    public ENTITY findByUUID(String uuid) {
        return abstractRepository.findByUUID(uuid)
                .orElseThrow(() -> new NotFoundException("object not found!"));
    }

    @Transactional(readOnly = true)
    public ENTITY findByUUIDNotDeleted(String uuid) {
        return abstractRepository.findByUUIDAndIsDeletedFalse(uuid)
                .orElseThrow(() -> new NotFoundException("object not found!"));
    }

    @Override
    public void safeDeleteById(PK id) {
        ENTITY object = getById(id);
        object.setIsDeleted(true);
        save(object);
    }
}
