package com.example.simplekafka.model;

import lombok.Data;

@Data
public class ReportJob {
    private String id;
    private ReportStatus status;
    private String filename;
    private String message;

    public ReportJob(String id) {
        this.id = id;
        this.status = ReportStatus.PENDING;
    }
}
