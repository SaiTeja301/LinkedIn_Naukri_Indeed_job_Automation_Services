package com.jobbot.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private Integer experienceYears;
    private String encryptedPassword;
    private String preferredRoles;
    private String preferredCompanies;
    private boolean remote;
    private boolean hybrid;
}
