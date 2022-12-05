package com.project.doctorhub.speciality.service;

import com.project.doctorhub.base.exception.HttpException;
import com.project.doctorhub.base.exception.NotFoundException;
import com.project.doctorhub.base.service.AbstractCrudService;
import com.project.doctorhub.speciality.dto.SpecialityUpdateDTO;
import com.project.doctorhub.storageFile.model.StorageFile;
import com.project.doctorhub.storageFile.model.StorageFileType;
import com.project.doctorhub.storageFile.service.StorageFileService;
import com.project.doctorhub.speciality.dto.SpecialityCreateDTO;
import com.project.doctorhub.speciality.model.Speciality;
import com.project.doctorhub.speciality.repository.SpecialityRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.InputStream;

@Service
public class SpecialityService
        extends AbstractCrudService<Speciality, Long, SpecialityRepository> {

    private final StorageFileService storageFileService;
    private final SpecialityRepository specialityRepository;

    public SpecialityService(
            SpecialityRepository abstractRepository,
            StorageFileService storageFileService
    ) {
        super(abstractRepository);
        this.specialityRepository = abstractRepository;
        this.storageFileService = storageFileService;
    }

    public Speciality findByName(String name) {
        return specialityRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new NotFoundException("speciality not found"));
    }

    @Transactional
    public Speciality create(SpecialityCreateDTO specialityCreateDTO) {
        if (specialityRepository.findByNameIgnoreCase(specialityCreateDTO.getName()).isPresent()) {
            throw new HttpException("تخصص وارد شده در حال حاظر موجود است!", HttpStatus.BAD_REQUEST);
        }

        StorageFile specialityImage = storageFileService.create(
                specialityCreateDTO.getImage(),
                StorageFileType.SPECIALITY_IMAGE
        );
        return create(specialityCreateDTO, specialityImage);
    }

    private Speciality create(SpecialityCreateDTO specialityCreateDTO, StorageFile specialityImage) {
        Speciality speciality = new Speciality();
        speciality.setImage(specialityImage);
        speciality.setName(specialityCreateDTO.getName().toLowerCase());
        speciality.setTitle(specialityCreateDTO.getTitle());
        speciality.setDescription(specialityCreateDTO.getDescription());
        speciality.setFullTitle(specialityCreateDTO.getFullTitle());
        speciality.setIsDeleted(false);
        return save(speciality);
    }

    public Speciality update(String uuid, SpecialityUpdateDTO specialityUpdateDTO) {
        Speciality speciality = findByUUIDNotDeleted(uuid);

        if (specialityUpdateDTO.getTitle() != null)
            speciality.setTitle(specialityUpdateDTO.getTitle());

        if (specialityUpdateDTO.getFullTitle() != null)
            speciality.setFullTitle(specialityUpdateDTO.getFullTitle());

        if (specialityUpdateDTO.getDescription() != null)
            speciality.setDescription(specialityUpdateDTO.getDescription());

        if (specialityUpdateDTO.getImage() != null) {
            StorageFile specialityImage = storageFileService.create(
                    specialityUpdateDTO.getImage(),
                    StorageFileType.SPECIALITY_IMAGE
            );
            speciality.setImage(specialityImage);
        }

        return save(speciality);
    }

    public void seeder() throws Exception {
        createIfNotExist("nutrition", "تغذیه", "کارشناس تغذیه و تناسب اندام", "رژیم چاقی و لاغری، کمبود اشتها و تغذیه سالم", storageFileService.create(new ClassPathResource("static/icon/diet.png").getInputStream(), StorageFileType.SPECIALITY_IMAGE, "nutrition"));
        createIfNotExist("child", "کودکان", "مشاوره با متخصص کودکان", "بیماری ها، مراقبت، رشد و تغذیه", storageFileService.create(new ClassPathResource("static/icon/baby.png").getInputStream(), StorageFileType.SPECIALITY_IMAGE, "child"));
        createIfNotExist("dental", "دنداپزشک", "مشاوره با متخصص دندانپزشک", "مراقبت و بیماری های لثه، دهان و دندان", storageFileService.create(new ClassPathResource("static/icon/dental.png").getInputStream(), StorageFileType.SPECIALITY_IMAGE, "dental"));
        createIfNotExist("ophthalmology", "چشم پزشک", "مشاوره با متخصص چشم پزشک", "مراقبت و بیماری های مثل عفونت", storageFileService.create(new ClassPathResource("static/icon/eye.png").getInputStream(), StorageFileType.SPECIALITY_IMAGE, "ophthalmology"));
        createIfNotExist("general", "عمومی", "مشاوره با پزشک عمومی", "پیشگیری و مراقبت از تمامی بیماری های خفیف", storageFileService.create(new ClassPathResource("static/icon/general.png").getInputStream(), StorageFileType.SPECIALITY_IMAGE, "general"));
        createIfNotExist("otorhinolaryngology", "گوش،حلق،بینی", "متخصص گوش، حلق، بینی", "سینوزیت، مشکلات گوش، بینی و عفونت سر و گردن", storageFileService.create(new ClassPathResource("static/icon/gosh.png").getInputStream(), StorageFileType.SPECIALITY_IMAGE, "otorhinolaryngology"));
        createIfNotExist("cardiovascular", "قلب و عروق", "متخصص قلب، عروق و فشارخون", "درد قفسه سینه، فشارخون، چربی خون", storageFileService.create(new ClassPathResource("static/icon/heart.png").getInputStream(), StorageFileType.SPECIALITY_IMAGE, "cardiovascular"));
        createIfNotExist("surgery", "جراحی عمومی", "مشاوره با متخصص جراحی", "مراقبت ها و جراحی هایی مثل فتق، دستگاه گوارش", storageFileService.create(new ClassPathResource("static/icon/jarahi.png").getInputStream(), StorageFileType.SPECIALITY_IMAGE, "surgery"));
        createIfNotExist("neurology", "مغز و اعصاب", "مشاوره با متخصص مغز و اعصاب", "بیماری های مثل سردرد، ام اس، بی حسی", storageFileService.create(new ClassPathResource("static/icon/maghz.png").getInputStream(), StorageFileType.SPECIALITY_IMAGE, "neurology"));
        createIfNotExist("orthopedist", "ارتوپد", "مشاوره با متخصص ارتوپدی", "بیماری های مثل درد مفاصل و استخوان ها ", storageFileService.create(new ClassPathResource("static/icon/orthoped.png").getInputStream(), StorageFileType.SPECIALITY_IMAGE, "orthopedist"));
        createIfNotExist("plastic_surgery", "جراحی پلاستیک و زیبایی", "متخصص جراحی پلاستیک و زیبایی", "جراحی زیبایی، لیفت صورت و تزریق ژل", storageFileService.create(new ClassPathResource("static/icon/plastic.png").getInputStream(), StorageFileType.SPECIALITY_IMAGE, "plastic_surgery"));
        createIfNotExist("skin_and_hair", "پوست و مو", "مشاوره با متخصص سلامت پوست و مو", "جوش، خارش و عفونت پوست،ریزش مو ، شوره سز", storageFileService.create(new ClassPathResource("static/icon/post-and-mo.png").getInputStream(), StorageFileType.SPECIALITY_IMAGE, "skin_and_hair"));
        createIfNotExist("psychiatrist", "روانپزشک", "مشاوره با متخصص روانپزشک", "درمان و پیشگیری مشکلات روحی، عاطفی و رفتاری", storageFileService.create(new ClassPathResource("static/icon/ravanpezeshk.png").getInputStream(), StorageFileType.SPECIALITY_IMAGE, "psychiatrist"));
        createIfNotExist("infectious", "عفونی", "مشاوره آنلاین کرونا و بیماری های عفونی", "پیشگیری، تشخیص، علائم", storageFileService.create(new ClassPathResource("static/icon/virus.png").getInputStream(), StorageFileType.SPECIALITY_IMAGE, "infectious"));
        createIfNotExist("obstetricians", "زنان و زایمان", "مشاوره با متخصص زنان و زایمان", "بارداری، جلوگیری و بیماری های مثل عفونت ", storageFileService.create(new ClassPathResource("static/icon/pregnant.png").getInputStream(), StorageFileType.SPECIALITY_IMAGE, "obstetricians"));
    }

    private void createIfNotExist(String name, String title, String fullTitle, String description, StorageFile image) {
        if (specialityRepository.findByNameIgnoreCase(name).isEmpty()) {

            Speciality speciality = new Speciality();
            speciality.setName(name);
            speciality.setTitle(title);
            speciality.setFullTitle(fullTitle);
            speciality.setDescription(description);
            speciality.setImage(image);
            speciality.setIsDeleted(false);

            save(speciality);
        }
    }
}
