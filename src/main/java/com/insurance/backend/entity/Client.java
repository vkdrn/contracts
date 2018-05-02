package com.insurance.backend.entity;


import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class Client extends BaseEntity {

    @NotNull
    private String firstName;

    private String patronymic;

    @NotNull
    private String lastName;

    @NotNull
    private LocalDate birthDate;

    @NotNull
    private Integer passportSeries;

    @NotNull
    private Integer passportNumber;

    public Client() {

    }

    public Client(String firstName, String patronymic, String lastName, LocalDate birthDate, Integer passportSeries, Integer passportNumber) {
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

    public Integer getPassportSeries() {
        return passportSeries;
    }

    public void setPassportSeries(Integer passportSeries) {
        this.passportSeries = passportSeries;
    }

    public Integer getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(Integer passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getFullname() {
        return String.format("%s %s %s", getLastName(), getFirstName(), getPatronymic());
    }

    public String getPassportDetails() {
        if (getPassportSeries() == null && getPassportNumber() == null) {
            return "";
        }
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
