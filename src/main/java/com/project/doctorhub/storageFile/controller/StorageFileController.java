package com.project.doctorhub.storageFile.controller;

import com.project.doctorhub.storageFile.model.StorageFile;
import com.project.doctorhub.storageFile.service.StorageFileService;
import com.project.doctorhub.util.StorageFileUtil;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/file")
public class StorageFileController {


    private final StorageFileUtil storageFileUtil;
    private final StorageFileService storageFileService;


    @GetMapping("/download/{uuid}")
    public ResponseEntity<?> downloadStorageFile(
            @PathVariable String uuid
    ) {

        StorageFile storageFile = storageFileService.findByUUIDNotDeleted(uuid);
        Resource resource = storageFileUtil.loadResource(storageFile.getStoragePath());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
