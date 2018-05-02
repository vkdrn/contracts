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

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;

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

        Binder<Contract> premiumBinder = initPremiumFields();
        Binder<Client> clientBinder = initClientFields();
        Binder<Contract> contractFieldsBinder = initContractFields(new Binder<>());

        //Если договор выбран, заполняем поля
        if (contractsUI.currentContractExist()) {
            Contract currentContract = contractService.find(contractsUI.getCurrentContractId());
            premiumBinder.readBean(currentContract);
            contractFieldsBinder.readBean(currentContract);
        }

        //Если клиент выбран, заполняем поля
        if (contractsUI.currentClientExist()) {
            Client currentClient = clientService.find(contractsUI.getCurrentClientId());
            inpFullname.setValue(currentClient.getFullname());
            clientBinder.readBean(currentClient);
        }

        btnCalculatePremium.addClickListener(click -> calculatePremium(premiumBinder));

        btnSelectClient.addClickListener(click -> getUI().getNavigator().navigateTo(ClientListView.NAME));
        if (!contractsUI.currentClientExist()) {
            btnEditClient.setEnabled(false);
        }
        btnEditClient.addClickListener(click -> getUI().getNavigator().navigateTo(EditClientView.NAME));

        btnSave.addClickListener(click -> saveContract(premiumBinder, clientBinder));

        btnCancel.addClickListener(click -> {
            getUI().getNavigator().navigateTo(ContractsView.NAME);
            contractsUI.setCurrentContractId(null);
            contractsUI.setCurrentClientId(null);
        });
    }


    private Binder<Client> initClientFields() {
        //Поля "Страхователь"
        inpFullname.setReadOnly(true);

        Binder<Client> binder = new Binder<>();
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
                .bind(Client::getPassportNumber, Client::setPassportNumber);

        return binder;
    }

    private Binder<Contract> initPremiumFields() {
        //Поля блока "Расчет"
        Binder<Contract> binder = new Binder<>();

        binder.forField(inpInsuranceSum)
                .asRequired("")
                .withConverter(new StringToIntegerConverter("Должно быть целым числом") {
                    @Override
                    protected java.text.NumberFormat getFormat(Locale locale) {
                        NumberFormat format = super.getFormat(locale);
                        format.setGroupingUsed(false);
                        return format;
                    }
                })
                .bind(Contract::getSum, Contract::setSum);

        comboPropertyType.setItems("квартира", "дом", "комната");
        binder.forField(comboPropertyType)
                .asRequired("")
                .bind(Contract::getPropertyType, Contract::setPropertyType);

        binder.forField(inpYearBuilt)
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
                .withValidator(integer -> integer <= LocalDate.now().getYear(), "Не больше текущего года")
                .bind(Contract::getYearBuilt, Contract::setYearBuilt);

        binder.forField(inpSquare)
                .asRequired("")
                .withConverter(new StringToDoubleConverter("Должно быть числом") {
                    @Override
                    protected java.text.NumberFormat getFormat(Locale locale) {
                        NumberFormat format = super.getFormat(locale);
                        format.setGroupingUsed(false);
                        return format;
                    }
                })
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
                                date.isBefore(calPeriodStart.getValue().plusYears(1L)),
                        "должен быть > срок действия полиса с и не больше года")
                .bind(Contract::getPeriodEnd, Contract::setPeriodEnd);

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
                square = Double.parseDouble(inpSquare.getValue().replace(",", "."));
            } catch (NumberFormatException e) {
                Notification.show("Указаны неправильные данные", Notification.Type.ERROR_MESSAGE);
            }

            Double premium = contractService.calculatePremium(insSum, calPeriodStart.getValue(), calPeriodEnd.getValue(),
                    comboPropertyType.getValue(), year, square);
            inpPremium.clear();
            inpPremium.setValue(String.valueOf(premium).replace(".", ","));
            calCalcDate.setValue(LocalDate.now());
        }
    }

    private Binder<Contract> initContractFields(Binder<Contract> fieldBinder) {
        calCalcDate.setReadOnly(true);
        fieldBinder.forField(calCalcDate)
                .asRequired("")
                .bind(Contract::getCalcDate, Contract::setCalcDate);

        fieldBinder.forField(inpPremium)
                .asRequired("")
                .withConverter(new StringToDoubleConverter("Должно быть числом") {
                    @Override
                    protected java.text.NumberFormat getFormat(Locale locale) {
                        NumberFormat format = super.getFormat(locale);
                        format.setGroupingUsed(false);
                        return format;
                    }
                })
                .withValidator(num -> num >= 0, "Должно быть неотрицательное")
                .bind(Contract::getPremium, Contract::setPremium);

        fieldBinder.forField(inpContractNumber)
                .asRequired("")
                .withValidator(str -> str.length() == 6, "Необходимо 6 символов")
                .withConverter(new StringToLongConverter("Должно быть целым числом") {
                    @Override
                    protected java.text.NumberFormat getFormat(Locale locale) {
                        NumberFormat format = super.getFormat(locale);
                        format.setGroupingUsed(false);
                        return format;
                    }
                })
                .withValidator(num -> {
                    if (contractsUI.currentContractExist()) {
                        return true;
                    } else {
                        return !contractService.numberExists(num);
                    }
                }, "Договор с таким номером существует")
                .bind(Contract::getContractNumber, Contract::setContractNumber);

        if (!contractsUI.currentContractExist()) {
            calContractDate.setValue(LocalDate.now());
        }
        fieldBinder.forField(calContractDate)
                .asRequired("")
                .bind(Contract::getContractDate, Contract::setContractDate);


        comboCountry.setSelectedItem("Россия");
        fieldBinder.forField(comboCountry)
                .asRequired("")
                .bind(Contract::getCountry, Contract::setCountry);

        fieldBinder.forField(inpPostalCode)
                .bind(Contract::getPostalCode, Contract::setPostalCode);

        fieldBinder.forField(inpRegion)
                .asRequired("")
                .bind(Contract::getRegion, Contract::setRegion);

        fieldBinder.forField(inpArea)
                .bind(Contract::getArea, Contract::setArea);

        fieldBinder.forField(inpCity)
                .asRequired("")
                .bind(Contract::getCity, Contract::setCity);

        fieldBinder.forField(inpStreet)
                .asRequired("")
                .bind(Contract::getStreet, Contract::setStreet);

        fieldBinder.forField(inpHouse)
                .bind(Contract::getHouse, Contract::setHouse);

        fieldBinder.forField(inpHousing)
                .bind(Contract::getHousing, Contract::setHousing);

        fieldBinder.forField(inpBuilding)
                .bind(Contract::getBuilding, Contract::setBuilding);

        fieldBinder.forField(inpApartment)
                .asRequired("")
                .withConverter(new StringToIntegerConverter("Должно быть целым числом") {
                    @Override
                    protected java.text.NumberFormat getFormat(Locale locale) {
                        NumberFormat format = super.getFormat(locale);
                        format.setGroupingUsed(false);
                        return format;
                    }
                })
                .bind(Contract::getApartment, Contract::setApartment);

        fieldBinder.forField(inpComment)
                .bind(Contract::getComment, Contract::setComment);

        return fieldBinder;
    }

    private Contract getContract() {
        if (contractsUI.currentContractExist()) {
            return contractService.find(contractsUI.getCurrentContractId());
        }

        return new Contract();
    }

    private void saveContract(Binder<Contract> premiumBinder, Binder<Client> clientBinder) {
        Binder<Contract> allFieldsBinder = initContractFields(premiumBinder);

        allFieldsBinder.validate();
        Contract contract = getContract();
        boolean contractValidated = allFieldsBinder.writeBeanIfValid(contract);

        clientBinder.validate();
        Client client = clientService.find(contractsUI.getCurrentClientId());
        boolean clientValidated = clientBinder.writeBeanIfValid(client);

        if (contractValidated && clientValidated) {
            Client resultClient = clientService.save(client);
            contract.setClient(resultClient);
            Contract result = contractService.save(contract);
            Notification.show("Договор успешно сохранен");
            contractsUI.setCurrentContractId(null);
            contractsUI.setCurrentClientId(null);
            getUI().getNavigator().navigateTo(ContractsView.NAME);
        } else {
            Notification.show("Ошибка сохранения. Проверьте правильность заполнения полей",
                    Notification.Type.WARNING_MESSAGE);
        }
    }
}
