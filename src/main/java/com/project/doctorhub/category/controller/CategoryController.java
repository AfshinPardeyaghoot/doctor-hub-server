package com.project.doctorhub.category.controller;

import com.project.doctorhub.base.dto.HttpResponse;
import com.project.doctorhub.category.dto.CategoryCreateDTO;
import com.project.doctorhub.category.dto.CategoryDTOMapper;
import com.project.doctorhub.category.dto.CategoryGetDTO;
import com.project.doctorhub.category.dto.CategoryUpdateDTO;
import com.project.doctorhub.category.model.Category;
import com.project.doctorhub.category.service.CategoryService;
import com.project.doctorhub.doctor.dto.DoctorDTOMapper;
import com.project.doctorhub.doctor.dto.DoctorGetDTO;
import com.project.doctorhub.doctor.model.Doctor;
import com.project.doctorhub.util.ListUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.function.Predicate;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final ListUtil listUtil;
    private final CategoryService categoryService;
    private final DoctorDTOMapper doctorDTOMapper;
    private final CategoryDTOMapper categoryDTOMapper;


    @PostMapping
    public ResponseEntity<HttpResponse<CategoryGetDTO>> createCategory(
            @Valid @ModelAttribute CategoryCreateDTO categoryCreateDTO
    ) {
        Category category = categoryService.create(categoryCreateDTO);
        CategoryGetDTO categoryGetDTO = categoryDTOMapper.entityToGetDTO(category);
        return ResponseEntity.ok(new HttpResponse<>(categoryGetDTO));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<HttpResponse<CategoryGetDTO>> updateCategory(
            @PathVariable String uuid,
            @ModelAttribute CategoryUpdateDTO categoryUpdateDTO
    ) {
        Category category = categoryService.update(uuid, categoryUpdateDTO);
        CategoryGetDTO categoryGetDTO = categoryDTOMapper.entityToGetDTO(category);
        return ResponseEntity.ok(new HttpResponse<>(categoryGetDTO));
    }

    @GetMapping
    public ResponseEntity<HttpResponse<Page<CategoryGetDTO>>> getAllCategories(
            @PageableDefault Pageable pageable
    ) {
        Page<Category> categories = categoryService.findAllNotDeleted(pageable);
        Page<CategoryGetDTO> specialityGetDTOS = categories.map(categoryDTOMapper::entityToGetDTO);
        return ResponseEntity.ok(new HttpResponse<>(specialityGetDTOS));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<HttpResponse<CategoryGetDTO>> getCategoryById(
            @PathVariable String uuid
    ) {
        Category category = categoryService.findByUUIDNotDeleted(uuid);
        CategoryGetDTO categoryGetDTO = categoryDTOMapper.entityToGetDTO(category);
        return ResponseEntity.ok(new HttpResponse<>(categoryGetDTO));
    }


    @GetMapping("/{uuid}/doctors")
    public ResponseEntity<HttpResponse<Page<DoctorGetDTO>>> getAllCategoryDoctors(
            @PathVariable String uuid,
            @RequestParam(required = false) String name,
            @PageableDefault Pageable pageable
    ) {

        List<Doctor> doctors = categoryService.findAllCategoryDoctors(uuid);
        Predicate<DoctorGetDTO> filterName = (doctor -> name == null || (doctor.getName().contains(name)));
        return ResponseEntity.ok(new HttpResponse<>(listUtil.getPage(pageable, doctors.stream().map(doctorDTOMapper::entityToGetDTO).filter(filterName).toList())));
    }

}
