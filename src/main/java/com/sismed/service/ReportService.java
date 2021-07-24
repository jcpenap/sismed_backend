package com.sismed.service;

import com.sismed.exception.CustomException;
import com.sismed.request.ReportRequest;
import com.sismed.response.FileResponse;

public interface ReportService {
    FileResponse generateReport(ReportRequest request) throws CustomException;
}
