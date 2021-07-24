package com.sismed.repository;

import com.sismed.domain.DetailImportedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailImportedFileRepository extends JpaRepository<DetailImportedFile, Long> {
}
