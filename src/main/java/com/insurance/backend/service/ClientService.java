package com.insurance.backend.service;


import com.insurance.backend.ClientRepository;
import com.insurance.backend.entity.Client;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Client find(Long id) {
        return clientRepository.findOne(id);
    }

    public Client save(Client client) {
        return clientRepository.save(client);
    }

    public List<Client> search(String firstName, String patronymic, String lastName) {
        List<Client> result = clientRepository.findAll();

        if (!StringUtils.isBlank(firstName) && result != null) {
            result = result.stream().filter(e -> e.getFirstName().toLowerCase().contains(firstName.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (!StringUtils.isBlank(patronymic) && result != null) {
            result = result.stream().filter(e -> e.getPatronymic().toLowerCase().contains(patronymic.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (!StringUtils.isBlank(lastName) && result != null) {
            result = result.stream().filter(e -> e.getLastName().toLowerCase().contains(lastName.toLowerCase()))
                    .collect(Collectors.toList());
        }

        return result;
    }

}
