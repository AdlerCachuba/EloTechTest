package com.elotech.elotechapi.controller;

import com.elotech.elotechapi.model.Person;
import com.elotech.elotechapi.repository.PersonRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class PersonController {

    @Autowired
    PersonRepository personRepository;

    @RequestMapping(value = "/person", method = RequestMethod.POST)
    public ResponseEntity<String> savePerson(@Valid @RequestBody Person person) {
        personRepository.save(person);
        return ResponseEntity.status(HttpStatus.CREATED).body("Person created successfully with ID " + person.getId());
    }

    @RequestMapping(value = "/person/{id}", method = RequestMethod.GET)
    public ResponseEntity<Person> getPerson(@PathVariable(value = "id") Long id) {
        Optional<Person> person = personRepository.findById(id);
        if (person.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(person.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @RequestMapping(value = "/person", method = RequestMethod.GET)
    public Page<Person> getPerson(Pageable pageable) {
        return personRepository.findAll(pageable);
    }

    @RequestMapping(value = "/person/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Person> updatePerson(@PathVariable(value = "id") Long id, @Valid @RequestBody Person person) {
        Optional<Person> personWanted = personRepository.findById(id);
        if (personWanted.isPresent()) {
            Person personChanged = personWanted.get();
            personChanged.setName(person.getName());
            personChanged.setCpf(person.getCpf());
            personChanged.setBirthDate(person.getBirthDate());
            personChanged.setContactList(person.getContactList());
            personRepository.save(personChanged);
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @RequestMapping(value = "/person/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Person> deletePerson(@PathVariable(value = "id") long id) {
        Optional<Person> person = personRepository.findById(id);
        if (person.isPresent()) {
            personRepository.delete(person.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}