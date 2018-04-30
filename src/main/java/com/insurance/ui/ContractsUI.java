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

    private Long currentContractId;
    private Long currentClientId;

    @Override
    protected void init(VaadinRequest request) {
        getUI().getNavigator().navigateTo(ContractsView.NAME);
    }

    public Long getCurrentContractId() {
        return currentContractId;
    }

    public void setCurrentContractId(Long currentContractId) {
        this.currentContractId = currentContractId;
    }

    public Long getCurrentClientId() {
        return currentClientId;
    }

    public void setCurrentClientId(Long currentClientId) {
        this.currentClientId = currentClientId;
    }

    public boolean currentContractExist() {
        return currentContractId != null;
    }

    public boolean currentClientExist() {
        return currentClientId != null;
    }
}
