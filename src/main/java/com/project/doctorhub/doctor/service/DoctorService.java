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
import com.project.doctorhub.schedule.model.DayOfWeek;
import com.project.doctorhub.schedule.service.DoctorScheduleService;
import com.project.doctorhub.speciality.model.Speciality;
import com.project.doctorhub.speciality.service.SpecialityService;
import com.project.doctorhub.storageFile.model.StorageFile;
import com.project.doctorhub.storageFile.model.StorageFileType;
import com.project.doctorhub.storageFile.service.StorageFileService;
import com.project.doctorhub.user.model.User;
import com.project.doctorhub.user.service.UserService;
import com.project.doctorhub.util.RandomUtil;
import org.springframework.context.annotation.Lazy;
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
    private final DoctorScheduleService doctorScheduleService;
    private final ConsultationTypeService consultationTypeService;
    private final DoctorConsultationTypeService doctorConsultationTypeService;

    public DoctorService(
            RandomUtil randomUtil,
            DoctorRepository abstractRepository,
            UserService userService,
            StorageFileService storageFileService,
            @Lazy SpecialityService specialityService,
            DoctorRepository doctorRepository,
            @Lazy DoctorScheduleService doctorScheduleService,
            ConsultationTypeService consultationTypeService,
            DoctorConsultationTypeService doctorConsultationTypeService
    ) {
        super(abstractRepository);
        this.randomUtil = randomUtil;
        this.userService = userService;
        this.doctorRepository = doctorRepository;
        this.specialityService = specialityService;
        this.storageFileService = storageFileService;
        this.doctorScheduleService = doctorScheduleService;
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


            Doctor doctor = createDoctor("علی", "باقری", String.format("0912%s", randomUtil.generateRandomNumber(7)), "nutrition_senior", "m1.jpg", "برای گرفتن برنامه غذایی و رژیم لطفا درخواست مشاوره متنی دهید تا راهنمایی شوید. مدرس و پژوهشگر دکتری تخصصی تغذیه و رژیم درمانی (دانشگاه شهید بهشتی تهران) عضو انجمن رژیم شناسان امریکا عضو انجمن تغذیه ایران مشاوره تخصصی تغذیه در زمینه های چاقی، لاغری،ورزشکاران، زنان باردار و شیرده، افزایش قد و رژیم درمانی در بیماری ها (کبد چرب، دیابت، پرفشاری خون و ...) مشاوره همراه با تنظیم و ارائه برنامه غذایی نفر اول آزمون دکتری تخصصی سابقه 11 سال رژیم درمانی ",5f,0);
            doctorConsultationTypeService.create(doctor, textConsultation, 500000L);
            doctorConsultationTypeService.create(doctor, voiceConsultation, 600000L);
            doctorScheduleService.create(doctor, DayOfWeek.WED, "08:00", "18:00");
            doctorScheduleService.create(doctor, DayOfWeek.THU, "10:00", "20:00");
            doctorScheduleService.create(doctor, DayOfWeek.SUN, "08:00", "14:00");
            doctorScheduleService.create(doctor, DayOfWeek.MON, "09:00", "17:00");
            doctorScheduleService.create(doctor, DayOfWeek.SAT, "10:00", "20:00");
            doctorScheduleService.create(doctor, DayOfWeek.TUE, "09:00", "19:00");

            Doctor doctor1 = createDoctor("زهرا", "رضایی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "nutrition", "w1.jpg", "برای گرفتن برنامه غذایی و رژیم لطفا درخواست مشاوره متنی دهید تا راهنمایی شوید. مشاور تغذیه و تناسب اندام هستم، صاحب امتیاز اولین سیستم مشاوره تغذیه تلفنی در کشور. تجویز تخصصی مکمل های ورزشی بدنسازی بانوان. در صورت تمایل اصلاح مزاج و تنطیم برنامه از دیدگاه طب سنتی. در حال حاضر تمرکز اصلی کار ما روی تغذیه ورزشی و کاهش وزن بدون گرسنگی هست.",5f,0);
            doctorConsultationTypeService.create(doctor1, textConsultation, 500000L);
            doctorConsultationTypeService.create(doctor1, voiceConsultation, 700000L);
            doctorScheduleService.create(doctor1, DayOfWeek.WED, "08:00", "18:00");
            doctorScheduleService.create(doctor1, DayOfWeek.THU, "10:00", "20:00");
            doctorScheduleService.create(doctor1, DayOfWeek.SUN, "08:00", "14:00");
            doctorScheduleService.create(doctor1, DayOfWeek.TUE, "09:00", "19:00");

            Doctor doctor2 = createDoctor("محمد", "حسین زاده", String.format("0912%s", randomUtil.generateRandomNumber(7)), "child_expert", "m2.jpg", "7سال سابقه طبابت متخصص کودکان و نوزادان و در موارد بیماریهای گوارشی، تنفسی، قلبی، مشکلات غددی، مشکلات خونی، بیماری های عفونی ، آسم و آلرژی، و تغذیه و مراقبت نوزادان فارغ التحصیل دانشگاه علوم پزشکی اصفهان و دانشگاه علوم پزشکی گیلان",5f,0);
            Doctor doctor3 = createDoctor("شایان", "علیپور", String.format("0912%s", randomUtil.generateRandomNumber(7)), "child_expert_assistant", "m3.jpg", "متخصص کودکان و نوزادان از دانشگاه شهید بهشتی،سابقه کار در بخش کودکان و نوزادان بیمارستان مفید، مهدیه، طالقانی ، نجمیه و نیکان اقدسیه، مشاوره در زمینه بیماریهای نوزادان، بیماریهای عفونی کودکان، بیماریهای گوارش کودکان، بیماریهای تنفسی کودکان و کرونا در کودکان و بیماریهای مغز و اعصاب کودکان و مشاوره در زمینه پایش رشد(قد و وزن) و تکامل نوزادان و کودکان، مشاوره تغذیه و مشاوره در زمینه واکسیناسیون",5f,0);
            Doctor doctor4 = createDoctor("مبینا", "خوشبخت", String.format("0912%s", randomUtil.generateRandomNumber(7)), "child_specialist", "w2.jpg", " متخصص کودکان و نوزادان فارغ التحصیل ازدانشگاه علوم پزشکی شهید بهشتی 24 سال سابقه طبابت رشد و تکامل نوزاد شیردهی نوزاد و زردی نوزادی درمان بیماری های کودکان غدد ،عفونی ، آلرژی ،اعصاب و... ",5f,0);
            doctorConsultationTypeService.create(doctor2, textConsultation, 500000L);
            doctorConsultationTypeService.create(doctor3, textConsultation, 600000L);
            doctorConsultationTypeService.create(doctor4, textConsultation, 500000L);
            doctorConsultationTypeService.create(doctor3, voiceConsultation, 800000L);
            doctorScheduleService.create(doctor2, DayOfWeek.WED, "08:00", "18:00");
            doctorScheduleService.create(doctor2, DayOfWeek.THU, "10:00", "20:00");
            doctorScheduleService.create(doctor2, DayOfWeek.SUN, "08:00", "14:00");
            doctorScheduleService.create(doctor3, DayOfWeek.SUN, "08:00", "14:00");
            doctorScheduleService.create(doctor3, DayOfWeek.MON, "09:00", "17:00");
            doctorScheduleService.create(doctor3, DayOfWeek.SAT, "10:00", "20:00");
            doctorScheduleService.create(doctor4, DayOfWeek.TUE, "09:00", "19:00");

            Doctor doctor5 = createDoctor("لیلا", "خیری", String.format("0912%s", randomUtil.generateRandomNumber(7)), "dentist", "w3.jpg", "دارای 7 سال سابقه کار کارهای ترمیمی زیبایی مشاوره جهت دردهای سر و گردن و بیماری های دهان و دندان دندانپزشکی اطفال جراحی دندان های نهفته درمان عفونت های دندانی ",5f,0);
            Doctor doctor6 = createDoctor("حسین", "محمد نژاد", String.format("0912%s", randomUtil.generateRandomNumber(7)), "gum_surgery_expert", "m4.jpg", "فارغ\u200Cالتحصیل از اصفهان مشاوره درمان های : زیبایی جراحی دندان عقل و دیگر دندانهای نهفته و انواع مشکلات پس از جراحی درمان ریشه کودکان و مراقبت های لازم برای حفظ هرچه بیشتر سلامت دندان\u200Cهای آنها، درمانهای پیشگیری کودکان مشکلات فک و زیبایی بیماری های دهان از جمله آفت ، تبخال ، کیست ها و تومور های فکی",5f,0);
            doctorConsultationTypeService.create(doctor5, textConsultation, 700000L);
            doctorConsultationTypeService.create(doctor6, textConsultation, 500000L);
            doctorConsultationTypeService.create(doctor5, voiceConsultation, 450000L);
            doctorScheduleService.create(doctor6, DayOfWeek.WED, "08:00", "18:00");
            doctorScheduleService.create(doctor5, DayOfWeek.THU, "10:00", "20:00");
            doctorScheduleService.create(doctor5, DayOfWeek.SUN, "08:00", "14:00");
            doctorScheduleService.create(doctor6, DayOfWeek.MON, "09:00", "17:00");
            doctorScheduleService.create(doctor6, DayOfWeek.SAT, "10:00", "20:00");
            doctorScheduleService.create(doctor5, DayOfWeek.TUE, "09:00", "19:00");

            Doctor doctor7 = createDoctor("محدثه", "نجفی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "ophthalmology_expert", "w4.jpg", "جراح و متخصص بیماری های چشم فارغ التحصیل از دانشگاه علوم پزشکی شیراز",5f,0);
            Doctor doctor8 = createDoctor("حمید", "لشکری", String.format("0912%s", randomUtil.generateRandomNumber(7)), "optometry_expert", "m5.jpg", " فارغ التحصیل پزشکی عمومی از وانشگاه علوم پزشکی تهران رزیدنت چشم پزشکی دانشگاه علوم پزشکی ایران",5f,0);
            Doctor doctor9 = createDoctor("سمیرا", "یاقوت", String.format("0912%s", randomUtil.generateRandomNumber(7)), "ophthalmology_expert_assistant", "w5.jpg", "جراح و متخصص چشم پزشکی (رزیدنت سال سوم) انجام جراحی های : آب مروارید , بلفاروپلاستی ,مجاری اشکی ,برداشتن عینک , افتادگی پلک , تزریقات داخل چشمی , انحراف چشم , ناخنک و خال ملتحمه درمان بیماریهای چشمی اعم از :عفونت ها , التهاب های داخل چشمی , اشک ریزش , آب سیاه , تنبلی چشم , تروما های چشمی , اختلالات پلکی , تجویز عینک , مشکلات چشمی بیماران دیابتی , قوز قرنیه و سایر اورژانس های چشم پزشکی ",5f,0);
            doctorConsultationTypeService.create(doctor7, textConsultation, 500000L);
            doctorConsultationTypeService.create(doctor8, textConsultation, 400000L);
            doctorConsultationTypeService.create(doctor9, textConsultation, 600000L);
            doctorConsultationTypeService.create(doctor7, voiceConsultation, 700000L);
            doctorConsultationTypeService.create(doctor9, voiceConsultation, 650000L);
            doctorScheduleService.create(doctor7, DayOfWeek.WED, "08:00", "18:00");
            doctorScheduleService.create(doctor9, DayOfWeek.THU, "10:00", "20:00");
            doctorScheduleService.create(doctor9, DayOfWeek.SUN, "08:00", "14:00");
            doctorScheduleService.create(doctor7, DayOfWeek.MON, "09:00", "17:00");
            doctorScheduleService.create(doctor9, DayOfWeek.MON, "09:00", "17:00");
            doctorScheduleService.create(doctor8, DayOfWeek.MON, "09:00", "17:00");
            doctorScheduleService.create(doctor8, DayOfWeek.SAT, "10:00", "20:00");
            doctorScheduleService.create(doctor8, DayOfWeek.TUE, "09:00", "19:00");


            Doctor doctor10 = createDoctor("اکیر", "فهمیده", String.format("0912%s", randomUtil.generateRandomNumber(7)), "general", "m6.jpg", " دارای مدرک پزشکی از دانشگاه دولتی و با 24 سال سابقه طبابت در زمینه داخلی، اطفال، اعصاب و سابقه 8 ساله درمان اعتیاد مواد مخدر و شیشه در...",5f,0);
            doctorConsultationTypeService.create(doctor10, textConsultation, 200000L);
            doctorConsultationTypeService.create(doctor, voiceConsultation, 300000L);
            doctorScheduleService.create(doctor10, DayOfWeek.WED, "08:00", "18:00");
            doctorScheduleService.create(doctor10, DayOfWeek.THU, "10:00", "20:00");
            doctorScheduleService.create(doctor10, DayOfWeek.SUN, "08:00", "14:00");
            doctorScheduleService.create(doctor10, DayOfWeek.MON, "09:00", "17:00");

            Doctor doctor11 = createDoctor("الهام", "رزمجوی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "otorhinolaryngology_expert", "w6.jpg", "متخصص گوش وحلق وبینی مسایل مربوط به پزشکی قانونی با سابقه کاری در ان حوزه",5f,0);
            Doctor doctor12 = createDoctor("غلام حسین", "شمس", String.format("0912%s", randomUtil.generateRandomNumber(7)), "otorhinolaryngology_expert_assistant", "m7.jpg", "31898 فارغ التحصیل از دانشگاه علوم پزشکی اهواز دارای بیش از سی سال سابقه فعالیت متخصص گوش و حلق و بینی انجام اعمال جراحی سروگردن و زیبایی بینی با تعرفه دولتی مشاوره رایگان ",5f,0);
            doctorConsultationTypeService.create(doctor11, textConsultation, 600000L);
            doctorConsultationTypeService.create(doctor12, textConsultation, 500000L);
            doctorConsultationTypeService.create(doctor11, voiceConsultation, 650000L);
            doctorConsultationTypeService.create(doctor12, voiceConsultation, 800000L);
            doctorScheduleService.create(doctor11, DayOfWeek.WED, "08:00", "18:00");
            doctorScheduleService.create(doctor11, DayOfWeek.THU, "10:00", "20:00");
            doctorScheduleService.create(doctor12, DayOfWeek.SUN, "08:00", "14:00");
            doctorScheduleService.create(doctor12, DayOfWeek.MON, "09:00", "17:00");
            doctorScheduleService.create(doctor11, DayOfWeek.SAT, "10:00", "20:00");
            doctorScheduleService.create(doctor12, DayOfWeek.SAT, "10:00", "20:00");
            doctorScheduleService.create(doctor12, DayOfWeek.TUE, "09:00", "19:00");

            Doctor doctor13 = createDoctor("سمیه", "بهرامی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "cardiovascular_expert", "w7.jpg", "150674 فارغ التحصیل دوره پزشکی عمومی و تخصص قلب و عروق از دانشگاه علوم پزشکی شهید بهشتی شهر تهران دارای مدرک زبان رشته پزشکی (MCHE)، دارای بورد تخصصی در رشته قلب و عروق شرکت در چندین کنگره علمی داخلی و خارجی", 5f,0);
            Doctor doctor14 = createDoctor("مجید", "سعیدی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "cardiovascular_specialist", "m8.jpg", " هیأت علمی دانشگاه علوم پزشکی شهید بهشتی. سابقه تالیف و ترجمه کتب متعدد در شاخه تخصصی کودکان و فوق تخصصی قلب کودکان. دارنده چندین مقاله علمی در مجلات داخلی و خارجی.", 5f,0);
            Doctor doctor15 = createDoctor("محمد علی", "موسوی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "cardiovascular_expert_assistant", "m9.jpg", "سابقه 5 سال فعالیت مستمر در اورژانس های جنرال به عنوان پزشک اورژانس دوره های اورژانس-داخلی-اطفال رزیدنت تخصصی بیماریهای قلب و عروق ", 5f,0);
            doctorConsultationTypeService.create(doctor13, textConsultation, 500000L);
            doctorConsultationTypeService.create(doctor14, textConsultation, 800000L);
            doctorConsultationTypeService.create(doctor15, textConsultation, 300000L);
            doctorConsultationTypeService.create(doctor15, voiceConsultation, 600000L);
            doctorScheduleService.create(doctor13, DayOfWeek.WED, "08:00", "18:00");
            doctorScheduleService.create(doctor14, DayOfWeek.THU, "10:00", "20:00");
            doctorScheduleService.create(doctor13, DayOfWeek.SUN, "08:00", "14:00");
            doctorScheduleService.create(doctor14, DayOfWeek.MON, "09:00", "17:00");
            doctorScheduleService.create(doctor13, DayOfWeek.SAT, "10:00", "20:00");
            doctorScheduleService.create(doctor14, DayOfWeek.TUE, "09:00", "19:00");
            doctorScheduleService.create(doctor13, DayOfWeek.TUE, "09:00", "19:00");

            Doctor doctor16 = createDoctor("معصومه", "کرمی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "general_surgeon_expert", "w8.jpg", " سابقه ی پنج سال طبابت در بیمارستان سانتر دانشگاهی. دو سال رئیس درمانگاه و دارای 2مقاله پژوهشی. مشاوره در زمینه ی مشکلات داخلی٬ جراحی٬ اطفال و زنان٬ اعصاب و مشاوره ی روان پزشکی و روان شناسی. پیگیری و تنظیم فشار خون٬ قند خون و تیروئید. مشاوره و\u200Cدرمان مشکلات پوست و مو و زیبایی. مشاوره و درمان کرونا با 18ماه سابقه طبابت در مرکز غربالگری کرونا. گذراندن دوره های حجامت و طب سنتی در مراکز طب سنتی دانشگاهی.", 5f,0);
            Doctor doctor17 = createDoctor("مرتضی", "سوزنده", String.format("0912%s", randomUtil.generateRandomNumber(7)), "general_surgeon_expert_assistant", "m10.jpg", "متخصص جراحی عمومی _جراح برتر نیروهای مسلح _چهار دوره جراح برتر بیمارستان امام حسین (ع) _دارای 22 سال سابقه طبابت و خدمات درمانی انجام مشاوره و اعمال جراحی در زمینه های: _دستگاه گوارش _معده _روده بزرگ و کوچک _لاپاروسکوپی _زیبایی شکم _سرطان ها _کیسه صفرا _اپاندیس _فتق _تیرویید _پستان _بواسیر _اعمال جراحی سرپایی *مشاوره علائم شکمی کورونا", 5f,0);
            doctorConsultationTypeService.create(doctor16, textConsultation, 500000L);
            doctorConsultationTypeService.create(doctor17, textConsultation, 400000L);
            doctorConsultationTypeService.create(doctor16, voiceConsultation, 600000L);
            doctorConsultationTypeService.create(doctor17, voiceConsultation, 600000L);
            doctorScheduleService.create(doctor16, DayOfWeek.WED, "08:00", "18:00");
            doctorScheduleService.create(doctor16, DayOfWeek.THU, "10:00", "20:00");
            doctorScheduleService.create(doctor17, DayOfWeek.SUN, "08:00", "14:00");
            doctorScheduleService.create(doctor17, DayOfWeek.MON, "09:00", "17:00");
            doctorScheduleService.create(doctor17, DayOfWeek.SAT, "10:00", "20:00");
            doctorScheduleService.create(doctor17, DayOfWeek.TUE, "09:00", "19:00");

            Doctor doctor18 = createDoctor("امیر", "حکیمی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "neurology_expert", "m11.jpg", " متخصص بیماریهای مغز و اعصاب و ستون فقرات از دانشگاه علوم پزشکی تبریز حیطه کاری: بیماریهای عروقی مغز مانند سکته مغزی، خونریزی مغزی انواع سردرد و سرگیجه تشنج بیماریهای مربوط به ستون فقرات مانند دیسک کمر، تنگی کانال نخاعی بیماریهای درگیر کننده اعصاب محیطی که با علایمی مانند درد،ضعف، گزگز و کرختی دست و پا تظاهر می یابد بیماریهای التهابی درگیر کننده سیستم عصبی مانند ام اس بیماریهای نورودژنراتیو مانند پارکینسون، انواع دمانس از جمله آلزایمر و... اختلالات حرکتی مانند ترمور(لرزش)، تیک، کره و...", 5f,0);
            Doctor doctor19 = createDoctor("فرهاد", "اصغری", String.format("0912%s", randomUtil.generateRandomNumber(7)), "neurology_specialist", "m12.jpg", " اینجانب متخصص کودکان و فوق تخصص بیماریهای مغز و اعصاب کودکان با 10 سال سایقه فعالیت در زمینه بیماریهای کودکان به خصوص در بیماریهای تشنج تاخیر تکاملی و میگرن و سایر بیماریهای نورولوژی کودکان می باشم.", 5f,0);
            Doctor doctor20 = createDoctor("پروین", "بهزادی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "neurology_expert_assistant", "w9.jpg", "دستیار تخصصی جراحی مغز و اعصاب و ستون فقرات مشاوره جهت تشخیص و درمان بیمارهای مرتبط با جراحی مغز و اعصاب انواع سردرد ، تشنج ، ضایعات مغزی ، خونریزی های مغزی، تومورهای مغزی و نخاعی ، هیدرسفالی و عوارض مرتبط با شانت. MS , آلزایمر، پارکینسون، افسردگی ، پرخاشگری ، اختلال شخصیتی ، اختلالات مرزی بیماری\u200Cهای های اعصاب کودکان، تشنج ، هیدرسفالی. ، تومورهای مغزی اطفال ، عوارض اعمال جراحی در اطفال کمردرد ، درد گردن ، بی حسی و ضعف اندام های فوقانی و تحتانی ،دیسک و تنگی های کانال نخاعی گردن و کمر انواع شکستگی های ستون فقرات و عوارض مرتبط با اعمال جراحی مغز و ستون فقرات.", 5f,0);
            Doctor doctor21 = createDoctor("فرشید", "نیکنام", String.format("0912%s", randomUtil.generateRandomNumber(7)), "neurology_resident", "w10.jpg", "دستیار تخصصی (رزیدنت) ارشد بیماری\u200Cهای مغز و اعصاب دانشگاه شهید بهشتی فارغ التحصیل پزشکی عمومی از دانشگاه علوم پزشکی تهران مشاوره در خصوص بیماری\u200Cهای سردرد، تشنج، سکته مغزی، پارکینسون، اختلالات حافظه، ام اس، نوروپاتی و دردهای سوزشی، سرگیجه حاد و مزمن، کمردرد، مشکلات اضطرابی و ... شاغل در سانتر کرونا و آماده ی پاسخ به سوالات و نگرانی های شما در رابطه با علایم و داروهاو انواع واکسن و ... در صورت در دسترس نبودن لطفا رزرو تلفنی بفرمایید تا در اسرع وقت با شما تماس بگیرم کاربران عزیز این سیستم جهت مشاوره می باشدو تجدید نسخه همکاران و در خواست تصویر برداری در صورت تشخیص پزشک صورت میگیرد.", 5f,0);
            doctorConsultationTypeService.create(doctor18, textConsultation, 500000L);
            doctorConsultationTypeService.create(doctor19, textConsultation, 800000L);
            doctorConsultationTypeService.create(doctor20, textConsultation, 400000L);
            doctorConsultationTypeService.create(doctor21, textConsultation, 600000L);
            doctorConsultationTypeService.create(doctor20, voiceConsultation, 650000L);
            doctorConsultationTypeService.create(doctor18, voiceConsultation, 650000L);
            doctorConsultationTypeService.create(doctor19, voiceConsultation, 700000L);
            doctorScheduleService.create(doctor18, DayOfWeek.WED, "08:00", "18:00");
            doctorScheduleService.create(doctor18, DayOfWeek.THU, "10:00", "20:00");
            doctorScheduleService.create(doctor19, DayOfWeek.SUN, "08:00", "14:00");
            doctorScheduleService.create(doctor18, DayOfWeek.MON, "09:00", "17:00");
            doctorScheduleService.create(doctor19, DayOfWeek.SAT, "10:00", "20:00");
            doctorScheduleService.create(doctor19, DayOfWeek.TUE, "09:00", "19:00");
            doctorScheduleService.create(doctor20, DayOfWeek.WED, "08:00", "18:00");
            doctorScheduleService.create(doctor21, DayOfWeek.THU, "10:00", "20:00");
            doctorScheduleService.create(doctor21, DayOfWeek.SUN, "08:00", "14:00");
            doctorScheduleService.create(doctor20, DayOfWeek.MON, "09:00", "17:00");
            doctorScheduleService.create(doctor20, DayOfWeek.SAT, "10:00", "20:00");
            doctorScheduleService.create(doctor21, DayOfWeek.TUE, "09:00", "19:00");

            Doctor doctor22 = createDoctor("مینا", "علوی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "orthopedist_expert", "w11.jpg", "جراح و متخصص ارتوپدی (استخوان و مفاصل ) ", 5f,0);
            Doctor doctor23 = createDoctor("مهدی", "کرامتی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "orthopedist_expert_assistant", "m14.jpg", " متخصص ارتوپدی فارغ التحصیل از دانشگاه شیراز جراح زانو لگن و مفاصل جراح شانه و دست و آرنج جراح پا و مج پا جراح ستتون فقرات فارغ التحصیل پزشکی عمومی از دانشگاه شیراز جراحی و آرتروسکوپی آسیب های ورزشی زانو و مچ پا بیماریهای مادرزادی و تغییر شکل و کوتاهی اندام اطفال و نوجوانان", 5f,0);
            Doctor doctor24 = createDoctor("سینا", "رضوی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "orthopedist_expert", "m20.png", "جراح و متخصص استخوان و مفاصل،دردگردن و کمر،تعویض مفصل،آسیب های ورزشی،شکستگی،عفونت اندام ها و مفاصل،آنومالی های مادرزادی اندام ها،انواع در رفتگی و کوتاهی اندام،کف پای صاف،قد کوتاه،انحراف زانو ها و سایر استخوان ها", 5f,0);
            Doctor doctor25 = createDoctor("پویا", "مرجانی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "orthopedist_resident", "m15.jpg", " متخصص و جراح ارتوپدی نفر اول قبولی ارتوپدی دانشگاه علوم پزشکی تهران در سال 96 فارغ التحصیل از دانشگاه علوم پزشکی تهران رتبه برتر برد تخصصی\n", 5f,0);
            doctorConsultationTypeService.create(doctor22, textConsultation, 400000L);
            doctorConsultationTypeService.create(doctor23, textConsultation, 400000L);
            doctorConsultationTypeService.create(doctor24, textConsultation, 500000L);
            doctorConsultationTypeService.create(doctor25, textConsultation, 700000L);
            doctorConsultationTypeService.create(doctor24, voiceConsultation, 650000L);
            doctorConsultationTypeService.create(doctor23, voiceConsultation, 750000L);
            doctorScheduleService.create(doctor22, DayOfWeek.WED, "08:00", "18:00");
            doctorScheduleService.create(doctor22, DayOfWeek.THU, "10:00", "20:00");
            doctorScheduleService.create(doctor23, DayOfWeek.SUN, "08:00", "14:00");
            doctorScheduleService.create(doctor22, DayOfWeek.MON, "09:00", "17:00");
            doctorScheduleService.create(doctor23, DayOfWeek.SAT, "10:00", "20:00");
            doctorScheduleService.create(doctor23, DayOfWeek.TUE, "09:00", "19:00");
            doctorScheduleService.create(doctor24, DayOfWeek.WED, "08:00", "18:00");
            doctorScheduleService.create(doctor25, DayOfWeek.THU, "10:00", "20:00");
            doctorScheduleService.create(doctor24, DayOfWeek.SUN, "08:00", "14:00");
            doctorScheduleService.create(doctor25, DayOfWeek.MON, "09:00", "17:00");
            doctorScheduleService.create(doctor24, DayOfWeek.SAT, "10:00", "20:00");
            doctorScheduleService.create(doctor25, DayOfWeek.TUE, "09:00", "19:00");

            Doctor doctor26 = createDoctor("نیما", "رضازاده", String.format("0912%s", randomUtil.generateRandomNumber(7)), "plastic_surgery_specialist", "m18.jpg", "رزیدنت سال اخر جراحی عمومی دانشگاه علوم پزشکی همدان بیمارستان فوق تخصصی بعثت حیطه تخصصی در زمینه: تروما،جراحی های داخل شکم به روش باز یا لاپاراسکوپیک،واریس و جراحی های عروقی،بیماری و جراحی های پستان،جراحی های پلاستیک (لیپوماتیک،ماموپلاستی،بلفاروپلاستی،تزریق بوتاکس و فیلر و لیفتینگ )", 5f,0);
            Doctor doctor27 = createDoctor("عرفان", "داننده", String.format("0912%s", randomUtil.generateRandomNumber(7)), "plastic_surgery_expert_assistant", "m16.jpg", " فوق تخصص جراحی پلاستیک و ترمیمی ▪️جراحی زیبایی بینی «رینوپلاستی» ▪️جراحی زیبایی سینه «ماموپلاستی و لیفت» ▪️جراحی زیبایی شکم «ابدومینوپلاستی» ▪️جراحی زیبایی پلک و ابرو «بلفاروپلاستی» ▪️جراحی زیبایی صورت «لیفت» ▪️تزریق فیلر و چربی جهت فرم دهی و زاویه سازی ▪️بوتاکس ▪️لیزر", 5f,0);
            Doctor doctor28 = createDoctor("شروین", "قمیشی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "plastic_surgery_fellowship", "m19.jpg", "بورد تخصصی جراحی عمومی دستیار فوق تخصصی جراحی پلاستیک و ترمیمی رتبه اول آزمون فوق تخصص جراحی پلاستیک و ترمیمی", 5f,0);
            doctorConsultationTypeService.create(doctor26, textConsultation, 800000L);
            doctorConsultationTypeService.create(doctor27, textConsultation, 500000L);
            doctorConsultationTypeService.create(doctor28, textConsultation, 700000L);
            doctorConsultationTypeService.create(doctor27, voiceConsultation, 600000L);
            doctorConsultationTypeService.create(doctor26, voiceConsultation, 600000L);
            doctorScheduleService.create(doctor26, DayOfWeek.WED, "08:00", "18:00");
            doctorScheduleService.create(doctor27, DayOfWeek.THU, "10:00", "20:00");
            doctorScheduleService.create(doctor26, DayOfWeek.SUN, "08:00", "14:00");
            doctorScheduleService.create(doctor28, DayOfWeek.MON, "09:00", "17:00");
            doctorScheduleService.create(doctor26, DayOfWeek.SAT, "10:00", "20:00");
            doctorScheduleService.create(doctor26, DayOfWeek.TUE, "09:00", "19:00");
            doctorScheduleService.create(doctor28, DayOfWeek.THU, "10:00", "20:00");
            doctorScheduleService.create(doctor27, DayOfWeek.SUN, "08:00", "14:00");
            doctorScheduleService.create(doctor27, DayOfWeek.MON, "09:00", "17:00");
            doctorScheduleService.create(doctor28, DayOfWeek.SAT, "10:00", "20:00");
            doctorScheduleService.create(doctor28, DayOfWeek.TUE, "09:00", "19:00");


            Doctor doctor29 = createDoctor("سعید", "ریگی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "dermatologist_expert_assistant", "m17.jpg", "دستیار تخصصی بیماری های پوست،مو،ناخن،مخاط دهان. ارائه مشاوره در زمینه زیبایی و جوانسازی ازجمله لیزر و جراحی های زیبایی ", 5f,0);
            Doctor doctor30 = createDoctor("سارا", "هاشمی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "dermatologist_expert", "w14.jpg", "سابقه طبابت در مراکز دولتی و خصوصی در حوزه بیماری های پوست و زیبایی عضو بنیاد ملی نخبگان جوان برتر استانی سال 1397 دانش آموخته دانشگاه علوم پزشکی ایران مدرس و مولف پزشکی", 5f,0);
            doctorConsultationTypeService.create(doctor29, textConsultation, 550000L);
            doctorConsultationTypeService.create(doctor30, textConsultation, 600000L);
            doctorConsultationTypeService.create(doctor29, voiceConsultation, 600000L);
            doctorConsultationTypeService.create(doctor30, voiceConsultation, 600000L);
            doctorScheduleService.create(doctor29, DayOfWeek.WED, "08:00", "18:00");
            doctorScheduleService.create(doctor29, DayOfWeek.THU, "10:00", "20:00");
            doctorScheduleService.create(doctor30, DayOfWeek.SUN, "08:00", "14:00");
            doctorScheduleService.create(doctor30, DayOfWeek.MON, "09:00", "17:00");
            doctorScheduleService.create(doctor30, DayOfWeek.SAT, "10:00", "20:00");
            doctorScheduleService.create(doctor29, DayOfWeek.TUE, "09:00", "19:00");

            Doctor doctor31 = createDoctor("ریحانه", "امامی", String.format("0912%s", randomUtil.generateRandomNumber(7)), "obstetricians_expert", "w12.jpg", "متخصص زنان زایمان نازایی و زیبایی بورد تخصصی از دانشگاه علوم\u200C پزشکی تهران مشاوره زنان و زایمان ؛باروری ؛اختلالات جنسی ؛بی اختیاری ادراری؛ درمان زگیل تناسلی ؛عفونت های زنان؛درمان خونریزی های نامنظم و مشکلات رحم و تخمدان", 5f,0);
            Doctor doctor32 = createDoctor("نگار", "نوبخت", String.format("0912%s", randomUtil.generateRandomNumber(7)), "obstetricians_expert_assistant", "w13.jpg", "تبه دو رقمی کنکور سراسری سال 1383 پزشکی عمومی: فارغ لتحصیل دانشگاه شهیدبهشتی سال 1390 تخصص زنان و زایمان: فارغ التحصیل دانشگاه مازندران سال 1396 عضو هیئت علمی دانشگاه بابل سال 1397-1400 هم اکنون دستیار فلوشیپ گاینکوانکولوژی (سرطان های زنان) دانشگاه شهیدبهشتی هستم.", 5f,0);
            doctorConsultationTypeService.create(doctor31, textConsultation, 500000L);
            doctorConsultationTypeService.create(doctor32, textConsultation, 800000L);
            doctorConsultationTypeService.create(doctor32, voiceConsultation, 600000L);
            doctorScheduleService.create(doctor31, DayOfWeek.WED, "08:00", "18:00");
            doctorScheduleService.create(doctor31, DayOfWeek.THU, "10:00", "20:00");
            doctorScheduleService.create(doctor32, DayOfWeek.SUN, "08:00", "14:00");
            doctorScheduleService.create(doctor32, DayOfWeek.MON, "09:00", "17:00");
            doctorScheduleService.create(doctor31, DayOfWeek.SAT, "10:00", "20:00");
            doctorScheduleService.create(doctor32, DayOfWeek.TUE, "09:00", "19:00");
            doctorScheduleService.create(doctor, DayOfWeek.WED, "08:00", "18:00");
        }
    }

    public Doctor createDoctor(String firstName, String lastName, String phone, String specialityName, String profileImageName, String description, Float rate, Integer consultationCount) throws Exception {
        User user = userService.createUser(phone, firstName, lastName);
        Speciality speciality = specialityService.findByName(specialityName);
        StorageFile profileImage = storageFileService.create(new ClassPathResource(String.format("static/profileImages/%s", profileImageName)).getInputStream(), StorageFileType.CATEGORY_IMAGE, "");


        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setDescription(description);
        doctor.setRate(rate);
        doctor.setConsultationCount(consultationCount);
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

    public boolean findBySpeciality(Speciality speciality) {
        return doctorRepository.existsBySpecialityAndIsDeletedFalse(speciality);
    }
}
