package com.sismed.repository;


import com.sismed.domain.Role;
import com.sismed.domain.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(RoleType name);

}
