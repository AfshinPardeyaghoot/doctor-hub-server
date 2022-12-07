package com.project.doctorhub.storageFile.service;

import com.project.doctorhub.base.service.AbstractCrudService;
import com.project.doctorhub.storageFile.model.StorageFile;
import com.project.doctorhub.storageFile.model.StorageFileType;
import com.project.doctorhub.storageFile.reporitory.StorageFileRepository;
import com.project.doctorhub.util.StorageFileUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.InputStream;

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
        String path = storageFileUtil.store(file, type);
        StorageFile storageFile = new StorageFile();
        storageFile.setStoragePath(path);
        storageFile.setType(type);
        storageFile.setIsDeleted(false);
        return save(storageFile);
    }

    public StorageFile create(InputStream inputStream, StorageFileType type, String name){
        String path = storageFileUtil.store(inputStream, type, name);
        StorageFile storageFile = new StorageFile();
        storageFile.setStoragePath(path);
        storageFile.setType(type);
        storageFile.setIsDeleted(false);
        return save(storageFile);
    }




}
