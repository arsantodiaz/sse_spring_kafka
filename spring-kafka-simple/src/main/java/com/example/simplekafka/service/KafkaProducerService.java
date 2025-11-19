package com.example.simplekafka.service;

import com.example.simplekafka.model.ReportRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final String TOPIC = "report-requests";

    @Autowired
    private KafkaTemplate<String, ReportRequest> kafkaTemplate;

    public void sendReportRequest(ReportRequest request) {
        kafkaTemplate.send(TOPIC, request.getReportId(), request);
    }
}
