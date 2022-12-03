package com.project.doctorhub.image.service;

import com.project.doctorhub.base.service.AbstractCrudService;
import com.project.doctorhub.image.model.StorageFile;
import com.project.doctorhub.image.model.StorageFileType;
import com.project.doctorhub.image.reporitory.StorageFileRepository;
import com.project.doctorhub.util.StorageFileUtil;
import org.hibernate.type.ImageType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

@Service

public class StorageFileService
        extends AbstractCrudService<StorageFile, Long, StorageFileRepository> {

    private final StorageFileUtil storageFileUtil;

    public StorageFileService(
            StorageFileRepository abstractRepository,
            StorageFileUtil storageFileUtil
    ) {
        super(abstractRepository);
        this.storageFileUtil = storageFileUtil;
    }

    @Transactional
    public StorageFile create(MultipartFile file, StorageFileType type) {
        String path = storageFileUtil.store(file, type.getPath(), file.getOriginalFilename());

        StorageFile storageFile = new StorageFile();
        storageFile.setStoragePath(path);
        storageFile.setType(type);
        storageFile.setIsDeleted(false);
        return save(storageFile);
    }


}
