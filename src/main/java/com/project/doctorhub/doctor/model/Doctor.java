package com.project.doctorhub.doctor.model;

import com.project.doctorhub.base.model.BaseEntity;
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
public class Doctor extends BaseEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "description")
    private String description;

    @Column(name = "gmc_number")
    private String gmcNumber;

    @ManyToOne
    @JoinColumn(name = "profile_image_storage_file_id")
    private StorageFile profileImage;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    private Set<DoctorSpeciality> doctorSpecialities = new HashSet<>();

}
