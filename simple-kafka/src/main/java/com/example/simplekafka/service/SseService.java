package com.example.simplekafka.service;

import com.example.simplekafka.model.ReportJob;
import com.example.simplekafka.model.ReportStatus; // <-- IMPORT ADDED
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SseService {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter createEmitter(String reportId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        this.emitters.put(reportId, emitter);

        emitter.onCompletion(() -> this.emitters.remove(reportId));
        emitter.onTimeout(() -> this.emitters.remove(reportId));
        emitter.onError(e -> this.emitters.remove(reportId));

        return emitter;
    }

    public void sendUpdate(ReportJob job) {
        SseEmitter emitter = emitters.get(job.getId());
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("statusUpdate").data(job));
                if (job.getStatus() == ReportStatus.COMPLETED || job.getStatus() == ReportStatus.FAILED) {
                    emitter.complete();
                }
            } catch (IOException e) {
                emitters.remove(job.getId());
            }
        }
    }
}
