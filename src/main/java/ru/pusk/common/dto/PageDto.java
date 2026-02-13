package ru.pusk.common.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Jacksonized
public class PageDto {
  private List<?> data;
  private Integer totalPages;
  private Long totalCount;
}
