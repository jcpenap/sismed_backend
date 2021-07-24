package com.sismed.service;

import com.sismed.domain.Errors;
import com.sismed.exception.CustomException;
import com.sismed.repository.ErrorsRepository;
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
import java.util.Base64;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ErrorsServiceImpl implements  ErrorsService{

    private final ErrorsRepository errorsRepository;
    private final EntityManager entityManager;

    @Override
    public String getErrorsByFileId(int fileId) throws CustomException {
        String result = "";
        try {
            findErrorsById(fileId);
            List<String> list = errorsRepository.findByFileId(fileId)
                    .stream()
                    .map(getResponse())
                    .collect(Collectors.toList());
            String tmpdir = System.getProperty("java.io.tmpdir");
            Path path = Paths.get(tmpdir + "/Log.txt");
            Files.write(path, list);
            result = Base64.getEncoder().encodeToString(Files.readAllBytes(path));
        } catch (IllegalArgumentException e) {
            throw new CustomException(e, "Error al consultar log del archivo");
        } catch (IOException e) {
            throw new CustomException(e, "Error al crear el archivo");
        }
        return result;
    }

    private Function<Errors, String> getResponse() {
        return e -> {
            StringBuilder response = new StringBuilder();
            response.append("Fila: ").append(e.getRownumber()).append(System.lineSeparator())
                    .append("Mensaje: ").append(e.getMessa()).append(System.lineSeparator())
                    .append("----------------------------");
            return response.toString();
        };
    }

    private boolean findErrorsById(int fileIde) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sismed.validate_file");
        query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
        query.setParameter(1, fileIde);
        return query.execute();
    }
}
