package com.example.simplekafka.service;

import com.example.simplekafka.model.ReportJob;
import com.example.simplekafka.model.ReportStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SseService {

    // Emitter untuk halaman status spesifik per pekerjaan
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    // Emitter BARU untuk notifikasi global di halaman utama
    private final List<SseEmitter> globalEmitters = new CopyOnWriteArrayList<>();

    public SseEmitter createEmitter(String reportId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        this.emitters.put(reportId, emitter);
        emitter.onCompletion(() -> this.emitters.remove(reportId));
        emitter.onTimeout(() -> this.emitters.remove(reportId));
        emitter.onError(e -> this.emitters.remove(reportId));
        return emitter;
    }

    /**
     * Metode BARU untuk membuat koneksi notifikasi global.
     */
    public SseEmitter createGlobalEmitter() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        this.globalEmitters.add(emitter);
        emitter.onCompletion(() -> this.globalEmitters.remove(emitter));
        emitter.onTimeout(() -> this.globalEmitters.remove(emitter));
        emitter.onError(e -> this.globalEmitters.remove(emitter));
        try {
            emitter.send(SseEmitter.event().name("connected").data("Global notification channel connected."));
        } catch (IOException e) {
            this.globalEmitters.remove(emitter);
        }
        return emitter;
    }

    /**
     * Mengirim update ke halaman status spesifik.
     */
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

    /**
     * Metode BARU untuk mengirim notifikasi ke semua klien di halaman utama.
     */
    public void sendGlobalNotification(ReportJob job) {
        for (SseEmitter emitter : globalEmitters) {
            try {
                emitter.send(SseEmitter.event().name("jobCompletion").data(job));
            } catch (IOException e) {
                globalEmitters.remove(emitter);
            }
        }
    }
}
