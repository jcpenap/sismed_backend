package com.sismed.service;

import com.sismed.domain.DetailImportedFile;
import com.sismed.domain.FileType;
import com.sismed.domain.ImportedFile;
import com.sismed.domain.User;
import com.sismed.dto.ExcelField;
import com.sismed.exception.CustomException;
import com.sismed.repository.DetailImportedFileRepository;
import com.sismed.repository.FileTypeRepository;
import com.sismed.repository.ImportedFileRepository;
import com.sismed.repository.UserRepository;
import com.sismed.request.FileInfoRequest;
import com.sismed.request.FileRequest;
import com.sismed.util.ExcelFieldMapper;
import com.sismed.util.ExcelFileReader;
import com.sismed.util.ExcelSection;
import com.sismed.util.ConfigProperties;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class FileServiceImpl implements FileService {


    private final static Logger log = LoggerFactory.getLogger(FileServiceImpl.class);
    private final ImportedFileRepository importedFileRepository;
    private final FileTypeRepository fileTypeRepository;
    private final UserRepository userRepository;
    private final DetailImportedFileRepository detailImportedFileRepository;
    private final JavaMailSender javaMailSender;
    private final ConfigProperties mail;

    @Override
    public String storeFile(MultipartFile file) throws CustomException {
        String filePath = "";
        try {

            String tmpdir = Files.createTempDirectory("sismed").toFile().getAbsolutePath();
            if (!new File(tmpdir).exists()) {
                new File(tmpdir).mkdir();
            }
            log.info("realPathtoUploads = {}", tmpdir);
            String orgName = file.getOriginalFilename();
            filePath = tmpdir + "/" + orgName;
            Path excelFilePath = Paths.get(filePath);
            Files.deleteIfExists(excelFilePath);
            Files.copy(file.getInputStream(), excelFilePath);
        } catch (IOException e) {
            throw new CustomException("Error al crear el archivo", "500");
        }
        return filePath;
    }

    @Override
    @Async
    public void upload(String storeFile, FileRequest fileRequest) throws CustomException {
        try {
            sendEmail(fileRequest.getUsername(), mail.getSubject(), mail.getTextLoading());
            int idFile = readFile(storeFile, fileRequest);
            sendEmail(fileRequest.getUsername(), mail.getSubjectLoaded(), mail.getTextLoaded()+ " " +idFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendEmail(String username, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(username);
        msg.setSubject(subject);
        msg.setText(text);

        javaMailSender.send(msg);
    }

    private int readFile(String pathname, FileRequest fileRequest) throws IOException, CustomException {
        File file = new File(pathname);
        Workbook workbook = WorkbookFactory.create(file);
        Sheet sheet = workbook.getSheetAt(0);
        ImportedFile importedFile = saveImportedFile(fileRequest, file.getName());
        Map<String, List<ExcelField[]>> excelRowValues = ExcelFileReader.getExcelRowValues(sheet);
        List<DetailImportedFile> importedFiles = ExcelFieldMapper.getPojos(excelRowValues.get(ExcelSection.IMPORTED_FILE.getTypeValue()),
                DetailImportedFile.class);
        detailImportedFileRepository.saveAll(importedFiles.stream().map(detail -> {
            detail.setImportedFile(importedFile);
            return detail;
        }).collect(Collectors.toList()));
        return importedFile.getId();
    }

    private ImportedFile saveImportedFile(FileRequest fileRequest, String fileName) throws CustomException {
        FileType fileType =
                getFileType(fileRequest.getFileType());
        ImportedFile importedFile = new ImportedFile();
        importedFile.setCreateTime(new Date());
        importedFile.setName(fileName);
        User user = getUser(fileRequest.getUsername());
        importedFile.setUser(user);
        importedFile.setFileType(fileType);
        return importedFileRepository.save(importedFile);

    }

    private FileType getFileType(String description) throws CustomException {
        return fileTypeRepository.getByDescription(description)
                .orElseThrow(() -> new CustomException("File type not found", "500"));
    }

    private User getUser(String email) throws CustomException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found", "500"));
    }

    @Override
    public String download() throws CustomException {
        String response = "";
        String tmpdir = System.getProperty("java.io.tmpdir");
        try (FileOutputStream fos =
                     new FileOutputStream(new File(tmpdir + "/SISMED.xlsx"))) {
            Workbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet("SISMED");
            Row row = sheet.createRow(0);
            Cell first = row.createCell(0);
            first.setCellValue(new HSSFRichTextString("NIT Proveedor"));
            Cell second = row.createCell(1);
            second.setCellValue(new HSSFRichTextString("Numero de Factura"));
            Cell third = row.createCell(2);
            third.setCellValue(new HSSFRichTextString("Fecha Factura"));
            Cell fourth = row.createCell(3);
            fourth.setCellValue(new HSSFRichTextString("Tipo Transaccion"));
            Cell fifth = row.createCell(4);
            fifth.setCellValue(new HSSFRichTextString("IUM-Primernivel"));
            Cell sixth = row.createCell(5);
            sixth.setCellValue(new HSSFRichTextString("IUM-Segundo nivel"));
            Cell seventh = row.createCell(6);
            seventh.setCellValue(new HSSFRichTextString("IUM-Tercer nivel"));
            Cell eight = row.createCell(7);
            eight.setCellValue(new HSSFRichTextString("Número de expediente"));
            Cell ninth = row.createCell(8);
            ninth.setCellValue(new HSSFRichTextString("Número consecutivo"));
            Cell tenth = row.createCell(9);
            tenth.setCellValue(new HSSFRichTextString("Unidad de Facturacion"));
            Cell eleventh = row.createCell(10);
            eleventh.setCellValue(new HSSFRichTextString("Valor unitario"));
            Cell twelfth = row.createCell(11);
            twelfth.setCellValue(new HSSFRichTextString("Cantidad"));
            workbook.write(fos);
            Path path = Paths.get(tmpdir + "/SISMED.xlsx");
            response = Base64.getEncoder().encodeToString(Files.readAllBytes(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public List<FileInfoRequest> getFiles() {
        List<ImportedFile> files = importedFileRepository.findAll();
        return files.stream().map(f -> {
                  FileInfoRequest fr = new FileInfoRequest();
                  fr.setFileType(f.getFileType().getInfo());
                  fr.setCreateTime(new SimpleDateFormat("yyyy-MM-dd").format(f.getCreateTime()));
                  fr.setFileTypeId(f.getFileType().getId());
                  fr.setId(f.getId());
                  fr.setName(f.getName());
                  fr.setUser(f.getUser().getEmail());
                  return fr;
        }).collect(Collectors.toList());
    }


}