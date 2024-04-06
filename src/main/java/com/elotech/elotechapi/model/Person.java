package com.elotech.elotechapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Column(length = 14)
    @NotBlank
    @CPF(message = "Cpf is not valid")
    private String cpf;

    @NotNull
    @Past(message = "The date of birth must be before the current date")
    private LocalDate birthDate;

    @NotNull(message = "At least one contact is necessary")
    @OneToMany(cascade = CascadeType.ALL)
    private List<Contact> contactList;
}
