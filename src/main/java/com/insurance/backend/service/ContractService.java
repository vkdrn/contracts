package com.insurance.backend.service;


import com.insurance.backend.ContractRepository;
import com.insurance.backend.entity.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ContractService {

    private ContractRepository contractRepository;
    private ClientService clientService;

    @Autowired
    public ContractService(ContractRepository contractRepository, ClientService clientService) {
        this.contractRepository = contractRepository;
        this.clientService = clientService;
    }

    public List<Contract> findAll() {
        return contractRepository.findAll();
    }

    public Contract save(Contract contract) {
        return contractRepository.save(contract);
    }

    public Double calculatePremium(int insSum, LocalDate startDate, LocalDate endDate, String propertyType, int yearBuilt, Double square) {
        double coefType;
        double coefYear = 0.0;
        double coefSquare = 0.0;
        int numOfDays;

        switch (propertyType) {
            case "квартира":
                coefType = 1.7;
                break;
            case "дом":
                coefType = 1.5;
                break;
            case "комната":
                coefType = 1.3;
                break;
            default:
                coefType = 0;
                break;
        }

        if (yearBuilt == 2015) {
            coefYear = 2;
        } else if (yearBuilt >= 2000 && yearBuilt <= 2014) {
            coefYear = 1.6;
        } else if (yearBuilt < 2000) {
            coefYear = 1.3;
        }

        if (square < 50) {
            coefSquare = 1.2;
        } else if (square >= 50 && square <= 100) {
            coefSquare = 1.5;
        } else if (square > 100) {
            coefSquare = 2;
        }

        numOfDays = (int) ChronoUnit.DAYS.between(startDate, endDate);

        double result = ((double) insSum / (double) numOfDays) * coefType * coefYear * coefSquare;
        return (double) Math.round(result * 100) / 100;
    }
}
