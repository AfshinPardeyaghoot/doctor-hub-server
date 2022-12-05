package com.project.doctorhub.speciality.model;

import com.project.doctorhub.base.model.BaseEntity;
import com.project.doctorhub.storageFile.model.StorageFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "speciality")
public class Speciality
        extends BaseEntity<Long> {

    @Column(name = "name", nullable = false)
    private String name;


    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "full_title")
    private String fullTitle;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "image_storage_file_id")
    private StorageFile image;


}
