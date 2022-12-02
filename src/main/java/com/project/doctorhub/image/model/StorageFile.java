package com.project.doctorhub.image.model;

import com.project.doctorhub.base.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "storage_file")
public class StorageFile
        extends BaseEntity<Long> {

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private StorageFileType type;

    @Column(name = "storage_path")
    private String storagePath;

}
