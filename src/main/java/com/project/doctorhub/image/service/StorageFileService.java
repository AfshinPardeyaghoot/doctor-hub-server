package com.project.doctorhub.image.service;

import com.project.doctorhub.base.service.AbstractCrudService;
import com.project.doctorhub.image.model.StorageFile;
import com.project.doctorhub.image.reporitory.StorageFileRepository;
import org.springframework.stereotype.Service;

@Service
public class StorageFileService
        extends AbstractCrudService<StorageFile, Long, StorageFileRepository> {

    public StorageFileService(StorageFileRepository abstractRepository) {
        super(abstractRepository);
    }
}
