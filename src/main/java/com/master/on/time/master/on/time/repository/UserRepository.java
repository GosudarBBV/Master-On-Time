package com.master.on.time.master.on.time.repository;

import com.master.on.time.master.on.time.model.RoleName;
import com.master.on.time.master.on.time.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email = :email")
    Optional<User> findByEmailWithRoles(@Param("email") String email);

    @Query("""
                select distinct u from User u
                join u.roles r
                join Category c on c.specialist = u
                join CategoryItem ci on ci.category = c
                where r.name = :roleName
                  and u.visible = true
                  and (:serviceName is null or lower(ci.name)
                  like lower(concat('%', :serviceName, '%')))
                  and (:location is null or lower(u.address.city)
                  like lower(concat('%', :location, '%')))
            """)
    List<User> findProvidersByServiceAndLocation(
            @Param("roleName") RoleName roleName,
            @Param("serviceName") String serviceName,
            @Param("location") String location
    );

    @Query("""
        select distinct u from User u
        join u.roles r
        join Category c on c.specialist = u
        join CategoryItem ci on ci.category = c
        where r.name = :roleName
          and u.visible = true
          and (:serviceName is null or lower(ci.name) like lower(concat('%', :serviceName, '%')))
          and (:firstName is null or lower(u.firstName) like lower(concat('%', :firstName, '%')))
          and (:city is null or lower(u.address.city) like lower(concat('%', :city, '%')))
          and (:categoryNames is null or c.name in :categoryNames)
          and (:minExperience is null or u.specialistProfile.experience >= :minExperience)
          and (:minRating is null or u.specialistProfile.rating >= :minRating)
            """)
    List<User> searchSpecialists(
            @Param("roleName") RoleName roleName,
            @Param("serviceName") String serviceName,
            @Param("firstName") String firstName,
            @Param("city") String city,
            @Param("categoryNames") List<String> categoryNames,
            @Param("minExperience") Integer minExperience,
            @Param("minRating") Double minRating
    );

    @Query("SELECT u FROM User u "
            + "WHERE (:email IS NULL OR u.email = :email) "
            + "AND (:firstName IS NULL OR u.firstName = :firstName) "
            + "AND (:lastName IS NULL OR u.lastName = :lastName) "
            + "AND (:phoneNumber IS NULL OR u.phoneNumber = :phoneNumber)")
    List<User> findByFilters(
            @Param("email") String email,
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("phoneNumber") String phoneNumber
    );

    @Query("""
    select u from User u
    join u.roles r
    where r.name = :roleName
      and u.visible = true
            """)
    List<User> findAllSpecialists(@Param("roleName") RoleName roleName);
}
