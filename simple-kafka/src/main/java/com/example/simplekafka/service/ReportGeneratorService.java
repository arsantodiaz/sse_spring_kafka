package com.example.simplekafka.service;

import com.example.simplekafka.model.ReportJob;
import com.example.simplekafka.model.ReportStatus;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportGeneratorService {

    private final SseService sseService;
    private final String reportOutputDir;

    public ReportGeneratorService(SseService sseService, @Value("${report.output.dir:/reports}") String reportOutputDir) {
        this.sseService = sseService;
        this.reportOutputDir = reportOutputDir;
        new File(reportOutputDir).mkdirs();
    }

    @Async
    public void generateReport(ReportJob job) {
        try {
            job.setStatus(ReportStatus.IN_PROGRESS);
            job.setMessage("Generating report...");
            sseService.sendUpdate(job);

            Thread.sleep(5000); // 5 seconds delay

            InputStream template = new ClassPathResource("report_template.jrxml").getInputStream();
            JasperReport jasperReport = JasperCompileManager.compileReport(template);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("REPORT_ID", job.getId());
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singletonList("dummy"));

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            String filename = "report-" + job.getId() + ".pdf";
            String outputPath = Paths.get(reportOutputDir, filename).toString();
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);

            job.setStatus(ReportStatus.COMPLETED);
            job.setFilename(filename);
            job.setMessage("Report generated successfully.");
            sseService.sendUpdate(job);
            // --- PANGGILAN BARU: Kirim notifikasi ke semua klien global ---
            sseService.sendGlobalNotification(job);

        } catch (Exception e) {
            job.setStatus(ReportStatus.FAILED);
            job.setMessage("Failed to generate report: " + e.getMessage());
            sseService.sendUpdate(job);
            // Kirim juga notifikasi global jika gagal
            sseService.sendGlobalNotification(job);
        }
    }
}
