package com.project.doctorhub.speciality.model;

import com.project.doctorhub.base.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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

}
