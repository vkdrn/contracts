package com.insurance.backend.service;

import com.insurance.backend.entity.Client;

import java.util.List;

public interface ClientService {

    public List<Client> findAll();

    public Client find(Long id);

    public Client save(Client client);

    public List<Client> search(String firstName, String patronymic, String lastName);
}
