package com.insurance.ui.view;

import com.insurance.backend.entity.Client;
import com.insurance.backend.entity.Contract;
import com.insurance.backend.service.ContractService;
import com.insurance.ui.ContractsUI;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

@SpringView(name = ContractsView.NAME)
public class ContractsView extends VerticalLayout implements View {

    public static final String NAME = "contracts";

    private ContractService contractService;

    private Grid<Contract> grid;

    @Autowired
    public ContractsView(ContractService contractService) {
        this.contractService = contractService;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        setupLayout();
        addHeader();
        addActionButtons();
        addGrid();
    }

    private void setupLayout() {
        setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
    }

    private void addHeader() {
        Label header = new Label("Договоры");
        header.addStyleName(ValoTheme.LABEL_H1);
        addComponent(header);
    }

    private void addActionButtons() {
        HorizontalLayout horizontal = new HorizontalLayout();
        Button createButton = new Button("Создать договор");
        createButton.addClickListener(click -> getUI().getNavigator().navigateTo(EditContractView.NAME));

        Button openButton = new Button("Открыть договор");
        openButton.addClickListener(click -> selectContract());

        horizontal.addComponents(createButton, openButton);
        addComponent(horizontal);
    }

    private void addGrid() {
        grid = new Grid<Contract>("", contractService.findAll());
        grid.addColumn(Contract::getId).setCaption("Номер договора");
        grid.addColumn(Contract::getDate).setCaption("Дата заключения");
        grid.addColumn(e -> e.getClient().getFullname()).setCaption("Страхователь");
        grid.addColumn(Contract::getPremium).setCaption("Премия");
        grid.addColumn(Contract::getFullPeriod).setCaption("Срок действия");
        grid.setWidth("1000px");
        addComponent(grid);
    }

    private void selectContract() {
        if (grid.getSelectedItems().size() > 0) {
            Contract selected = grid.getSelectedItems().stream().findFirst().get();
            ((ContractsUI)UI.getCurrent()).setGlobalContract(selected);
            getUI().getNavigator().navigateTo(EditContractView.NAME);
        }
    }
}
