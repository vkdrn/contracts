package com.insurance.ui.view;

import com.insurance.backend.entity.Client;
import com.insurance.backend.entity.Contract;
import com.insurance.backend.service.ClientService;
import com.insurance.backend.service.ContractService;
import com.insurance.ui.ContractsUI;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.converter.StringToLongConverter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@SpringView(name = EditContractView.NAME)
public class EditContractView extends EditContractViewDesign implements View {

    public static final String NAME = "edit-contract";

    private ClientService clientService;
    private ContractService contractService;

    private ContractsUI contractsUI;

    private Binder<Contract> fieldBinder;

    @Autowired
    public EditContractView(ClientService clientService, ContractService contractService) {
        this.clientService = clientService;
        this.contractService = contractService;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        contractsUI = (ContractsUI) UI.getCurrent();
        if (contractsUI.globalContractExists() && !contractsUI.globalContractIsNew()) {
            fieldBinder = initPremiumFields();
            fieldBinder.readBean(contractsUI.getGlobalContract());
        } else if (!contractsUI.globalContractExists()) {
            Contract newContract = new Contract();
            contractsUI.setGlobalContract(newContract);
            fieldBinder = initPremiumFields();
        } else if (contractsUI.globalContractExists() && contractsUI.globalContractIsNew()) {
            fieldBinder = initPremiumFields();
        }

        if(contractsUI.globalContractExists() && contractsUI.globalClientExists()) {
            Binder<Client> clientBinder = initClientFields();
            clientBinder.readBean(contractsUI.getGlobalContract().getClient());
        } else {
            Binder<Client> clientBinder = initClientFields();
        }

        initFields();

        Binder<Contract> finalPremiumBinder = fieldBinder;
        btnCalculatePremium.addClickListener(click -> calculatePremium(finalPremiumBinder));

        btnSelectClient.addClickListener(click -> getUI().getNavigator().navigateTo(ClientListView.NAME));
        if (!contractsUI.globalContractExists() || !contractsUI.globalClientExists()) {
            btnEditClient.setEnabled(false);
        }
        btnEditClient.addClickListener(click -> getUI().getNavigator().navigateTo(EditClientView.NAME));
    }


    private Binder<Client> initClientFields() {
        //Поля "Страхователь"
        inpFullname.setReadOnly(true);
        if (contractsUI.globalClientExists()) {
            inpFullname.setValue(contractsUI.getGlobalContract().getClient().getFullname());
        }

        Binder<Client> binder = new Binder<>();
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
                .withValidator(str -> str.length() == 6, "Необходимо 6 символов")
                .withConverter(new StringToIntegerConverter("Должно быть целым числом"))
                .bind(Client::getPassportNumber, Client::setPassportNumber);

        return binder;
    }

    private Binder<Contract> initPremiumFields() {
        //Поля блока "Расчет"
        Binder<Contract> binder = new Binder<>();
        binder.forField(inpInsuranceSum)
                .asRequired("")
                .withConverter(new StringToIntegerConverter("Должно быть целым числом"))
                .bind(Contract::getSum, Contract::setSum);
        comboPropertyType.setItems("квартира", "дом", "комната");
        binder.forField(comboPropertyType)
                .asRequired("")
                .bind(Contract::getPropertyType, Contract::setPropertyType);
        binder.forField(inpYearBuilt)
                .asRequired("")
                .withValidator(str -> str.length() == 4, "Необходимо 4 символа")
                .withConverter(new StringToIntegerConverter("Должно быть целым числом"))
                .withValidator(integer -> integer <= LocalDate.now().getYear(), "Не больше текущего года")
                .bind(Contract::getYearBuilt, Contract::setYearBuilt);
        binder.forField(inpSquare)
                .asRequired("")

                .withConverter(new StringToDoubleConverter("Должно быть числом"))
                .bind(Contract::getSquare, Contract::setSquare);
        binder.forField(calPeriodStart)
                .asRequired("")
                .withValidator(date -> date.isAfter(LocalDate.now()) || date.isEqual(LocalDate.now()),
                        "Должен быть >= текущей дате")
                .bind(Contract::getPeriodStart, Contract::setPeriodStart);
        calPeriodStart.setDefaultValue(LocalDate.now());

        binder.forField(calPeriodEnd)
                .asRequired("")
                .withValidator(date -> date.isAfter(calPeriodStart.getValue()) &&
                                date.getYear() - LocalDate.now().getYear() <= 1,
                        "должен быть > срок действия полиса с и не больше года")
                .bind(Contract::getPeriodEnd, Contract::setPeriodEnd);

        fieldBinder.forField(inpCalcDate)
                .asRequired("")
                .bind(Contract::getCalcDateInString, Contract::setCalcDateInString);
        inpCalcDate.setReadOnly(true);

        fieldBinder.forField(inpPremium)
                .asRequired("")
                .withConverter(new StringToDoubleConverter("Должно быть числом"))
                .withValidator(num -> num >= 0, "Должно быть неотрицательное")
                .bind(Contract::getPremium, Contract::setPremium);

        return binder;
    }

    private void calculatePremium(Binder<Contract> premiumBinder) {
        if (premiumBinder.validate().isOk()) {
            Integer insSum = 0;
            Integer year = 0;
            Double square = 0.0;
            try {
                insSum = Integer.parseInt(inpInsuranceSum.getValue());
                year = Integer.parseInt(inpYearBuilt.getValue());
                square = Double.parseDouble(inpSquare.getValue());
            } catch (NumberFormatException e) {
                Notification.show("Указаны неправильные данные", Notification.Type.ERROR_MESSAGE);
            }

            Double premium = contractService.calculatePremium(insSum, calPeriodStart.getValue(), calPeriodEnd.getValue(),
                    comboPropertyType.getValue(), year, square);
            inpPremium.setValue(String.valueOf(premium));
            inpCalcDate.setValue(LocalDate.now().toString());
        }
    }

    private void initFields() {
        fieldBinder.forField(inpContractNumber)
                .asRequired("")
                .withValidator(str -> str.length() == 6, "Необходимо 6 символов")
                .withConverter(new StringToLongConverter("Должно быть целым числом"))
                .withValidator(num -> !contractService.exists(num), "Договор с таким номером существует")
                .bind(Contract::getContractNumber, Contract::setContractNumber);

        calContractDate.setDefaultValue(LocalDate.now());
        fieldBinder.forField(calContractDate)
                .asRequired("")
                .bind(Contract::getContractDate, Contract::setContractDate);


        comboCountry.setItems();
    }
}
