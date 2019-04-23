package com.sid.web;

import com.sid.dao.SocieteRepository;
import com.sid.dao.TransactionRepository;
import com.sid.entities.Societe;
import com.sid.entities.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

@RestController
public class TransactionReactiveController {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private SocieteRepository societeRepository;

    @GetMapping(value = "/transactions")
    public Flux<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @GetMapping(value = "/transactions/{id}")
    public Mono<Transaction> findOne(@PathVariable  String id) {
        return transactionRepository.findById(id);
    }

    @PostMapping(value = "/transactions")
    public Mono<Transaction> save(@RequestBody  Transaction transaction){
        return transactionRepository.save(transaction);
    }

    @DeleteMapping(value = "/transactions/{id}")
    public Mono<Void> deleteOne(@PathVariable  String id) {
        return transactionRepository.deleteById(id);
    }

    @PutMapping(value = "/transactions/{id}")
    public Mono<Transaction> update(@RequestBody  Transaction transaction,@PathVariable String id){
        transaction.setId(id);
        return transactionRepository.save(transaction);
    }

    @GetMapping(value = "/streamTransactions",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Transaction> streamTransaction() {
        return transactionRepository.findAll();
    }

    @GetMapping(value = "/transactionsBySociete/{id}")
    public Flux<Transaction> transactionsBySociete(@PathVariable String id) {
        Societe societe=new Societe();
        societe.setId(id);
        return transactionRepository.findBySociete(societe);
    }

    @GetMapping(value = "/streamTransactionsBySociete/{id}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Transaction> streamTransactionsBySociete(@PathVariable String id) {

        return societeRepository.findById(id).flatMapMany( soc ->{
            Flux<Long> interval=Flux.interval(Duration.ofMillis(1000));
            Flux<Transaction> transactionFlux = Flux.fromStream(Stream.generate( ()-> {

                Transaction transaction = new Transaction();
                transaction.setInstant(Instant.now());
                transaction.setSociete(soc);
                transaction.setPrice(soc.getPrice()*(1+(Math.random()*12)-6/100));
                return transaction;
            }));

            return Flux.zip(interval,transactionFlux).
                    map( data -> {
                        return data.getT2();
                    });
        });
    }

    @GetMapping(value = "/events/{id}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Double> events(@PathVariable String id){
        WebClient webClient = WebClient.create("http://localhost:8082");

        Flux<Double> eventFlux = webClient.get()
                .uri("/streamEvents/"+id)
                .retrieve().bodyToFlux(Event.class)
                .map( data -> data.getValue());
        return eventFlux;
    }
}

class Event {
    private Instant instant;
    private double value;
    private String societyId;

    public Instant getInstant() {
        return instant;
    }

    public void setInstant(Instant instant) {
        this.instant = instant;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getSocietyId() {
        return societyId;
    }

    public void setSocietyId(String societyId) {
        this.societyId = societyId;
    }
}
