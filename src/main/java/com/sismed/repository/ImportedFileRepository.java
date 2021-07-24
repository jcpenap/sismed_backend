package com.sismed.repository;

import com.sismed.domain.ImportedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportedFileRepository extends JpaRepository<ImportedFile, Long> {

}
