package com.sismed.repository;

import com.sismed.domain.SismedReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SismedReportRepository  extends JpaRepository<SismedReport, Long> {
}
