package com.insurance.backend;


import com.insurance.backend.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findByFirstNameLikeIgnoreCase(String name);

    List<Client> findByPatronymicLikeIgnoreCase(String name);

    List<Client> findByLastNameLikeIgnoreCase(String name);
}
