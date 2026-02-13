package ru.pusk.personalinfo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.Named;
import ru.pusk.personalinfo.dto.PersonalInfoDto;
import ru.pusk.personalinfo.entity.IndividualInfo;
import ru.pusk.personalinfo.enums.UserLegalType;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface PersonalInfoMapper {
  @Mapping(target = "email", source = "source.contactEmail")
  @Mapping(target = "type", source = "source.type", qualifiedByName = "fromUserLegalType")
  PersonalInfoDto toPersonalInfoDto(IndividualInfo source);
  @Named("fromUserLegalType")
  default String fromUserLegalType(UserLegalType type) {
    return type != null ? type.getValue() : null;
  }
  @Named("toUserLegalType")
  default UserLegalType toUserLegalType(String type) {
    return type != null ? UserLegalType.fromString(type) : null;
  }
}
