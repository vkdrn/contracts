package com.insurance.backend.service;


import com.insurance.backend.ClientRepository;
import com.insurance.backend.entity.Client;
import com.insurance.backend.util.ClientSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Client find(Long id) {
        return clientRepository.findOne(id);
    }

    public Client save(Client client) {
        Assert.notNull(client, "client must not be null");
        return clientRepository.save(client);
    }

    public List<Client> search(String firstName, String patronymic, String lastName) {
        Assert.notNull(firstName);
        Assert.notNull(patronymic);
        Assert.notNull(lastName);

        Client client = new Client();

        client.setFirstName(firstName);
        client.setPatronymic(patronymic);
        client.setLastName(lastName);

        Specification<Client> spec = new ClientSpecification(client);


        return clientRepository.findAll(spec);
    }

}
