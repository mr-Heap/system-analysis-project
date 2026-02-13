package ru.pusk.personalinfo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.pusk.auth.data.Authority;
import ru.pusk.personalinfo.dto.UserDto;
import ru.pusk.personalinfo.entity.IndividualInfo;

import java.util.List;
import java.util.Optional;

@Repository
public interface IndividualInfoRepository extends JpaRepository<IndividualInfo, Long> {

  Optional<IndividualInfo> findByUserId(Long userId);

  @Query(value = """
      select * from user_authority ua
      join individual_info ii on ua.user_id=ii.user_id
      where ua.authority='ROLE_CLIENT_MANAGER'
      """, nativeQuery = true)
  List<IndividualInfo> findAllByAuthority(Authority authority);

  @Query(value = """
      SELECT u.user_id                                                   as id,
             u.registered_at                                             as registrationDate,
             COALESCE(STRING_AGG(DISTINCT t.name, ', '), 'Нет подписок') as tariffs,
             CASE
                 WHEN MAX(CASE WHEN s.active THEN 1 ELSE 0 END) = 1 THEN 'Активна'
                 WHEN COUNT(s.id) > 0 THEN 'Неактивна с ' || TO_CHAR(MAX(s.end_date), 'DD.MM.YYYY')
                 ELSE 'Нет подписок'
                 END                                                     as subscriptionStatus,
             '+' || au.phone                                             as login,
             concat(i.first_name, ' ', i.last_name, ' ', i.patronymic) as fio,
             case when da is null then 'active' else 'blocked' end as status,
             ua.authority as role
      FROM users u
               JOIN individual_info i on i.user_id = u.user_id
               LEFT JOIN subscription s ON s.user_id = u.user_id
               LEFT JOIN tariff t ON t.id = s.tariff_id
               LEFT JOIN auth_phone au ON au.user_id = u.user_id
               LEFT JOIN deleted_account da on da.user_id = u.user_id
               LEFT JOIN user_authority ua on ua.user_id = u.user_id
      where (:query is null or
             concat(i.first_name, ' ', i.last_name, ' ', i.patronymic) ilike '%' || :query || '%' or
             concat('+', au.phone) ilike '%' || :query || '%')
      GROUP BY u.user_id, u.registered_at, au.phone, ua.authority, i.first_name, i.last_name, i.patronymic, da.id, da.user_id
      order by case when :sortBy = 'login' then au.phone end,
               case when :sortBy = 'role' then ua.authority end,
               case when :sortBy = 'registration-date' then u.registered_at end desc,
               case when :sortBy = 'status' then case when da is null then 1 else 0 end end desc,
               case when :sortBy = 'subscription' then
                        CASE
                            WHEN MAX(CASE WHEN s.active THEN 1 ELSE 0 END) = 1 THEN 1
                            WHEN COUNT(s.id) > 0 THEN 0
                            ELSE -1
                            END
                   end desc
            """, nativeQuery = true)
  Page<UserDto> findAllUsers(String query, String sortBy, Pageable pageable);
}
