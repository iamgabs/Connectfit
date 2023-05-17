package com.example.connectfit.models.entities;

import com.example.connectfit.enums.ProfessionalGroupEnum;
import com.example.connectfit.enums.UserGroupEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {
    private String name;
    private String password;
    private String email;
    private UserGroupEnum userGroupEnum;
    private ProfessionalGroupEnum professionalGroupEnum;
}
