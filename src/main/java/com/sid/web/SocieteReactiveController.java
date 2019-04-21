package com.sid.web;

import com.sid.dao.SocieteRepository;
import com.sid.entities.Societe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class SocieteReactiveController {

    @Autowired
    private SocieteRepository societeRepository;

    @GetMapping(value = "/societies")
    public Flux<Societe> findAll() {
        return societeRepository.findAll();
    }

    @GetMapping(value = "/societies/{id}")
    public Mono<Societe> findOne(@PathVariable  String id) {
        return societeRepository.findById(id);
    }
    @PostMapping(value = "/societies")
    public Mono<Societe> save(@RequestBody  Societe societe){
        return societeRepository.save(societe);
    }

    @DeleteMapping(value = "/societies/{id}")
    public Mono<Void> deleteOne(@PathVariable  String id) {
        return societeRepository.deleteById(id);
    }

    @PutMapping(value = "/societies/{id}")
    public Mono<Societe> update(@RequestBody  Societe societe,@PathVariable String id){
        societe.setId(id);
        return societeRepository.save(societe);
    }
}
