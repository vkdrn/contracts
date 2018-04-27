package com.insurance.ui.view;

import com.insurance.backend.entity.Client;
import com.insurance.backend.service.ClientService;
import com.vaadin.data.Binder;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Notification;
import org.springframework.beans.factory.annotation.Autowired;

@SpringView(name = NewClientView.NAME)
public class NewClientView extends NewClientViewDesign implements View {

    public static final String NAME = "new-client";

    private ClientService clientService;

    @Autowired
    public NewClientView(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        Binder<Client> binder = new Binder<>();
        Client client = new Client();
        binder.forField(inpLastname)
                .asRequired("")
                .bind(Client::getLastName, Client::setLastName);
        binder.forField(inpFirstname)
                .asRequired("")
                .bind(Client::getFirstName, Client::setFirstName);
        binder.forField(inpPatronymic)
                .bind(Client::getPatronymic, Client::setPatronymic);
        binder.forField(calBirthdate)
                .asRequired("")
                .bind(Client::getBirthDate, Client::setBirthDate);
        binder.forField(inpPassportSeries)
                .asRequired("")
                .bind(Client::getPassportSeries, Client::setPassportSeries);
        binder.forField(inpPassportNumber)
                .asRequired("")
                .bind(Client::getPassportNumber, Client::setPassportNumber);

        btnSave.addClickListener(click -> {
            binder.validate();
            boolean validated = binder.writeBeanIfValid(client);
            if (validated) {
                Client result = clientService.save(client);
                Notification.show("Успешно сохранено");
                getUI().getNavigator().navigateTo(EditContractView.NAME + "/client=" + result.getId());
            } else {
                Notification.show("Ошибка сохранения. Проверьте правильность заполнения полей",
                        Notification.Type.WARNING_MESSAGE);
            }
        });

        btnCancel.addClickListener(click -> getUI().getNavigator().navigateTo(ContractsView.NAME));
    }
}
