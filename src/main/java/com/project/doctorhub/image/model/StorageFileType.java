package com.project.doctorhub.image.model;

public enum StorageFileType {
    PROFILE_IMAGE("/profile_image"), SPECIALITY_IMAGE("/speciality/image");

    private final String path;

    StorageFileType(String path) {
        this.path = path;
    }

    public String getPath(){
        return path;
    }
}
