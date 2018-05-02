package com.insurance.backend.service;


import com.insurance.backend.ContractRepository;
import com.insurance.backend.entity.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ContractServiceImpl implements ContractService {

    private ContractRepository contractRepository;

    @Autowired
    public ContractServiceImpl(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    public List<Contract> findAll() {
        return contractRepository.findAll();
    }

    public Contract find(Long id) {
        Assert.notNull(id);
        return contractRepository.findOne(id);
    }

    public boolean numberExists(Long contractNumber) {
        Assert.notNull(contractNumber);
        return contractRepository.findByContractNumber(contractNumber).size() > 0;
    }

    public Contract save(Contract contract) {
        Assert.notNull(contract, "contract must not be null");
        return contractRepository.save(contract);
    }

    public Double calculatePremium(int insSum, LocalDate startDate, LocalDate endDate, String propertyType, int yearBuilt, Double square) {
        Assert.notNull(startDate);
        Assert.notNull(endDate);
        Assert.notNull(propertyType);
        Assert.notNull(square);

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
