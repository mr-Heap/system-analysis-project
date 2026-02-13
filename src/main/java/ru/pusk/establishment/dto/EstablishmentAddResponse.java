package ru.pusk.establishment.dto;


import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
public record EstablishmentAddResponse(Long establishmentId) {

}
