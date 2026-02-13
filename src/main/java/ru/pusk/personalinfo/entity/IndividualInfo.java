package ru.pusk.personalinfo.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.pusk.personalinfo.enums.UserLegalType;

@Getter
@Setter
@Builder
@Entity(name = "IndividualInfo")
@NoArgsConstructor
@AllArgsConstructor
public class IndividualInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String lastName;
    private String firstName;
    private String patronymic;
    @Enumerated(EnumType.STRING)
    private UserLegalType type;
    private String phone;
    private String contactEmail;
    private Long userId;
    private String avatarFileId;
}
