package com.project.doctorhub.speciality.service;

import com.project.doctorhub.base.exception.HttpException;
import com.project.doctorhub.base.exception.NotFoundException;
import com.project.doctorhub.base.service.AbstractCrudService;
import com.project.doctorhub.speciality.dto.SpecialityCreateDTO;
import com.project.doctorhub.speciality.dto.SpecialityUpdateDTO;
import com.project.doctorhub.speciality.model.Speciality;
import com.project.doctorhub.speciality.repository.SpecialityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class SpecialityService
        extends AbstractCrudService<Speciality, Long, SpecialityRepository> {
    private final SpecialityRepository specialityRepository;

    public SpecialityService(
            SpecialityRepository abstractRepository
    ) {
        super(abstractRepository);
        this.specialityRepository = abstractRepository;
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

        Speciality speciality = new Speciality();
        speciality.setName(specialityCreateDTO.getName().toLowerCase());
        speciality.setTitle(specialityCreateDTO.getTitle());
        speciality.setIsDeleted(false);
        return save(speciality);
    }

    public List<Speciality> findAllByUUIDs(List<String> uuids) {
        return specialityRepository.findAllByUUIDInAndIsDeletedFalse(uuids);
    }

    public Speciality update(String uuid, SpecialityUpdateDTO specialityUpdateDTO) {
        Speciality speciality = findByUUIDNotDeleted(uuid);

        if (specialityUpdateDTO.getTitle() != null)
            speciality.setTitle(specialityUpdateDTO.getTitle());

        return save(speciality);
    }

    public Speciality update(String uuid, SpecialityCreateDTO createDTO) {
        Speciality speciality = findByUUIDNotDeleted(uuid);
        if (!Objects.equals(speciality.getName(), createDTO.getName()) && specialityRepository.findByNameIgnoreCase(createDTO.getName()).isPresent())
            throw new HttpException("نام وارد شده تکراری است!", HttpStatus.BAD_REQUEST);

        if (createDTO.getTitle() != null)
            speciality.setTitle(createDTO.getTitle());

        if (createDTO.getName() != null)
            speciality.setName(createDTO.getName().toLowerCase());

        return save(speciality);
    }


    public void seeder() {
        create("nutrition_senior", "کارشناس ارشد تغذیه");
        create("nutrition", "کارشناس تغذیه");

        create("child_expert", "متخصص بیماری های کودکان");
        create("child_expert_assistant", "دستیار تخصصی  بیماری های کودکان");
        create("child_specialist", "فوق تخصص کودکان");

        create("dentist", "دندانپزشک ");
        create("gum_surgery_expert", "متخصص جراحی لثه");

        create("ophthalmology_expert", "متخصص چشم پزشکی");
        create("optometry_expert", "کارشناس بینایی سنجی");
        create("ophthalmology_expert_assistant", "دستیار تخصصی چشم پزشکی");

        create("general", "پزشک عمومی");

        create("otorhinolaryngology_expert", "متخصص بیماری های گوش،حلق،بینی");
        create("otorhinolaryngology_expert_assistant", "دستیار تخصصی گوش،حلق،بینی");

        create("cardiovascular_expert", "متخصص بیماری های قلب و عروق");
        create("cardiovascular_specialist", "فوق تخصص بیماری های قلب و عروق");
        create("cardiovascular_expert_assistant", "دستیار تخصصی بیماری های قلب و عروق");

        create("general_surgeon_expert", "متخصص جراح عمومی");
        create("general_surgeon_expert_assistant", "دستیار تخصصی جراح عمومی");

        create("neurology_expert", "متخصص بیماری های مغز و اعصاب");
        create("neurology_specialist", "فوق تخصص بیماری های مغز و اعصاب");
        create("neurology_expert_assistant", "دستیار تخصصی جراجی مغز و اعصاب");
        create("neurology_resident", "رزیدنت مغز و اعصاب");

        create("orthopedist_expert", "متخصص ارتوپدی و جراحی استخوان و مفاصل");
        create("orthopedist_expert_assistant", "دستیار تخصصی ارتوپدی و جراحی استخوان و مفاصل");
        create("orthopedist_expert", "متخصص جراح استخوان و مفاصل");
        create("orthopedist_resident", "رزیدنت ارتوپدی");

        create("plastic_surgery_specialist", "فوق تخصص جراحی پلاستیک، ترمیمی و سوختگی");
        create("plastic_surgery_expert_assistant", "دستیار فوق تخصص جراحی پلاستیک، ترمیمی و سوختگی");
        create("plastic_surgery_fellowship", "فلوشیپ جراحی پلاستیک و ترمیمی چشم");

        create("dermatologist_expert_assistant", "دستیار تخصصی بیماری های پوست و مو");
        create("dermatologist_expert", "متخصص بیماری های پوست و مو");

        create("obstetricians_expert", "متخصص زنان و زایمان");
        create("obstetricians_expert_assistant", "دستیار تخصصی زنان و زایمان");
    }

    private void create(String name, String title) {
        if (specialityRepository.findByNameIgnoreCase(name).isEmpty()) {
            Speciality speciality = new Speciality();
            speciality.setName(name);
            speciality.setTitle(title);
            speciality.setIsDeleted(false);
            save(speciality);
        }
    }


    public Page<Speciality> findAllBySearchNotDeleted(String search, Pageable pageable) {
        if (search != null)
            return specialityRepository.findAllBySearchNotDeleted(search, pageable);
        else return findAllNotDeleted(pageable);
    }

}
