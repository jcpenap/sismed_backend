package com.sismed.repository;

import com.sismed.domain.Errors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ErrorsRepository extends JpaRepository<Errors, Integer> {
    List<Errors> findByFileId(int fileId);
}
