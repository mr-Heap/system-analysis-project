package ru.pusk.establishment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import ru.pusk.establishment.dto.EstablishmentResponseDto;
import ru.pusk.establishment.entity.Establishment;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface EstablishmentMapper {

  EstablishmentResponseDto toEstablishmentDto(Establishment establishment);

  List<EstablishmentResponseDto> toEstablishmentDtoList(List<Establishment> establishment);


  Establishment toEstablishment(EstablishmentResponseDto establishment);

  List<Establishment> toEstablishmentList(List<EstablishmentResponseDto> establishment);

}
