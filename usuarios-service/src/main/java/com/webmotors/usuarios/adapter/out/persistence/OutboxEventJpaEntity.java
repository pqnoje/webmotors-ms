package com.webmotors.usuarios.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "outbox_events")
public class OutboxEventJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 36)
    private String eventId;

    @Column(nullable = false, length = 200)
    private String topic;

    @Column(nullable = false, length = 100)
    private String eventKey;

    @Column(nullable = false, columnDefinition = "text")
    private String payload;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(nullable = false)
    private int attempts;

    @Column(nullable = false)
    private Instant createdAt;

    private Instant publishedAt;

    @Column(columnDefinition = "text")
    private String lastError;

    protected OutboxEventJpaEntity() {
    }

    public OutboxEventJpaEntity(String eventId, String topic, String eventKey, String payload) {
        this.eventId = eventId;
        this.topic = topic;
        this.eventKey = eventKey;
        this.payload = payload;
        this.status = "PENDING";
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public String getEventId() { return eventId; }
    public String getTopic() { return topic; }
    public String getEventKey() { return eventKey; }
    public String getPayload() { return payload; }
    public String getStatus() { return status; }
    public int getAttempts() { return attempts; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getPublishedAt() { return publishedAt; }
    public String getLastError() { return lastError; }

    public void markPublished() {
        status = "PUBLISHED";
        publishedAt = Instant.now();
        lastError = null;
    }

    public void markFailed(Exception exception) {
        attempts++;
        lastError = exception.getMessage();
    }
}