package com.sid.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
public class Transaction {
    @Id
    private String id;
    private Instant instant;
    private double price;
    @DBRef
    @JsonProperty (access = JsonProperty.Access.WRITE_ONLY)
    private Societe societe;

    public Transaction() {
    }

    public Transaction(String id, Instant instant, double price, Societe societe) {
        this.id = id;
        this.instant = instant;
        this.price = price;
        this.societe = societe;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getInstant() {
        return instant;
    }

    public void setInstant(Instant instant) {
        this.instant = instant;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Societe getSociete() {
        return societe;
    }

    public void setSociete(Societe societe) {
        this.societe = societe;
    }
}
