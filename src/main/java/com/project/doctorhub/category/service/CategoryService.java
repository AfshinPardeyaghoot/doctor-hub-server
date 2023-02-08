package com.project.doctorhub.category.service;

import com.project.doctorhub.base.exception.HttpException;
import com.project.doctorhub.base.exception.NotFoundException;
import com.project.doctorhub.base.service.AbstractCrudService;
import com.project.doctorhub.category.dto.CategoryCreateDTO;
import com.project.doctorhub.category.dto.CategoryUpdateDTO;
import com.project.doctorhub.category.model.Category;
import com.project.doctorhub.category.repository.CategoryRepository;
import com.project.doctorhub.doctor.model.Doctor;
import com.project.doctorhub.speciality.model.SpecialityCategory;
import com.project.doctorhub.speciality.service.SpecialityCategoryService;
import com.project.doctorhub.storageFile.model.StorageFile;
import com.project.doctorhub.storageFile.model.StorageFileType;
import com.project.doctorhub.storageFile.service.StorageFileService;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class CategoryService
        extends AbstractCrudService<Category, Long, CategoryRepository> {

    private final StorageFileService storageFileService;
    private final CategoryRepository categoryRepository;
    private final SpecialityCategoryService specialityCategoryService;

    public CategoryService(
            CategoryRepository abstractRepository,
            StorageFileService storageFileService,
            @Lazy SpecialityCategoryService specialityCategoryService
    ) {
        super(abstractRepository);
        this.categoryRepository = abstractRepository;
        this.storageFileService = storageFileService;
        this.specialityCategoryService = specialityCategoryService;
    }


    public Category findByName(String name) {
        return categoryRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new NotFoundException("speciality category not found"));
    }


    @Transactional(readOnly = true)
    public Page<Category> findAllBySearchAndIsDeletedFalse(String search, Pageable pageable) {
        if (search != null)
            return categoryRepository.findAllBySearchAndIsDeletedFalse(search, pageable);
        return findAllNotDeleted(pageable);
    }

    @Transactional
    public Category create(CategoryCreateDTO categoryCreateDTO) {
        if (categoryRepository.findByNameIgnoreCase(categoryCreateDTO.getName()).isPresent()) {
            throw new HttpException("تخصص وارد شده در حال حاظر موجود است!", HttpStatus.BAD_REQUEST);
        }

        StorageFile categoryImage = storageFileService.create(
                categoryCreateDTO.getImage(),
                StorageFileType.CATEGORY_IMAGE
        );

        Category category = create(categoryCreateDTO, categoryImage);
        createCategorySpecialities(category, categoryCreateDTO.getSpecialityIds());
        return category;
    }

    private void createCategorySpecialities(Category category, List<String> specialityIds) {
        List<SpecialityCategory> specialityCategories = specialityCategoryService.createAll(category, specialityIds);
    }

    private Category create(CategoryCreateDTO categoryCreateDTO, StorageFile specialityCategoryImage) {
        Category category = new Category();
        category.setImage(specialityCategoryImage);
        category.setName(categoryCreateDTO.getName().toLowerCase());
        category.setTitle(categoryCreateDTO.getTitle());
        category.setDescription(categoryCreateDTO.getDescription());
        category.setFullTitle(categoryCreateDTO.getFullTitle());
        category.setIsDeleted(false);
        return save(category);
    }

    public Category update(String uuid, CategoryUpdateDTO categoryUpdateDTO) {
        Category category = findByUUIDNotDeleted(uuid);

        if (categoryUpdateDTO.getTitle() != null)
            category.setTitle(categoryUpdateDTO.getTitle());

        if (categoryUpdateDTO.getFullTitle() != null)
            category.setFullTitle(categoryUpdateDTO.getFullTitle());

        if (categoryUpdateDTO.getDescription() != null)
            category.setDescription(categoryUpdateDTO.getDescription());

        if (categoryUpdateDTO.getImage() != null) {
            StorageFile specialityImage = storageFileService.create(
                    categoryUpdateDTO.getImage(),
                    StorageFileType.CATEGORY_IMAGE
            );
            category.setImage(specialityImage);
        }

        if (categoryUpdateDTO.getSpecialityIds() != null) {
            updateCategorySpecialities(category, categoryUpdateDTO.getSpecialityIds());
        }

        return save(category);
    }

    private void updateCategorySpecialities(Category category, List<String> specialityIds) {
        specialityCategoryService.updateCategorySpecialities(category, specialityIds);
    }

    public void seeder() throws Exception {
        createIfNotExist("nutrition", "تغذیه", "کارشناس تغذیه و تناسب اندام", "رژیم چاقی و لاغری، کمبود اشتها و تغذیه سالم", storageFileService.create(new ClassPathResource("static/icon/diet.png").getInputStream(), StorageFileType.CATEGORY_IMAGE, "nutrition"));
        createIfNotExist("child", "کودکان", "متخصص کودکان", "بیماری ها، مراقبت، رشد و تغذیه", storageFileService.create(new ClassPathResource("static/icon/baby.png").getInputStream(), StorageFileType.CATEGORY_IMAGE, "child"));
        createIfNotExist("dental", "دنداپزشک", "متخصص دندانپزشک", "مراقبت و بیماری های لثه، دهان و دندان", storageFileService.create(new ClassPathResource("static/icon/dental.png").getInputStream(), StorageFileType.CATEGORY_IMAGE, "dental"));
        createIfNotExist("ophthalmology", "چشم پزشک", "متخصص چشم پزشک", "مراقبت و بیماری های مثل عفونت", storageFileService.create(new ClassPathResource("static/icon/eye.png").getInputStream(), StorageFileType.CATEGORY_IMAGE, "ophthalmology"));
        createIfNotExist("general", "عمومی", "پزشک عمومی", "پیشگیری و مراقبت از تمامی بیماری های خفیف", storageFileService.create(new ClassPathResource("static/icon/general.png").getInputStream(), StorageFileType.CATEGORY_IMAGE, "general"));
        createIfNotExist("otorhinolaryngology", "گوش،حلق،بینی", "متخصص گوش، حلق، بینی", "سینوزیت، مشکلات گوش، بینی و عفونت سر و گردن", storageFileService.create(new ClassPathResource("static/icon/gosh.png").getInputStream(), StorageFileType.CATEGORY_IMAGE, "otorhinolaryngology"));
        createIfNotExist("cardiovascular", "قلب و عروق", "متخصص قلب، عروق و فشارخون", "درد قفسه سینه، فشارخون، چربی خون", storageFileService.create(new ClassPathResource("static/icon/heart.png").getInputStream(), StorageFileType.CATEGORY_IMAGE, "cardiovascular"));
        createIfNotExist("general_surgeon", "جراحی عمومی", "متخصص جراحی", "مراقبت ها و جراحی هایی مثل فتق، دستگاه گوارش", storageFileService.create(new ClassPathResource("static/icon/jarahi.png").getInputStream(), StorageFileType.CATEGORY_IMAGE, "surgery"));
        createIfNotExist("neurology", "مغز و اعصاب", "متخصص مغز و اعصاب", "بیماری های مثل سردرد، ام اس، بی حسی", storageFileService.create(new ClassPathResource("static/icon/maghz.png").getInputStream(), StorageFileType.CATEGORY_IMAGE, "neurology"));
        createIfNotExist("orthopedist", "ارتوپد", "متخصص ارتوپدی", "بیماری های مثل درد مفاصل و استخوان ها ", storageFileService.create(new ClassPathResource("static/icon/orthoped.png").getInputStream(), StorageFileType.CATEGORY_IMAGE, "orthopedist"));
        createIfNotExist("plastic_surgery", "جراحی پلاستیک و زیبایی", "متخصص جراحی پلاستیک و زیبایی", "جراحی زیبایی، لیفت صورت و تزریق ژل", storageFileService.create(new ClassPathResource("static/icon/plastic.png").getInputStream(), StorageFileType.CATEGORY_IMAGE, "plastic_surgery"));
        createIfNotExist("dermatologist", "پوست و مو", "متخصص سلامت پوست و مو", "جوش، خارش و عفونت پوست،ریزش مو ، شوره سز", storageFileService.create(new ClassPathResource("static/icon/post-and-mo.png").getInputStream(), StorageFileType.CATEGORY_IMAGE, "dermatologist"));
        createIfNotExist("psychiatrist", "روانپزشک", "متخصص روانپزشک", "درمان و پیشگیری مشکلات روحی، عاطفی و رفتاری", storageFileService.create(new ClassPathResource("static/icon/ravanpezeshk.png").getInputStream(), StorageFileType.CATEGORY_IMAGE, "psychiatrist"));
        createIfNotExist("obstetricians", "زنان و زایمان", "متخصص زنان و زایمان", "بارداری، جلوگیری و بیماری های مثل عفونت ", storageFileService.create(new ClassPathResource("static/icon/pregnant.png").getInputStream(), StorageFileType.CATEGORY_IMAGE, "obstetricians"));
    }

    private void createIfNotExist(String name, String title, String fullTitle, String description, StorageFile image) {
        if (categoryRepository.findByNameIgnoreCase(name).isEmpty()) {

            Category category = new Category();
            category.setName(name);
            category.setTitle(title);
            category.setFullTitle(fullTitle);
            category.setDescription(description);
            category.setImage(image);
            category.setIsDeleted(false);

            save(category);
        }
    }

    public void delete(String uuid){
        Category category = findByUUIDNotDeleted(uuid);
        safeDelete(category);
    }

    public List<Doctor> findAllCategoryDoctors(String uuid) {
        return categoryRepository.findAllCategoryDoctors(uuid);
    }
}
