package com.project.doctorhub.consultation.model;

import com.project.doctorhub.base.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "consultation_type")
public class ConsultationType extends BaseEntity<Long> {

    private String name;
    private String title;

}
