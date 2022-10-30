package com.project.doctorhub.auth.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@RequiredArgsConstructor
public class SendVerificationCodeDTO {

    @NotNull(message = "لطفا شماره تلفن را وارد کنید")
    private String phone;

}
