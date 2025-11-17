package com.example.simplekafka.controller;

import com.example.simplekafka.model.ReportJob;
import com.example.simplekafka.model.ReportRequest;
import com.example.simplekafka.service.KafkaConsumerService;
import com.example.simplekafka.service.KafkaProducerService;
import com.example.simplekafka.service.SseService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
public class ReportController {

    private final KafkaProducerService kafkaProducerService;
    private final SseService sseService;
    private final KafkaConsumerService kafkaConsumerService;
    private final String reportOutputDir;

    public ReportController(KafkaProducerService kafkaProducerService, SseService sseService, KafkaConsumerService kafkaConsumerService, @Value("${report.output.dir:/reports}") String reportOutputDir) {
        this.kafkaProducerService = kafkaProducerService;
        this.sseService = sseService;
        this.kafkaConsumerService = kafkaConsumerService;
        this.reportOutputDir = reportOutputDir;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/generate")
    public String generateReport(Model model) {
        String reportId = UUID.randomUUID().toString();
        ReportRequest request = new ReportRequest(reportId, "user1");
        kafkaProducerService.sendReportRequest(request);
        model.addAttribute("reportId", reportId);
        return "status";
    }

    @GetMapping("/status/{reportId}")
    public SseEmitter streamStatus(@PathVariable String reportId) {
        return sseService.createEmitter(reportId);
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            Path file = Paths.get(reportOutputDir).resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}
