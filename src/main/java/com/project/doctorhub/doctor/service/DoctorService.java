package com.project.doctorhub.doctor.service;

import com.project.doctorhub.auth.model.Role;
import com.project.doctorhub.base.exception.HttpException;
import com.project.doctorhub.base.service.AbstractCrudService;
import com.project.doctorhub.doctor.dto.DoctorCreateDTO;
import com.project.doctorhub.doctor.dto.DoctorUpdateDTO;
import com.project.doctorhub.doctor.model.Doctor;
import com.project.doctorhub.doctor.repository.DoctorRepository;
import com.project.doctorhub.speciality.model.Speciality;
import com.project.doctorhub.speciality.service.SpecialityService;
import com.project.doctorhub.storageFile.model.StorageFile;
import com.project.doctorhub.storageFile.model.StorageFileType;
import com.project.doctorhub.storageFile.service.StorageFileService;
import com.project.doctorhub.user.model.User;
import com.project.doctorhub.user.service.UserService;
import com.project.doctorhub.util.RandomUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
public class DoctorService
        extends AbstractCrudService<Doctor, Long, DoctorRepository> {

    private final RandomUtil randomUtil;
    private final UserService userService;
    private final DoctorRepository doctorRepository;
    private final SpecialityService specialityService;
    private final StorageFileService storageFileService;

    public DoctorService(
            RandomUtil randomUtil,
            DoctorRepository abstractRepository,
            UserService userService,
            StorageFileService storageFileService,
            SpecialityService specialityService,
            DoctorRepository doctorRepository
    ) {
        super(abstractRepository);
        this.randomUtil = randomUtil;
        this.userService = userService;
        this.doctorRepository = doctorRepository;
        this.specialityService = specialityService;
        this.storageFileService = storageFileService;
    }


    @Transactional
    public Doctor create(DoctorCreateDTO doctorCreateDTO) {

        if (doctorRepository.findByPhone(doctorCreateDTO.getPhone()).isPresent()) {
            throw new HttpException("در حال حاضر پزشکی با شماره وارد شده وجود دارد!", HttpStatus.BAD_REQUEST);
        }

        User user = userService.createOrFetch(
                doctorCreateDTO.getPhone(),
                Role.DOCTOR
        );

        StorageFile profileImage = storageFileService.create(
                doctorCreateDTO.getProfileImage(),
                StorageFileType.PROFILE_IMAGE
        );

        Speciality speciality = specialityService.findByUUIDNotDeleted(doctorCreateDTO.getSpecialityId());

        Doctor doctor = create(
                user,
                doctorCreateDTO.getDescription(),
                doctorCreateDTO.getGmcNumber(),
                profileImage,
                speciality
        );


        return doctor;
    }

    private Doctor create(User user, String description, String gmcNumber, StorageFile profileImage, Speciality speciality) {
        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setDescription(description);
        doctor.setGmcNumber(gmcNumber);
        doctor.setProfileImage(profileImage);
        doctor.setIsDeleted(false);
        doctor.setSpeciality(speciality);
        return save(doctor);
    }

    public Doctor update(String uuid, DoctorUpdateDTO doctorUpdateDTO) {
        Doctor doctor = findByUUIDNotDeleted(uuid);
        if (doctorUpdateDTO.getDescription() != null)
            doctor.setDescription(doctorUpdateDTO.getDescription());

        if (doctorUpdateDTO.getGmcNumber() != null)
            doctor.setGmcNumber(doctorUpdateDTO.getGmcNumber());

        if (doctorUpdateDTO.getProfileImage() != null
                && !doctorUpdateDTO.getProfileImage().isEmpty()) {

            StorageFile profileImage = storageFileService.create(
                    doctorUpdateDTO.getProfileImage(),
                    StorageFileType.PROFILE_IMAGE
            );
            doctor.setProfileImage(profileImage);
        }

        if (doctorUpdateDTO.getSpecialityId() != null) {
            Speciality speciality = specialityService.findByUUIDNotDeleted(doctorUpdateDTO.getSpecialityId());
            doctor.setSpeciality(speciality);
        }

        return save(doctor);
    }

    public Page<Doctor> findAllByNameLike(String name, Pageable pageable) {
        if (name != null)
            return doctorRepository.findAllByNameLike(name, pageable);
        return doctorRepository.findAllByIsDeleted(false, pageable);
    }


    public void seeder() throws Exception {
        if (doctorRepository.count() == 0) {


            createDoctor("علی", "باقری", String.format("0912%s", randomUtil.generateRandomNumber(7)), "nutrition_senior", "m1.jpg");
            createDoctor("زهرا", "رضایی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "nutrition", "w1.jpg");

            createDoctor("محمد", "حسین زاده", String.format("0912%s", randomUtil.generateRandomNumber(7)), "child_expert", "m2.jpg");
            createDoctor("شایان", "علیپور", String.format("0912%s", randomUtil.generateRandomNumber(7)), "child_expert_assistant", "m3.jpg");
            createDoctor("مبینا", "خوشبخت", String.format("0912%s", randomUtil.generateRandomNumber(7)), "child_specialist", "w2.jpg");

            createDoctor("لیلا", "خیری", String.format("0912%s", randomUtil.generateRandomNumber(7)), "dentist", "w3.jpg");
            createDoctor("حسین", "محمد نژاد", String.format("0912%s", randomUtil.generateRandomNumber(7)), "gum_surgery_expert", "m4.jpg");

            createDoctor("محدثه", "نجفی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "ophthalmology_expert", "w4.jpg");
            createDoctor("حمید", "لشکری", String.format("0912%s", randomUtil.generateRandomNumber(7)), "optometry_expert", "m5.jpg");
            createDoctor("سمیرا", "یاقوت", String.format("0912%s", randomUtil.generateRandomNumber(7)), "ophthalmology_expert_assistant", "w5.jpg");


            createDoctor("اکیر", "فهمیده", String.format("0912%s", randomUtil.generateRandomNumber(7)), "general", "m6.jpg");

            createDoctor("الهام", "رزمجوی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "otorhinolaryngology_expert", "w6.jpg");
            createDoctor("غلام حسین", "شمس", String.format("0912%s", randomUtil.generateRandomNumber(7)), "otorhinolaryngology_expert_assistant", "m7.jpg");

            createDoctor("سمیه", "بهرامی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "cardiovascular_expert", "w7.jpg");
            createDoctor("مجید", "سعیدی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "cardiovascular_specialist", "m8.jpg");
            createDoctor("محمد علی", "موسوی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "cardiovascular_expert_assistant", "m9.jpg");

            createDoctor("معصومه", "کرمی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "general_surgeon_expert", "w8.jpg");
            createDoctor("مرتضی", "سوزنده", String.format("0912%s", randomUtil.generateRandomNumber(7)), "general_surgeon_expert_assistant", "m10.jpg");

            createDoctor("امیر", "حکیمی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "neurology_expert", "m11.jpg");
            createDoctor("فرهاد", "اصغری", String.format("0912%s", randomUtil.generateRandomNumber(7)), "neurology_specialist", "m12.jpg");
            createDoctor("پروین", "بهزادی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "neurology_expert_assistant", "w9.jpg");
            createDoctor("فرشید", "نیکنام", String.format("0912%s", randomUtil.generateRandomNumber(7)), "neurology_resident", "w10.jpg");

            createDoctor("مینا", "علوی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "orthopedist_expert", "w11.jpg");
            createDoctor("مهدی", "کرامتی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "orthopedist_expert_assistant", "m14.jpg");
            createDoctor("سینا", "رضوی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "orthopedist_expert", "m20.png");
            createDoctor("پویا", "مرجانی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "orthopedist_resident", "m15.jpg");

            createDoctor("نیما", "رضازاده", String.format("0912%s", randomUtil.generateRandomNumber(7)), "plastic_surgery_specialist", "m18.jpg");
            createDoctor("عرفان", "داننده", String.format("0912%s", randomUtil.generateRandomNumber(7)), "plastic_surgery_expert_assistant", "m16.jpg");
            createDoctor("شروین", "قمیشی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "plastic_surgery_fellowship", "m19.jpg");

            createDoctor("سعید", "ریگی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "dermatologist_expert_assistant", "m17.jpg");
            createDoctor("سارا", "هاشمی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "dermatologist_expert", "w14.jpg");

            createDoctor("ریحانه", "امامی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "obstetricians_expert", "w12.jpg");
            createDoctor("نگار", "نوبخت", String.format("0912%s", randomUtil.generateRandomNumber(7)), "obstetricians_expert_assistant", "w13.jpg");

        }
    }

    public void createDoctor(String firstName, String lastName, String phone, String specialityName, String profileImageName) throws Exception {
        User user = userService.createUser(phone, firstName, lastName);
        Speciality speciality = specialityService.findByName(specialityName);
        StorageFile profileImage = storageFileService.create(new ClassPathResource(String.format("static/profileImages/%s", profileImageName)).getInputStream(), StorageFileType.CATEGORY_IMAGE, "");


        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setGmcNumber(randomUtil.generateRandomNumber(6));
        doctor.setSpeciality(speciality);
        doctor.setProfileImage(profileImage);
        doctor.setIsDeleted(false);
        save(doctor);
    }

}
