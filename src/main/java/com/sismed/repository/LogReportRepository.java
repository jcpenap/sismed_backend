package com.sismed.repository;

import com.sismed.domain.LogReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogReportRepository extends JpaRepository<LogReport, Long> {

}
