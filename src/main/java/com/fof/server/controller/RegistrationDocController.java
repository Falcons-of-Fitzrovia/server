package com.fof.server.controller;

import com.fof.server.service.EntrepreneurService;
import com.fof.server.service.InvestorService;
import com.fof.server.model.normal.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class RegistrationDocController {
    private final EntrepreneurService entrepreneurService;
    private final InvestorService investorService;

    public RegistrationDocController(EntrepreneurService entrepreneurService, InvestorService investorService) {
        this.entrepreneurService = entrepreneurService;
        this.investorService = investorService;
    }

    @PostMapping("/upload")
    public ResponseEntity <ResponseDTO> uploadFile(
            @RequestParam("policeReport") MultipartFile policeReport,
            @RequestParam("bankStatement") MultipartFile bankStatement,
            @RequestParam("businessregdoc") MultipartFile businessRegistration
    ) {
        String rootDirectory = System.getProperty("user.dir");

        // Example paths for saving images and videos
        String imageUploadPath = rootDirectory + "/src/main/resources/static/uploads/images/regImages";

        try {
            // Save images
            String policeReportFileName = policeReport.getOriginalFilename();
            Path policeReportFilePath = Paths.get(imageUploadPath, policeReportFileName);
            Files.write(policeReportFilePath, policeReport.getBytes());

            String bankStatementFileName = bankStatement.getOriginalFilename();
            Path bankStatementFilePath = Paths.get(imageUploadPath, bankStatementFileName);
            Files.write(bankStatementFilePath, bankStatement.getBytes());

            String businessRegistrationFileName = businessRegistration.getOriginalFilename();
            Path businessRegistrationFilePath = Paths.get(imageUploadPath, businessRegistrationFileName);
            Files.write(businessRegistrationFilePath, businessRegistration.getBytes());

            return ResponseEntity.ok(new ResponseDTO("Success", "Files uploaded successfully."));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO("Failure", "Error uploading files."));
        }
    }

    @PostMapping("/uploadenterprice")
    public ResponseEntity <ResponseDTO> uploadFileEnterprice(
            @RequestParam("bankStatement") MultipartFile bankStatement,
            @RequestParam("businessregdoc") MultipartFile businessRegistration
    ) {
        String rootDirectory = System.getProperty("user.dir");

        // Example paths for saving images and videos
        String imageUploadPath = rootDirectory + "/src/main/resources/static/uploads/images/regImages";

        try {
            // Save images
            String bankStatementFileName = bankStatement.getOriginalFilename();
            Path bankStatementFilePath = Paths.get(imageUploadPath, bankStatementFileName);
            Files.write(bankStatementFilePath, bankStatement.getBytes());

            String businessRegistrationFileName = businessRegistration.getOriginalFilename();
            Path businessRegistrationFilePath = Paths.get(imageUploadPath, businessRegistrationFileName);
            Files.write(businessRegistrationFilePath, businessRegistration.getBytes());

            return ResponseEntity.ok(new ResponseDTO("Success", "Files uploaded successfully."));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO("Failure", "Error uploading files."));
        }
    }

    @PostMapping("/uploadindividual")
    public ResponseEntity <ResponseDTO> uploadFileIndividual(
            @RequestParam("policeReport") MultipartFile policeReport,
            @RequestParam("bankStatement") MultipartFile bankStatement
    ) {
        String rootDirectory = System.getProperty("user.dir");

        // Example paths for saving images and videos
        String imageUploadPath = rootDirectory + "/src/main/resources/static/uploads/images/regImages";

        try {
            // Save images
            String policeReportFileName = policeReport.getOriginalFilename();
            Path policeReportFilePath = Paths.get(imageUploadPath, policeReportFileName);
            Files.write(policeReportFilePath, policeReport.getBytes());

            String bankStatementFileName = bankStatement.getOriginalFilename();
            Path bankStatementFilePath = Paths.get(imageUploadPath, bankStatementFileName);
            Files.write(bankStatementFilePath, bankStatement.getBytes());

            return ResponseEntity.ok(new ResponseDTO("Success", "Files uploaded successfully."));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO("Failure", "Error uploading files."));
        }
    }

    @GetMapping("/get-pdf/{id}")
    public ResponseEntity<List<byte[]>> getPdfs(@PathVariable String id) throws IOException {
        List<byte[]> pdfs = new ArrayList<>();
        String rootDirectory = System.getProperty("user.dir");
        String imageUploadPath = rootDirectory + "/src/main/resources/static/uploads/images/regImages";

        String policeReportFileName = entrepreneurService.getEntrepreneurById(Integer.parseInt(id)).getPoliceReport();
        String bankStatementFileName = entrepreneurService.getEntrepreneurById(Integer.parseInt(id)).getIncomeStatement();
        String businessRegistrationFileName = entrepreneurService.getEntrepreneurById(Integer.parseInt(id)).getBusinessRegDoc();

        Path policeReportFilePath = Paths.get(imageUploadPath, policeReportFileName);
        Path bankStatementFilePath = Paths.get(imageUploadPath, bankStatementFileName);
        Path businessRegistrationFilePath = Paths.get(imageUploadPath, businessRegistrationFileName);

        pdfs.add(Files.readAllBytes(policeReportFilePath));
        pdfs.add(Files.readAllBytes(bankStatementFilePath));
        pdfs.add(Files.readAllBytes(businessRegistrationFilePath));
        return ResponseEntity.ok().body(pdfs);
    }

    @GetMapping("/get-investor-pdf/{id}")
    public ResponseEntity<List<byte[]>> getInvestorPdfs(@PathVariable String id) throws IOException {
        List<byte[]> pdfs = new ArrayList<>();
        String rootDirectory = System.getProperty("user.dir");
        String imageUploadPath = rootDirectory + "/src/main/resources/static/uploads/images/regImages";

        String policeReportFileName = investorService.getInvestorById(Integer.parseInt(id)).getPoliceReport();
        String bankStatementFileName = investorService.getInvestorById(Integer.parseInt(id)).getFinancialDocument();

        Path bankStatementFilePath = Paths.get(imageUploadPath, bankStatementFileName);
        Path policeReportFilePath = Paths.get(imageUploadPath, policeReportFileName);

        pdfs.add(Files.readAllBytes(policeReportFilePath));
        pdfs.add(Files.readAllBytes(bankStatementFilePath));

        return ResponseEntity.ok().body(pdfs);
    }

}