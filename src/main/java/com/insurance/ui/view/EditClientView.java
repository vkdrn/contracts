package com.insurance.ui.view;

import com.insurance.backend.entity.Client;
import com.insurance.backend.service.ClientService;
import com.insurance.ui.ContractsUI;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;

@SpringView(name = EditClientView.NAME)
public class EditClientView extends EditClientViewDesign implements View {

    public static final String NAME = "edit-client";

    private ClientService clientService;

    @Autowired
    public EditClientView(ClientService clientService) {
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
                .withValidator(str -> str.length() == 4, "Необходимо 4 символа")
                .withConverter(new StringToIntegerConverter("Должно быть целым числом"))
                .bind(Client::getPassportSeries, Client::setPassportSeries);
        binder.forField(inpPassportNumber)
                .asRequired("")
                .withValidator(str -> str.length() == 4, "Необходимо 4 символа")
                .withConverter(new StringToIntegerConverter("Должно быть целым числом"))
                .bind(Client::getPassportNumber, Client::setPassportNumber);

        binder.readBean(((ContractsUI) UI.getCurrent()).getGlobalContract().getClient());

        btnSave.addClickListener(click -> {
            binder.validate();
            boolean validated = binder.writeBeanIfValid(client);
            if (validated) {
                Client result = clientService.save(client);
                Notification.show("Успешно сохранено");
                ((ContractsUI) UI.getCurrent()).getGlobalContract().setClient(result);
                getUI().getNavigator().navigateTo(EditContractView.NAME);
            } else {
                Notification.show("Ошибка сохранения. Проверьте правильность заполнения полей",
                        Notification.Type.WARNING_MESSAGE);
            }
        });

        btnCancel.addClickListener(click -> getUI().getNavigator().navigateTo(EditContractView.NAME));
    }
}
