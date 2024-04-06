package com.metrobank.testapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(schema = "metrobank-intern",name = "accounts")
@Data
public class Accounts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="account_id", nullable = false, columnDefinition = "serial")
    private Long accountId;

    //Name
    @Column(name="first_name", nullable = false)
    @NotBlank(message = "First name must not be blank")
    private String firstName;

    @Column(name="middle_name", nullable = false)
    @NotBlank(message = "Middle name must not be blank")
    private String middleName;

    @Column(name="last_name",nullable = false)
    @NotBlank(message = "Last name must not be blank")
    private String lastName;

    //Contacts
    @Column(name="mobile_number",nullable = false)
    @NotBlank(message = "Mobile Number must not be blank")
    private String mobileNumber;

    @Column(name="telephone_number",nullable = true)
    private String telephoneNumber;

    @Column(name="email_address",nullable = false)
    @NotBlank(message = "Email Address must not be blank")
    private String emailAddress;

    //DOB
    @Column(name="date_of_birth",nullable = false)
    @NotBlank(message = "Date of Birth must not be blank")
    private String dateOfBirth;

    //Accounts Type
    @Column(name="checking_account", nullable = false)
    @NotNull(message = "Enter true or false if this is the type of account you want to open")
    private Boolean checkingAccount;

    @Column(name="savings_account", nullable = false)
    @NotNull(message = "Enter true or false if this is the type of account you want to open")
    private Boolean savingsAccount;

    @Column(name="money_market_account", nullable = false)
    @NotNull(message = "Enter true or false if this is the type of account you want to open")
    private Boolean moneyMarketAccount;

    @Column(name="certificate_of_deposit_account", nullable = false)
    @NotNull(message = "Enter true or false if this is the type of account you want to open")
    private Boolean certificateOfDepositAccount;


    //Address
    @Column(name="home_address_street", nullable = false)
    @NotBlank(message = "Street must not be blank")
    private String homeAddress_street;

    @Column(name="home_address_city", nullable = false)
    @NotBlank(message = "City must not be blank")
    private String homeAddress_city;

    @Column(name="home_address_province", nullable = false)
    @NotBlank(message = "Province must not be blank")
    private String homeAddress_province;

    @Column(name="home_address_zip_code", nullable = false)
    @NotBlank(message = "Zipcode must not be blank")
    private String homeAddress_zipCode;

    @Column(name="permanent_address_street", nullable = false)
    @NotBlank(message = "Street must not be blank")
    private String permanentAddress_street;

    @Column(name="permanent_address_city", nullable = false)
    @NotBlank(message = "City must not be blank")
    private String permanentAddress_city;

    @Column(name="permanent_address_province", nullable = false)
    @NotBlank(message = "Province must not be blank")
    private String permanentAddress_province;

    @Column(name="permanent_address_zip_code", nullable = false)
    @NotBlank(message = "Zipcode must not be blank")
    private String permanentAddress_zipCode;

    //MonthlySalary
    @Column(name="monthly_salary", nullable = false)
    @NotNull(message = "Monthly Salary must not be blank")
    private double monthlySalary;


    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="created_at", columnDefinition = "TIMESTAMP DEFAULT current_timestamp")
    private Timestamp accountTimeCreated;

    @Builder
    public Accounts(long accountId, String firstName, String middleName, String lastName, String mobileNumber,String telephoneNumber ,String emailAddress, String dateOfBirth, boolean checkingAccount, boolean savingsAccount, boolean certificateOfDepositAccount, boolean moneyMarketAccount, String homeAddress_street, String homeAddress_city, String homeAddress_province, String homeAddress_zipCode, String permanentAddress_street, String permanentAddress_city, String permanentAddress_province, String permanentAddress_zipCode, double monthlySalary, Timestamp accountTimeCreated){

        //Account ID
        this.accountId = accountId;

        //Name
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;

        //Contacts
        this.mobileNumber = mobileNumber;
        this.telephoneNumber = telephoneNumber;
        this.emailAddress = emailAddress;

        //Date of Birth
        this.dateOfBirth = dateOfBirth;

        //Type of Accounts
        this.checkingAccount = checkingAccount;
        this.savingsAccount = savingsAccount;
        this.moneyMarketAccount = moneyMarketAccount;
        this.certificateOfDepositAccount = certificateOfDepositAccount;

        //Home Address
        this.homeAddress_street = homeAddress_street;
        this.homeAddress_city = homeAddress_city;
        this.homeAddress_province = homeAddress_province;
        this.homeAddress_zipCode = homeAddress_zipCode;

        //Permanent Address
        this.permanentAddress_street = permanentAddress_street;
        this.permanentAddress_city = permanentAddress_city;
        this.permanentAddress_province = permanentAddress_province;
        this.permanentAddress_zipCode = permanentAddress_zipCode;

        //Monthly Salary
        this.monthlySalary = monthlySalary;

        //Account time created
        this.accountTimeCreated = accountTimeCreated;
    }

    public Accounts(){}

}


