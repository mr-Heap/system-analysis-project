package ru.pusk.legalinfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pusk.legalinfo.entity.OrganizationInfo;
import ru.pusk.personalinfo.enums.UserLegalType;

import java.util.List;

@Repository
public interface OrganizationInfoRepository extends JpaRepository<OrganizationInfo, Long> {
    List<OrganizationInfo> findAllByUserIdAndType(Long userId, UserLegalType type);
}
