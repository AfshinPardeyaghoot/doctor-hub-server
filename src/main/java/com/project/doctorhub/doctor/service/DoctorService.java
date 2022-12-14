package com.project.doctorhub.doctor.service;

import com.project.doctorhub.auth.model.Role;
import com.project.doctorhub.base.exception.HttpException;
import com.project.doctorhub.base.exception.NotFoundException;
import com.project.doctorhub.base.service.AbstractCrudService;
import com.project.doctorhub.consultation.model.ConsultationType;
import com.project.doctorhub.consultation.service.ConsultationTypeService;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
public class DoctorService
        extends AbstractCrudService<Doctor, Long, DoctorRepository> {

    private final RandomUtil randomUtil;
    private final UserService userService;
    private final DoctorRepository doctorRepository;
    private final SpecialityService specialityService;
    private final StorageFileService storageFileService;
    private final ConsultationTypeService consultationTypeService;
    private final DoctorConsultationTypeService doctorConsultationTypeService;

    public DoctorService(
            RandomUtil randomUtil,
            DoctorRepository abstractRepository,
            UserService userService,
            StorageFileService storageFileService,
            SpecialityService specialityService,
            DoctorRepository doctorRepository,
            ConsultationTypeService consultationTypeService,
            DoctorConsultationTypeService doctorConsultationTypeService
    ) {
        super(abstractRepository);
        this.randomUtil = randomUtil;
        this.userService = userService;
        this.doctorRepository = doctorRepository;
        this.specialityService = specialityService;
        this.storageFileService = storageFileService;
        this.consultationTypeService = consultationTypeService;
        this.doctorConsultationTypeService = doctorConsultationTypeService;
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

    public List<Doctor> findAllByNameLike() {
        return doctorRepository.findAllByIsDeleted(false);
    }


    public void seeder() throws Exception {
        if (doctorRepository.count() == 0) {

            ConsultationType textConsultation = consultationTypeService.findByNameNotDeleted("text");
            ConsultationType voiceConsultation = consultationTypeService.findByNameNotDeleted("voice");


            Doctor doctor = createDoctor("علی", "باقری", String.format("0912%s", randomUtil.generateRandomNumber(7)), "nutrition_senior", "m1.jpg");
            doctorConsultationTypeService.create(doctor, textConsultation, 500000L);
            doctorConsultationTypeService.create(doctor, voiceConsultation, 600000L);

            Doctor doctor1 = createDoctor("زهرا", "رضایی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "nutrition", "w1.jpg");
            doctorConsultationTypeService.create(doctor1, textConsultation, 500000L);
            doctorConsultationTypeService.create(doctor1, voiceConsultation, 700000L);

            Doctor doctor2 = createDoctor("محمد", "حسین زاده", String.format("0912%s", randomUtil.generateRandomNumber(7)), "child_expert", "m2.jpg");
            Doctor doctor3 = createDoctor("شایان", "علیپور", String.format("0912%s", randomUtil.generateRandomNumber(7)), "child_expert_assistant", "m3.jpg");
            Doctor doctor4 = createDoctor("مبینا", "خوشبخت", String.format("0912%s", randomUtil.generateRandomNumber(7)), "child_specialist", "w2.jpg");
            doctorConsultationTypeService.create(doctor2, textConsultation, 500000L);
            doctorConsultationTypeService.create(doctor3, textConsultation, 600000L);
            doctorConsultationTypeService.create(doctor4, textConsultation, 500000L);
            doctorConsultationTypeService.create(doctor3, voiceConsultation, 800000L);

            Doctor doctor5 = createDoctor("لیلا", "خیری", String.format("0912%s", randomUtil.generateRandomNumber(7)), "dentist", "w3.jpg");
            Doctor doctor6 = createDoctor("حسین", "محمد نژاد", String.format("0912%s", randomUtil.generateRandomNumber(7)), "gum_surgery_expert", "m4.jpg");
            doctorConsultationTypeService.create(doctor5, textConsultation, 700000L);
            doctorConsultationTypeService.create(doctor6, textConsultation, 500000L);
            doctorConsultationTypeService.create(doctor5, voiceConsultation, 450000L);

            Doctor doctor7 = createDoctor("محدثه", "نجفی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "ophthalmology_expert", "w4.jpg");
            Doctor doctor8 = createDoctor("حمید", "لشکری", String.format("0912%s", randomUtil.generateRandomNumber(7)), "optometry_expert", "m5.jpg");
            Doctor doctor9 = createDoctor("سمیرا", "یاقوت", String.format("0912%s", randomUtil.generateRandomNumber(7)), "ophthalmology_expert_assistant", "w5.jpg");
            doctorConsultationTypeService.create(doctor7, textConsultation, 500000L);
            doctorConsultationTypeService.create(doctor8, textConsultation, 400000L);
            doctorConsultationTypeService.create(doctor9, textConsultation, 600000L);
            doctorConsultationTypeService.create(doctor7, voiceConsultation, 700000L);
            doctorConsultationTypeService.create(doctor9, voiceConsultation, 650000L);


            Doctor doctor10 = createDoctor("اکیر", "فهمیده", String.format("0912%s", randomUtil.generateRandomNumber(7)), "general", "m6.jpg");
            doctorConsultationTypeService.create(doctor10, textConsultation, 200000L);
            doctorConsultationTypeService.create(doctor, voiceConsultation, 300000L);

            Doctor doctor11 = createDoctor("الهام", "رزمجوی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "otorhinolaryngology_expert", "w6.jpg");
            Doctor doctor12 = createDoctor("غلام حسین", "شمس", String.format("0912%s", randomUtil.generateRandomNumber(7)), "otorhinolaryngology_expert_assistant", "m7.jpg");
            doctorConsultationTypeService.create(doctor11, textConsultation, 600000L);
            doctorConsultationTypeService.create(doctor12, textConsultation, 500000L);
            doctorConsultationTypeService.create(doctor11, voiceConsultation, 650000L);
            doctorConsultationTypeService.create(doctor12, voiceConsultation, 800000L);

            Doctor doctor13 = createDoctor("سمیه", "بهرامی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "cardiovascular_expert", "w7.jpg");
            Doctor doctor14 = createDoctor("مجید", "سعیدی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "cardiovascular_specialist", "m8.jpg");
            Doctor doctor15 = createDoctor("محمد علی", "موسوی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "cardiovascular_expert_assistant", "m9.jpg");
            doctorConsultationTypeService.create(doctor13, textConsultation, 500000L);
            doctorConsultationTypeService.create(doctor14, textConsultation, 800000L);
            doctorConsultationTypeService.create(doctor15, textConsultation, 300000L);
            doctorConsultationTypeService.create(doctor15, voiceConsultation, 600000L);

            Doctor doctor16 = createDoctor("معصومه", "کرمی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "general_surgeon_expert", "w8.jpg");
            Doctor doctor17 = createDoctor("مرتضی", "سوزنده", String.format("0912%s", randomUtil.generateRandomNumber(7)), "general_surgeon_expert_assistant", "m10.jpg");
            doctorConsultationTypeService.create(doctor16, textConsultation, 500000L);
            doctorConsultationTypeService.create(doctor17, textConsultation, 400000L);
            doctorConsultationTypeService.create(doctor16, voiceConsultation, 600000L);
            doctorConsultationTypeService.create(doctor17, voiceConsultation, 600000L);

            Doctor doctor18 = createDoctor("امیر", "حکیمی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "neurology_expert", "m11.jpg");
            Doctor doctor19 = createDoctor("فرهاد", "اصغری", String.format("0912%s", randomUtil.generateRandomNumber(7)), "neurology_specialist", "m12.jpg");
            Doctor doctor20 = createDoctor("پروین", "بهزادی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "neurology_expert_assistant", "w9.jpg");
            Doctor doctor21 = createDoctor("فرشید", "نیکنام", String.format("0912%s", randomUtil.generateRandomNumber(7)), "neurology_resident", "w10.jpg");
            doctorConsultationTypeService.create(doctor18, textConsultation, 500000L);
            doctorConsultationTypeService.create(doctor19, textConsultation, 800000L);
            doctorConsultationTypeService.create(doctor20, textConsultation, 400000L);
            doctorConsultationTypeService.create(doctor21, textConsultation, 600000L);
            doctorConsultationTypeService.create(doctor20, voiceConsultation, 650000L);
            doctorConsultationTypeService.create(doctor18, voiceConsultation, 650000L);
            doctorConsultationTypeService.create(doctor19, voiceConsultation, 700000L);

            Doctor doctor22 = createDoctor("مینا", "علوی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "orthopedist_expert", "w11.jpg");
            Doctor doctor23 = createDoctor("مهدی", "کرامتی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "orthopedist_expert_assistant", "m14.jpg");
            Doctor doctor24 = createDoctor("سینا", "رضوی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "orthopedist_expert", "m20.png");
            Doctor doctor25 = createDoctor("پویا", "مرجانی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "orthopedist_resident", "m15.jpg");
            doctorConsultationTypeService.create(doctor22, textConsultation, 400000L);
            doctorConsultationTypeService.create(doctor23, textConsultation, 400000L);
            doctorConsultationTypeService.create(doctor24, textConsultation, 500000L);
            doctorConsultationTypeService.create(doctor25, textConsultation, 700000L);
            doctorConsultationTypeService.create(doctor24, voiceConsultation, 650000L);
            doctorConsultationTypeService.create(doctor23, voiceConsultation, 750000L);

            Doctor doctor26 = createDoctor("نیما", "رضازاده", String.format("0912%s", randomUtil.generateRandomNumber(7)), "plastic_surgery_specialist", "m18.jpg");
            Doctor doctor27 = createDoctor("عرفان", "داننده", String.format("0912%s", randomUtil.generateRandomNumber(7)), "plastic_surgery_expert_assistant", "m16.jpg");
            Doctor doctor28 = createDoctor("شروین", "قمیشی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "plastic_surgery_fellowship", "m19.jpg");
            doctorConsultationTypeService.create(doctor26, textConsultation, 800000L);
            doctorConsultationTypeService.create(doctor27, textConsultation, 500000L);
            doctorConsultationTypeService.create(doctor28, textConsultation, 700000L);
            doctorConsultationTypeService.create(doctor27, voiceConsultation, 600000L);
            doctorConsultationTypeService.create(doctor26, voiceConsultation, 600000L);


            Doctor doctor29 = createDoctor("سعید", "ریگی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "dermatologist_expert_assistant", "m17.jpg");
            Doctor doctor30 = createDoctor("سارا", "هاشمی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "dermatologist_expert", "w14.jpg");
            doctorConsultationTypeService.create(doctor29, textConsultation, 550000L);
            doctorConsultationTypeService.create(doctor30, textConsultation, 600000L);
            doctorConsultationTypeService.create(doctor29, voiceConsultation, 600000L);
            doctorConsultationTypeService.create(doctor30, voiceConsultation, 600000L);

            Doctor doctor31 = createDoctor("ریحانه", "امامی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "obstetricians_expert", "w12.jpg");
            Doctor doctor32 = createDoctor("نگار", "نوبخت", String.format("0912%s", randomUtil.generateRandomNumber(7)), "obstetricians_expert_assistant", "w13.jpg");
            doctorConsultationTypeService.create(doctor31, textConsultation, 500000L);
            doctorConsultationTypeService.create(doctor32, textConsultation, 800000L);
            doctorConsultationTypeService.create(doctor32, voiceConsultation, 600000L);
        }
    }

    public Doctor createDoctor(String firstName, String lastName, String phone, String specialityName, String profileImageName) throws Exception {
        User user = userService.createUser(phone, firstName, lastName);
        Speciality speciality = specialityService.findByName(specialityName);
        StorageFile profileImage = storageFileService.create(new ClassPathResource(String.format("static/profileImages/%s", profileImageName)).getInputStream(), StorageFileType.CATEGORY_IMAGE, "");


        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setGmcNumber(randomUtil.generateRandomNumber(6));
        doctor.setSpeciality(speciality);
        doctor.setProfileImage(profileImage);
        doctor.setIsDeleted(false);
        return save(doctor);

    }

    public Doctor findByPhoneNotDeleted(String doctorPhone) {
        return doctorRepository.findByPhone(doctorPhone)
                .orElseThrow(() -> new NotFoundException("پزشکی با شماره تلفن وارد شده پیدا نشد!"));
    }
}
