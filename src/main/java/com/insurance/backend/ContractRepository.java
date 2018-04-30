package com.insurance.backend;


import com.insurance.backend.entity.Client;
import com.insurance.backend.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Long> {

    List<Client> findByContractNumber(Long contractNumber);
}
