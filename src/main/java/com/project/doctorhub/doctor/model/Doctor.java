package com.project.doctorhub.doctor.model;

import com.project.doctorhub.base.model.BaseEntity;
import com.project.doctorhub.speciality.model.Speciality;
import com.project.doctorhub.storageFile.model.StorageFile;
import com.project.doctorhub.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "doctor")
@NamedEntityGraph(
        name = "doctor_all_joins",
        attributeNodes = {
                @NamedAttributeNode("profileImage"),
                @NamedAttributeNode("speciality"),
                @NamedAttributeNode(value = "doctorConsultationTypes", subgraph = "consultationType")
        },
        subgraphs = {
                @NamedSubgraph(name = "consultationType", attributeNodes = {
                        @NamedAttributeNode("consultationType")
                })
        }
)
public class Doctor extends BaseEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "gmc_number")
    private String gmcNumber;

    @ManyToOne
    @JoinColumn(name = "profile_image_storage_file_id")
    private StorageFile profileImage;

    @ManyToOne
    @JoinColumn(name = "speciality_id")
    private Speciality speciality;

    @OneToMany(mappedBy = "doctor")
    private Set<DoctorConsultationType> doctorConsultationTypes = new HashSet<>();

}
