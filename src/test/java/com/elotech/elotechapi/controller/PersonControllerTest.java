package com.elotech.elotechapi.controller;

import com.elotech.elotechapi.model.Contact;
import com.elotech.elotechapi.model.Person;
import com.elotech.elotechapi.repository.PersonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerTest {

    private Person person;
    private Person personWithWrongCPF;
    private Person personWithWrongBirthDate;
    private List<Contact> contactList;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonRepository personRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void prepareScenarios() {
        contactList = createContactList();
        person = createPerson(contactList);
        personWithWrongCPF = createPersonWithWrongCPF(contactList);
        personWithWrongBirthDate = createPersonWithWrongBirthDate(contactList);
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
   }

    @Test
    public void getPersonByID() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/person/{id}",1))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void getAllPerson() throws Exception {
        List<Person> personList = new ArrayList<>(List.of(person));
        Page<Person> foundPage = new PageImpl<>(personList);
        when(personRepository.findAll(any(Pageable.class))).thenReturn(foundPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/person"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    public void savePerson() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isCreated());
    }

    @Test
    public void savePersonWithInvalidCPF() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personWithWrongCPF)))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string("Cpf is not valid. "));
    }

    @Test
    public void savePersonWithInvalidBirthDate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personWithWrongBirthDate)))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string("The date of birth must be before the current date. "));
    }

    @Test
    public void updatePerson() throws Exception {
        Contact contactUpdated = new Contact(1L,"Celular Comercial","4499774455","adler.cachuba@gmail.com");
        List<Contact> contactList = new ArrayList<>(List.of(contactUpdated));
        Person updatedPerson = new Person (1L,"Adler Mateus Cachuba","25267213004",  LocalDate.of(1995, 3, 2), contactList);

        mockMvc.perform(MockMvcRequestBuilders.put("/person/{id}",1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedPerson)))
                .andExpect(status().isOk());
    }

    @Test
    public void updatePersonWithInvalidCPF() throws Exception {
        Person updatedPersonWithInvalidCPF = createPersonWithWrongCPF(contactList);

        mockMvc.perform(MockMvcRequestBuilders.put("/person/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPersonWithInvalidCPF)))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string("Cpf is not valid. "));
    }

    @Test
    public void updatePersonWithInvalidBirthDate() throws Exception {
        Person updatedPersonWithInvalidBirthDate = createPersonWithWrongBirthDate(contactList);

        mockMvc.perform(MockMvcRequestBuilders.put("/person/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPersonWithInvalidBirthDate)))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string("The date of birth must be before the current date. "));
    }

    @Test
    public void deletePerson() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/person/{id}",1))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deletePersonWithInvalidID() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/person/{id}",9))
                .andExpect(status().isNotFound());
    }

    private static Person createPerson(List<Contact> contactList) {
        return new Person(1L,"Adler","25267213004", LocalDate.of(1995, 3, 2), contactList);
    }

    private static Person createPersonWithWrongCPF(List<Contact> contactList) {
        return new Person(1L,"Adler","11111111111", LocalDate.of(1995, 3, 2), contactList);
    }

    private static Person createPersonWithWrongBirthDate(List<Contact> contactList) {
        return new Person(1L,"Adler","25267213004", LocalDate.now().plusYears(1), contactList);
    }

    private static List<Contact> createContactList() {
        Contact contact = new Contact(1L,"Celular","4499888888","adlercachuba@gmail.com");
        return new ArrayList<>(List.of(contact));
    }
}
