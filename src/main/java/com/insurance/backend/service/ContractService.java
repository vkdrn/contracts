package com.insurance.backend.service;


import com.insurance.backend.entity.Contract;

import java.time.LocalDate;
import java.util.List;

public interface ContractService {

    public List<Contract> findAll();

    public Contract find(Long id);

    public boolean numberExists(Long contractNumber);

    public Contract save(Contract contract);

    public Double calculatePremium(int insSum, LocalDate startDate, LocalDate endDate, String propertyType, int yearBuilt, Double square);

}
