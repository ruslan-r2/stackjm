package com.javamentor.qa.platform.webapp.converters;

import com.javamentor.qa.platform.models.dto.UserRegistrationDto;
import com.javamentor.qa.platform.models.entity.user.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class UserConverter {

    @Mapping(target = "fullName", expression = "java(userRegistrationDto.getFirstName() + \" \" + userRegistrationDto.getLastName())")
    public abstract User userRegistrationDtoToUser(UserRegistrationDto userRegistrationDto);

    @InheritInverseConfiguration
    public abstract UserRegistrationDto userToUserRegistrationDto(User user);

}
