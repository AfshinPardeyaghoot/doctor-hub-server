package com.project.doctorhub.base.service;



import com.project.doctorhub.base.exception.NotFoundException;
import com.project.doctorhub.base.model.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

public interface CrudService<ENTITY extends BaseEntity<PK>, PK extends Serializable> {

    Page<ENTITY> findAll(Pageable pageable);

    List<ENTITY> findAll();

    Page<ENTITY> findAllNotDeleted(Pageable pageable);

    List<ENTITY> findAllNotDeleted();

    ENTITY save(ENTITY object);

    void delete(ENTITY object);

    void deleteById(PK id);

    Long countAll();

    ENTITY getById(PK id) throws NotFoundException;

    void safeDelete(ENTITY object);

    void safeDeleteById(PK id);

}
