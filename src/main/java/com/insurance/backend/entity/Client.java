package com.insurance.backend.entity;


import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
public class Client extends AbstractEntity {

    private String firstName;

    private String patronymic;

    private String lastName;

    private LocalDate birthDate;

    private String passportSeries;

    private String passportNumber;

    public Client() {

    }

    public Client(String firstName, String patronymic, String lastName, LocalDate birthDate, String passportSeries, String passportNumber) {
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.passportSeries = passportSeries;
        this.passportNumber = passportNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPassportSeries() {
        return passportSeries;
    }

    public void setPassportSeries(String passportSeries) {
        this.passportSeries = passportSeries;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getFullname() {
        return String.format("%s %s %s", getLastName(), getFirstName(), getPatronymic());
    }

    public String getPassportDetails() {
        return String.valueOf(getPassportSeries()) + " " + String.valueOf(getPassportNumber());
    }

    @Override
    public String toString() {
        return "Client{" +
                "firstName='" + firstName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate +
                ", passportSeries=" + passportSeries +
                ", passportNumber=" + passportNumber +
                '}';
    }
}
