package com.project.doctorhub.speciality.model;

import com.project.doctorhub.base.model.BaseEntity;
import com.project.doctorhub.category.model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpecialityCategory extends BaseEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "speciality_id", nullable = false)
    private Speciality speciality;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

}
