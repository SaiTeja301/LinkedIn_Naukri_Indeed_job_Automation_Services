package com.jobbot.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jobbot.dto.*;
import com.jobbot.entity.*;

@Component
public class EntityMapper {

    private static ModelMapper mapper;

    @Autowired
    public void setMapper(ModelMapper mapper) {
        EntityMapper.mapper = mapper;
    }

    public static AiResponseDto toDto(AiResponseEntity entity) {
        if (entity == null)
            return null;
        AiResponseDto dto = new AiResponseDto();
        dto.setId(entity.getId());
        dto.setPrompt(entity.getPrompt());
        dto.setResponse(entity.getResponse());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public static AiResponseEntity toEntity(AiResponseDto dto) {
        if (dto == null)
            return null;
        AiResponseEntity entity = new AiResponseEntity();
        entity.setId(dto.getId());
        entity.setPrompt(dto.getPrompt());
        entity.setResponse(dto.getResponse());
        // createdAt handled by entity
        return entity;
    }

    // public static UserDto toDto(UserEntity entity) {
    // if (entity == null)
    // return null;
    // UserDto dto = new UserDto();
    // dto.setId(entity.getId());
    // dto.setName(entity.getName());
    // dto.setEmail(entity.getEmail());
    // dto.setExperienceYears(entity.getExperienceYears());
    // dto.setPreferredRoles(entity.getPreferredRoles());
    // dto.setPreferredCompanies(entity.getPreferredCompanies());
    // dto.setRemote(entity.isRemote());
    // dto.setHybrid(entity.isHybrid());
    // return dto;
    // }

    public static UserDto toDto(UserEntity entity) {
        if (entity == null)
            return null;
        UserDto dto = mapper.map(entity, UserDto.class);
        return dto;
    }

    public static UserEntity toEntity(UserDto dto) {
        if (dto == null)
            return null;
        UserEntity entity = new UserEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setEncryptedPassword(dto.getEncryptedPassword());
        entity.setExperienceYears(dto.getExperienceYears());
        entity.setPreferredRoles(dto.getPreferredRoles());
        entity.setPreferredCompanies(dto.getPreferredCompanies());
        entity.setRemote(dto.isRemote());
        entity.setHybrid(dto.isHybrid());
        return entity;
    }

    public static JobDto toDto(JobEntity entity) {
        if (entity == null)
            return null;
        JobDto dto = new JobDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setCompany(entity.getCompany());
        dto.setLocation(entity.getLocation());
        dto.setJobUrl(entity.getJobUrl());
        dto.setPlatform(entity.getPlatform());
        dto.setJob_posted(entity.getJob_posted());
        dto.setJob_applyed_count_status(entity.getJob_applyed_count_status());
        dto.setDescription(entity.getDescription());
        dto.setApplied(entity.isApplied());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public static JobEntity toEntity(JobDto dto) {
        if (dto == null)
            return null;
        JobEntity entity = new JobEntity();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setCompany(dto.getCompany());
        entity.setLocation(dto.getLocation());
        entity.setJobUrl(dto.getJobUrl());
        entity.setPlatform(dto.getPlatform());
        entity.setJob_posted(dto.getJob_posted());
        entity.setJob_applyed_count_status(dto.getJob_applyed_count_status());
        entity.setDescription(dto.getDescription());
        entity.setApplied(dto.isApplied());
        // createdAt is handled by entity default
        return entity;
    }

    public static ResumeDto toDto(ResumeEntity entity) {
        if (entity == null)
            return null;
        ResumeDto dto = new ResumeDto();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);
        dto.setContent(entity.getContent());
        dto.setVersion(entity.getVersion());
        dto.setUploadedAt(entity.getUploadedAt());
        return dto;
    }

    public static JobApplicationDto toDto(JobApplicationEntity entity) {
        if (entity == null)
            return null;
        JobApplicationDto dto = new JobApplicationDto();
        dto.setId(entity.getId());
        dto.setJobId(entity.getJob() != null ? entity.getJob().getId() : null);
        dto.setStatus(entity.getStatus());
        dto.setAppliedAt(entity.getAppliedAt());
        return dto;
    }
}
