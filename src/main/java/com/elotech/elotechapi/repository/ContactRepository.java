package com.elotech.elotechapi.repository;

import com.elotech.elotechapi.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository <Contact,Long> {

}
