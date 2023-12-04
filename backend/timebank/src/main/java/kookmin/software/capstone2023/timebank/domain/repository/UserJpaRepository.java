package kookmin.software.capstone2023.timebank.domain.repository;

import kookmin.software.capstone2023.timebank.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {
//    @Query(value = "select * from user u where u.id = :id", nativeQuery = true)
//    User getUserById(@Param("id") Long id);
    User getUserById(Long id);

}
