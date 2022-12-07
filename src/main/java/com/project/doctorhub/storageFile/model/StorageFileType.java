package com.project.doctorhub.storageFile.model;

public enum StorageFileType {
    PROFILE_IMAGE("/profile_image"), CATEGORY_IMAGE("/category/image");

    private final String path;

    StorageFileType(String path) {
        this.path = path;
    }

    public String getPath(){
        return path;
    }
}
