package com.project.doctorhub.util;

import com.project.doctorhub.base.config.ApplicationBaseProperties;
import com.project.doctorhub.base.exception.InternalServerException;
import com.project.doctorhub.base.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Random;

@Slf4j
@Component
@AllArgsConstructor
public class StorageFileUtil {

    private final ApplicationBaseProperties applicationBaseProperties;

    public String store(MultipartFile file, String uploadLocation, String name) {
        Path uploadPath = Paths.get(applicationBaseProperties.getStorageBasePath() + uploadLocation)
                .toAbsolutePath()
                .normalize();

        createDirIfNotExist(uploadPath);

        return copyFileToFileStorage(file, addRandomNumberToFileName(sanitizeFileName(name)), uploadPath).substring(applicationBaseProperties.getStorageBasePath().length());
    }

    public Resource loadResource(String filePath) {
        return getFileResource(String.format("%s%s", applicationBaseProperties.getStorageBasePath(), filePath));
    }

    private Resource getFileResource(String fileUri) {
        return new FileSystemResource(fileUri);
    }

    private void createDirIfNotExist(Path uploadPath) {
        if (!Files.exists(uploadPath)) {
            createDir(uploadPath);
        }
    }

    private void createDir(Path uploadPath) {
        try {
            Files.createDirectories(uploadPath);

        } catch (IOException e) {
            log.error("could not create the directory {}", e.getMessage());
            throw new InternalServerException();
        }
    }

    @NotNull
    private String copyFileToFileStorage(MultipartFile file, String name, Path uploadPath) {
        try {

            System.out.println("before target file : " + uploadPath);

            Path targetLocation = uploadPath.resolve(name);

            System.out.println("target file : " + targetLocation);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return uploadPath + "/" + name;

        } catch (IOException ex) {
            log.error("could not copy file in file system {}", ex.getMessage());
            throw new InternalServerException();
        }
    }

    private String addRandomNumberToFileName(String fileName) {
        Random random = new Random();
        String nameSpace = Integer.toString(random.nextInt(90000) + 100000);
        return nameSpace + "__" + fileName;
    }

    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z\\u0600-\\u06FF\\s\\d._-]", "_");
    }
}
