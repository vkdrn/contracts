package com.insurance.backend.entity;


import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
public class Contract extends BaseEntity {

    private Integer sum;

    private String propertyType;

    private Integer yearBuilt;

    private Double square;

    private LocalDate periodStart;

    private LocalDate periodEnd;

    private LocalDate calcDate;

    private Double premium;

    private Long contractNumber;

    private LocalDate contractDate;

    private String country;

    private String postalCode;

    private String region;

    private String area;

    private String city;

    private String street;

    private String house;

    private String housing;

    private String building;

    private Integer apartment;

    private String comment;

    @NotNull
    @OneToOne
    private Client client;

    public Contract() {

    }

    public Contract(Integer sum, String propertyType, Integer yearBuilt, Double square, LocalDate periodStart, LocalDate periodEnd, LocalDate calcDate, Double premium, Long contractNumber, LocalDate contractDate, String country, String postalCode, String region, String area, String city, String street, String house, String housing, String building, Integer apartment, String comment, Client client) {
        this.sum = sum;
        this.propertyType = propertyType;
        this.yearBuilt = yearBuilt;
        this.square = square;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.calcDate = calcDate;
        this.premium = premium;
        this.contractNumber = contractNumber;
        this.contractDate = contractDate;
        this.country = country;
        this.postalCode = postalCode;
        this.region = region;
        this.area = area;
        this.city = city;
        this.street = street;
        this.house = house;
        this.housing = housing;
        this.building = building;
        this.apartment = apartment;
        this.comment = comment;
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

    public Double getPremium() {
        return premium;
    }

    public void setPremium(Double premium) {
        this.premium = premium;
    }

    public LocalDate getContractDate() {
        return contractDate;
    }

    public void setContractDate(LocalDate contractDate) {
        this.contractDate = contractDate;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Long getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(Long contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getFullPeriod() {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return String.format("%s-%s", getPeriodStart().format(pattern), getPeriodEnd().format(pattern));
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getHousing() {
        return housing;
    }

    public void setHousing(String housing) {
        this.housing = housing;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public Integer getApartment() {
        return apartment;
    }

    public void setApartment(Integer apartment) {
        this.apartment = apartment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
                ", contractNumber=" + contractNumber +
                ", contractDate=" + contractDate +
                ", country='" + country + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", region='" + region + '\'' +
                ", area='" + area + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", house=" + house +
                ", housing='" + housing + '\'' +
                ", building='" + building + '\'' +
                ", apartment=" + apartment +
                ", comment='" + comment + '\'' +
                ", client=" + client +
                '}';
    }
}
