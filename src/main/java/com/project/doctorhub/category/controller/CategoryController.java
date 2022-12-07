package com.project.doctorhub.category.controller;

import com.project.doctorhub.base.dto.HttpResponse;
import com.project.doctorhub.category.dto.CategoryCreateDTO;
import com.project.doctorhub.category.dto.CategoryDTOMapper;
import com.project.doctorhub.category.dto.CategoryGetDTO;
import com.project.doctorhub.category.dto.CategoryUpdateDTO;
import com.project.doctorhub.category.model.Category;
import com.project.doctorhub.category.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;
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

}
