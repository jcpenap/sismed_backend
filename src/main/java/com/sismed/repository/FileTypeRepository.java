package com.sismed.repository;

import com.sismed.domain.FileType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileTypeRepository extends JpaRepository<FileType, Integer> {
    Optional<FileType> getByDescription(String description);
}
