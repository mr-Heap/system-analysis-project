package ru.pusk.legalinfo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import ru.pusk.legalinfo.dto.LegalInfoDto;
import ru.pusk.legalinfo.entity.OrganizationInfo;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface LegalInfoMapper {
  LegalInfoDto toLegalInfoDto(OrganizationInfo organizationInfo);
  List<LegalInfoDto> toLegalInfoDtoList(List<OrganizationInfo> organizationInfos);

  OrganizationInfo toOrganizationInfoEntity(LegalInfoDto dto);
}
