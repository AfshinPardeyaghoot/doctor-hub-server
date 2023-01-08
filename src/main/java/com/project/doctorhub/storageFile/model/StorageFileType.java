package com.project.doctorhub.storageFile.model;

public enum StorageFileType {
    PROFILE_IMAGE("/profile_image"), CATEGORY_IMAGE("/category/image"), CHAT_MESSAGE_FILE("/chat/message/file");

    private final String path;

    StorageFileType(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
