package com.sismed.repository;

import com.sismed.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    @Query(value="select r.name from sismed.user_role ur inner join sismed.role r on (ur.role_id = r.id) where user_id = :userId" ,nativeQuery=true )
    List<String> rolesByUserId(@Param("userId") int userId);
}
