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

import java.text.NumberFormat;
import java.util.Locale;

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
        ContractsUI contractsUI = (ContractsUI) UI.getCurrent();
        Binder<Client> binder = initFields();

        Client currentClient = new Client();
        if (contractsUI.currentClientExist()) {
            currentClient = clientService.find(contractsUI.getCurrentClientId());
            binder.readBean(currentClient);
        }

        Client finalCurrentClient = currentClient;
        btnSave.addClickListener(click -> saveClient(binder, finalCurrentClient));

        btnCancel.addClickListener(click -> getUI().getNavigator().navigateTo(EditContractView.NAME));
    }

    public Binder<Client> initFields() {
        Binder<Client> binder = new Binder<>();

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
                .withConverter(new StringToIntegerConverter("Должно быть целым числом") {
                    @Override
                    protected java.text.NumberFormat getFormat(Locale locale) {
                        NumberFormat format = super.getFormat(locale);
                        format.setGroupingUsed(false);
                        return format;
                    }
                })
                .withValidator(num -> num > 0, "Допускается только положительное число")
                .bind(Client::getPassportSeries, Client::setPassportSeries);

        binder.forField(inpPassportNumber)
                .asRequired("")
                .withValidator(str -> str.length() == 6, "Необходимо 6 символов")
                .withConverter(new StringToIntegerConverter("Должно быть целым числом") {
                    @Override
                    protected java.text.NumberFormat getFormat(Locale locale) {
                        NumberFormat format = super.getFormat(locale);
                        format.setGroupingUsed(false);
                        return format;
                    }
                })
                .withValidator(num -> num > 0, "Допускается только положительное число")
                .bind(Client::getPassportNumber, Client::setPassportNumber);

        return binder;
    }

    public void saveClient(Binder<Client> binder, Client client) {
        binder.validate();
        boolean validated = binder.writeBeanIfValid(client);
        if (validated) {
            Client result = clientService.save(client);
            Notification.show("Успешно сохранено");
            ((ContractsUI) UI.getCurrent()).setCurrentClientId(result.getId());
            getUI().getNavigator().navigateTo(EditContractView.NAME);
        } else {
            Notification.show("Ошибка сохранения. Проверьте правильность заполнения полей",
                    Notification.Type.WARNING_MESSAGE);
        }
    }
}
