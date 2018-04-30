package com.insurance.ui.view;

import com.insurance.backend.entity.Client;
import com.insurance.backend.service.ClientService;
import com.insurance.ui.ContractsUI;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringView(name = ClientListView.NAME)
public class ClientListView extends ClientListViewDesign implements View {

    public static final String NAME = "client-list";

    private Grid<Client> gridClient;

    private ClientService clientService;

    @Autowired
    public ClientListView(ClientService clientService) {
        this.clientService = clientService;
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        initGrid(clientService.findAll());
        btnSearch.addClickListener(click -> search());
        btnSelect.addClickListener(click -> selectClient());
        btnNew.addClickListener(click -> getUI().getNavigator().navigateTo(NewClientView.NAME));
        btnClose.addClickListener(click -> getUI().getNavigator().navigateTo(EditContractView.NAME));
    }

    private void initGrid(List<Client> clientList) {
        gridLayout.removeComponent(grid);
        gridClient = new Grid<Client>("", clientList);
        gridLayout.addComponent(gridClient, 0, 1);
        gridClient.setWidth("1000px");
        gridClient.addColumn(Client::getFullname).setCaption("ФИО");
        gridClient.addColumn(c -> c.getBirthDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .setCaption("Дата рождения");
        gridClient.addColumn(Client::getPassportDetails).setCaption("Паспортные данные");
    }

    private void selectClient() {
        if (gridClient.getSelectedItems().size() > 0) {
            Client selected = gridClient.getSelectedItems().stream().findFirst().get();
            ((ContractsUI) UI.getCurrent()).setCurrentClientId(selected.getId());
            getUI().getNavigator().navigateTo(EditContractView.NAME);
        }
    }

    private void search() {
        List<Client> clients = clientService.search(inpFirstname.getValue(), inpPatronymic.getValue(), inpLastname.getValue());
        gridClient.setItems(clients);
    }
}
