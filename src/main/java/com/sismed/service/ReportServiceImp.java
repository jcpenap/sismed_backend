package com.sismed.service;

import com.sismed.domain.SismedReport;
import com.sismed.domain.User;
import com.sismed.exception.CustomException;
import com.sismed.repository.LogReportRepository;
import com.sismed.repository.SismedReportRepository;
import com.sismed.repository.UserRepository;
import com.sismed.request.ReportRequest;
import com.sismed.response.FileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.ParseException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportServiceImp implements ReportService {

    public static final String COMMA = ",";
    private final EntityManager entityManager;
    private final UserRepository userRepository;
    private final LogReportRepository logReportRepository;
    private final SismedReportRepository sismedReportRepository;

    @Override
    public FileResponse generateReport(ReportRequest request) throws CustomException {
        FileResponse response = new FileResponse();
        try {
            response = encodeFileByEmail(request);
        } catch (IOException e) {
            new CustomException("Error al crear el archivo", "500");
        } catch (ParseException e) {
            new CustomException("Fecha   Invalida", "500");
        }
        return response;
    }

    private FileResponse encodeFileByEmail(ReportRequest request) throws CustomException, IOException, ParseException {
        String tmpdir = System.getProperty("java.io.tmpdir");
        User user = this.getUser(request.getEmail());

        generate(request);

        List<SismedReport> reportList = sismedReportRepository.findAll();
        List<String> list = reportList.stream().map(r -> {
            StringBuilder response = new StringBuilder();
            response.append(r.getReg()).append(COMMA)
                    .append(r.getRowNumber()).append(COMMA)
                    .append(r.getAvaliabilityCode()).append(COMMA)
                    .append(r.getInvoiceMonth()).append(COMMA)
                    .append(r.getRoleActor()).append(COMMA)
                    .append(r.getDescription()).append(COMMA)
                    .append(r.getTransactionType()).append(COMMA)
                    .append(r.getFirstIum()).append(COMMA)
                    .append(r.getSecondIum()).append(COMMA)
                    .append(r.getThirdIum()).append(COMMA)
                    .append(r.getExpedient()).append(COMMA)
                    .append(r.getConsecutive()).append(COMMA)
                    .append(r.getUnitInvoice()).append(COMMA)
                    .append(r.getMinPrice()).append(COMMA)
                    .append(r.getTotalPrice()).append(COMMA)
                    .append(r.getTotalUnits()).append(COMMA)
                    .append(r.getMinInvoice()).append(COMMA)
                    .append(r.getMaxInvoice());
            return response.toString();
        }).collect(Collectors.toList());

        String first = firstRow(request, user, reportList);
        list.add(0, first);
        String date = request.getFinishDate().replace("-","");
        String nit = (String.format("%012d", Integer.parseInt(user.getAvaliabilityCode())));
        String name = "MED100MPRE" + date + user.getDocumentType().getName() + nit + ".txt";
        Path path = Paths.get(tmpdir + "/"+ name);
        Files.write(path, list);
        FileResponse response = new FileResponse();
        response.setFile(Base64.getEncoder().encodeToString(Files.readAllBytes(path)));
        response.setName(name);
        return response;
    }

    private String firstRow(ReportRequest request, User user, List<SismedReport> reportList) {
        if(reportList!=null && !reportList.isEmpty()) {
            Integer totalMeddicine = reportList.get(0).getTotalMeddicine();
            StringBuilder s = new StringBuilder();
            s.append(1).append(COMMA)
                    .append(user.getDocumentType().getName()).append(COMMA)
                    .append(user.getAvaliabilityCode()).append(COMMA)
                    .append(request.getInitialDate()).append(COMMA)
                    .append(request.getFinishDate()).append(COMMA)
                    .append(reportList.size()).append(COMMA)
                    .append(totalMeddicine);
            return s.toString();
        }
        return "";
    }

    private User getUser(String email) throws CustomException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found", "500"));
    }

    private boolean generate(ReportRequest request) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sismed.sismed_detaill");
        query.registerStoredProcedureParameter(1, Date.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(2, Date.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(3, Integer.class, ParameterMode.IN);
        query.setParameter(1, Date.valueOf(request.getInitialDate()));
        query.setParameter(2, Date.valueOf(request.getFinishDate()));
        query.setParameter(3, request.getFileType());
        return query.execute();
    }
}
