package com.insurance.ui;


import com.insurance.backend.entity.Contract;
import com.insurance.ui.view.ContractsView;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.UI;

@SpringUI
@SpringViewDisplay
@Theme("valo")
public class ContractsUI extends UI {

    Contract globalContract;

    @Override
    protected void init(VaadinRequest request) {
        getUI().getNavigator().navigateTo(ContractsView.NAME);
    }

    public Contract getGlobalContract() {
        return globalContract;
    }

    public void setGlobalContract(Contract globalContract) {
        this.globalContract = globalContract;
    }

    public boolean globalContractExists() {
        return globalContract != null;
    }

    public boolean globalContractIsNew() {
        return globalContract.getId() == null;
    }

    public boolean globalClientExists() {
        return getGlobalContract().getClient() != null;
    }

    public boolean isNew() {
        return globalContractExists() && globalContract.getId() == null;
    }
}
