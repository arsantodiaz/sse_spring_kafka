package com.example.simplekafka.service;

import com.example.simplekafka.model.ReportJob;
import com.example.simplekafka.model.ReportRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class KafkaConsumerService {

    private final ReportGeneratorService reportGeneratorService;
    private final Map<String, ReportJob> jobStore = new ConcurrentHashMap<>();

    public KafkaConsumerService(ReportGeneratorService reportGeneratorService) {
        this.reportGeneratorService = reportGeneratorService;
    }

    @KafkaListener(topics = "report-requests", groupId = "report-generator-group")
    public void listen(ReportRequest request) {
        System.out.println("Received message: " + request);
        ReportJob job = new ReportJob(request.getReportId());
        jobStore.put(job.getId(), job);
        reportGeneratorService.generateReport(job);
    }

    public ReportJob getJob(String id) {
        return jobStore.get(id);
    }
}
