package com.insurance.backend.entity;


import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
public class Contract extends AbstractEntity {

    private Integer sum;

    private String propertyType;

    private Integer yearBuilt;

    private Double square;

    private LocalDate periodStart;

    private LocalDate periodEnd;

    private LocalDate calcDate;

    private Double premium;

    private LocalDate date;

    @NotNull
    @OneToOne
    private Client client;

    public Contract() {

    }

    public Contract(Integer sum, String propertyType, Integer yearBuilt, Double square, LocalDate periodStart, LocalDate periodEnd, LocalDate calcDate, Double premium, LocalDate date, Client client) {
        this.sum = sum;
        this.propertyType = propertyType;
        this.yearBuilt = yearBuilt;
        this.square = square;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.calcDate = calcDate;
        this.premium = premium;
        this.date = date;
        this.client = client;
    }

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public Integer getYearBuilt() {
        return yearBuilt;
    }

    public void setYearBuilt(Integer yearBuilt) {
        this.yearBuilt = yearBuilt;
    }

    public Double getSquare() {
        return square;
    }

    public void setSquare(Double square) {
        this.square = square;
    }

    public LocalDate getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(LocalDate periodStart) {
        this.periodStart = periodStart;
    }

    public LocalDate getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(LocalDate periodEnd) {
        this.periodEnd = periodEnd;
    }

    public LocalDate getCalcDate() {
        return calcDate;
    }

    public void setCalcDate(LocalDate calcDate) {
        this.calcDate = calcDate;
    }

    public String getCalcDateInString() {
        return calcDate.toString();
    }

    public void setCalcDateInString(String calcDate) {
        this.calcDate = LocalDate.parse(calcDate);
    }

    public Double getPremium() {
        return premium;
    }

    public void setPremium(Double premium) {
        this.premium = premium;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getFullPeriod() {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return String.format("%s-%s", getPeriodStart().format(pattern), getPeriodEnd().format(pattern));
    }

    @Override
    public String toString() {
        return "Contract{" +
                "sum=" + sum +
                ", propertyType='" + propertyType + '\'' +
                ", yearBuilt=" + yearBuilt +
                ", square=" + square +
                ", periodStart=" + periodStart +
                ", periodEnd=" + periodEnd +
                ", calcDate=" + calcDate +
                ", premium=" + premium +
                ", date=" + date +
                ", client=" + client +
                '}';
    }
}
