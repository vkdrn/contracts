package com.insurance.ui.view;

import com.insurance.backend.entity.Client;
import com.insurance.backend.entity.Contract;
import com.insurance.backend.service.ClientService;
import com.insurance.backend.service.ContractService;
import com.insurance.ui.ContractsUI;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Map;

@SpringView(name = EditContractView.NAME)
public class EditContractView extends EditContractViewDesign implements View {

    public static final String NAME = "edit-contract";

    private ClientService clientService;
    private ContractService contractService;

    private ContractsUI contractsUI;

    @Autowired
    public EditContractView(ClientService clientService, ContractService contractService) {
        this.clientService = clientService;
        this.contractService = contractService;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        contractsUI = (ContractsUI) UI.getCurrent();
        Binder<Contract> premiumBinder;
        if (contractsUI.globalContractExists()) {
            premiumBinder = initPremiumFields(contractsUI.getGlobalContract());
            premiumBinder.readBean(contractsUI.getGlobalContract());
        } else {
            Contract newContract = new Contract();
            contractsUI.setGlobalContract(newContract);
            premiumBinder = initPremiumFields(newContract);
        }
        setClientValue(event);

        Binder<Contract> finalPremiumBinder = premiumBinder;
        btnCalculatePremium.addClickListener(click -> calculatePremium(finalPremiumBinder));

        btnSelectClient.addClickListener(click -> getUI().getNavigator().navigateTo(ClientListView.NAME));
    }


    private void setClientValue(ViewChangeListener.ViewChangeEvent event) {
        inpFullname.setReadOnly(true);
        Map<String, String> parameters = event.getParameterMap();
        if (parameters.containsKey("client")) {
            Long clientId = null;
            try {
                clientId = Long.parseLong(parameters.get("client"));
            } catch (NumberFormatException e) {
                Notification.show("Неверный ID клиента", Notification.Type.ERROR_MESSAGE);
            }

            Client client = clientService.find(clientId);

            if (client != null) {
                inpFullname.setValue(client.getFullname());
            } else {
                Notification.show("Неверный ID клиента", Notification.Type.ERROR_MESSAGE);
            }
        }
    }

    private Binder<Contract> initPremiumFields(Contract contract) {
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
        calPeriodStart.setValue(LocalDate.now());

        binder.forField(calPeriodEnd)
                .asRequired("")
                .withValidator(date -> date.isAfter(calPeriodStart.getValue()) &&
                                date.getYear() - LocalDate.now().getYear() <= 1,
                        "должен быть > срок действия полиса с и не больше года")
                .bind(Contract::getPeriodEnd, Contract::setPeriodEnd);

        inpCalcDate.setValue(LocalDate.now().toString());

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
        }
    }

    private void initFields(Contract contract) {
        Binder<Contract> binder = initPremiumFields(contract);

        binder.forField(inpCalcDate)
                .asRequired("")
                .bind(Contract::getCalcDateInString, Contract::setCalcDateInString);
        inpCalcDate.setReadOnly(true);

        binder.forField(inpPremium)
                .asRequired("")
                .withConverter(new StringToDoubleConverter("Должно быть числом"))
                .withValidator(num -> num >= 0, "Должно быть неотрицательное")
                .bind(Contract::getPremium, Contract::setPremium);
    }
}
