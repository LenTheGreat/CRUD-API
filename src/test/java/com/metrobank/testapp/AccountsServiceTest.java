package com.metrobank.testapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metrobank.testapp.dto.AccountsDTO;
import com.metrobank.testapp.model.Accounts;
import com.metrobank.testapp.repository.AccountsRepository;
import com.metrobank.testapp.service.AccountService;
import com.metrobank.testapp.service.impl.AccountServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.security.auth.login.AccountNotFoundException;
import java.lang.reflect.Array;
import java.util.*;

import org.hamcrest.collection.IsEmptyCollection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc

public class AccountsServiceTest {

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private AccountsRepository accountsRepository;
    @InjectMocks
    private AccountServiceImpl accountService;
    private  Accounts accounts;

    @Test //Positive
    void shouldReturnAllAccounts(){

        when(accountsRepository.findAll()).thenReturn(List.of(new Accounts(), new Accounts())); // Returning the List of All Accounts when accessing the Repository

        assertThat(accountService.findAll()).hasSize(2); // Assume the total number of accounts
        verify(accountsRepository, times(1)).findAll(); // expected # of times the findALl() method is called in the repository
        verifyNoMoreInteractions(accountsRepository); // validate if there is no more interaction with the repository
    }

    @Test  //Negative
    void shouldNotReturnAllAccounts(){
        when(accountService.findAll()).thenReturn(Collections.emptyList());
        assertThat(accountService.findAll().isEmpty());
        verify(accountsRepository, times(1)).findAll();
        verifyNoMoreInteractions(accountsRepository);
    }
    @Test //Positive
    void shouldFindAnExistingAccountById(){
        long accountIdtoFind = 5L; // AccountId to Find
        Accounts existingAccount = new Accounts();
        existingAccount.setAccountId(accountIdtoFind);

        // Assert that when findById() is called on the accountsRepository with the specific ID, return null
        when(accountsRepository.findById(accountIdtoFind)).thenReturn(existingAccount);

        // Call the findById() method of the accountService with the specific ID
        Accounts foundAccount = accountService.findById(accountIdtoFind);

        //Assuming that the returned Account is null
        assertThat(foundAccount).isEqualTo(existingAccount);

        // Verify that the findById() method of the repository was called exactly once with the specific ID
        verify(accountsRepository, times(1)).findById(accountIdtoFind);

        // Verify that there are no more interactions with the repository
        verifyNoMoreInteractions(accountsRepository);
    }

    @Test //Negative
    void shouldNotFindANonExistingAccountId(){
        long accountId = 0l;

        when(accountService.findById(accountId)).thenReturn(null);
        assertThat(accountService.findById(accountId)).isNull();
    }

    @Test//Positive
    void shouldDeleteUserWhenGivenIdIsFound(){
        long existingId = 56l;
        Accounts existingAccount =new Accounts();
        //existingAccount.setAccountId(existingId);
        //when(accountService.findById(existingId)).thenReturn(existingAccount);
        accountService.deleteAccount(existingId);

        verify(accountsRepository).deleteById(existingId);
    }

    @Test//Negative
    void shouldNotDeleteUserWhenGivenANonExistentId(){
        //Given
        long nonExistingAccountId = 56l;

        doThrow(EmptyResultDataAccessException.class)
                .when(accountsRepository)
                .deleteById(nonExistingAccountId);

        assertThrows(EmptyResultDataAccessException.class,
                ()-> accountService.deleteAccount(nonExistingAccountId),
                "Failed Test");


        verify(accountsRepository
                ,times(1))
                .deleteById(nonExistingAccountId);

    }

    @Test //Positive
    void shouldCreateAnAccount(){

        Accounts newAccount = new Accounts();
        newAccount.setAccountId(10l);
        newAccount.setFirstName("TestFirstName");
        newAccount.setMiddleName("TestMiddleName");
        newAccount.setLastName("TestLastName");

        //when(accountService.createAccount(newAccounts)).thenReturn(newAccounts);
        when(accountsRepository.save(newAccount)).thenReturn(newAccount);

        Accounts createdAccount = accountService.createAccount(newAccount);

        verify(accountsRepository).save(newAccount);

        assertEquals(newAccount, createdAccount);
    }

    @Test//Negative
    void shouldNotCreateAnAccount(){
        Accounts newAccount = new Accounts();

        doThrow(RuntimeException.class).when(accountsRepository).save(newAccount);

        assertThrows(RuntimeException.class,
                ()-> accountService.createAccount(newAccount)
                ,"Failed Runtime Exception");
    }

    @Test//Positive
    void shouldFindAnAccountByMobileNumber(){
        Accounts existingAccount = new Accounts();
        String existingMobileNumber ="7";
        existingAccount.setMobileNumber(existingMobileNumber);
        when (accountService.findByMobileNumber(existingMobileNumber)).thenReturn(existingAccount);
        assertThat(existingAccount.getMobileNumber()).isEqualTo(existingMobileNumber);
    }

    @Test//Negative
    void shouldNotFindAnAccountByNonExistingMobileNumber(){
        String nonExistingMobileNumber = "Non Existing Number";

        when(accountsRepository.findByMobileNumber(nonExistingMobileNumber)).thenReturn(null);
        Accounts foundAccount = accountService.findByMobileNumber(nonExistingMobileNumber);

        assertNull(foundAccount);
    }

    @Test//Positive
    void findingAnAccountByEmailAddress(){
        Accounts existingAccount = new Accounts();
        String existingEmailAddress ="test@email.com";
        existingAccount.setEmailAddress(existingEmailAddress);
        when (accountService.findByEmailAddress(existingEmailAddress)).thenReturn(existingAccount);
        assertThat(existingAccount.getEmailAddress()).isEqualTo(existingEmailAddress);
    }

    @Test//Negative
    void shouldNotFindAnAccountByNonExistingEmailAddress(){
        String nonExistingEmailAddress = "Non Existing Email Address";

        when(accountsRepository.findByEmailAddress(nonExistingEmailAddress)).thenReturn(null);
        Accounts foundAccount = accountService.findByEmailAddress(nonExistingEmailAddress);

        assertNull(foundAccount);
    }

    @Test
    void shouldUpdateAnExistingAccount(){
        Accounts existingAccount = new Accounts();
        long accountId = 6l;
        existingAccount.setAccountId(accountId);
        existingAccount.setFirstName("Sun");
        existingAccount.setMiddleName("K.");
        existingAccount.setLastName("Wukong");

        existingAccount.setMobileNumber("45");
        existingAccount.setTelephoneNumber("2564");
        existingAccount.setEmailAddress("test@email.com");

        existingAccount.setMonthlySalary(56368);

        existingAccount.setDateOfBirth("March 7 2007");

        existingAccount.setCheckingAccount(false);
        existingAccount.setSavingsAccount(true);
        existingAccount.setMoneyMarketAccount(false);
        existingAccount.setCertificateOfDepositAccount(false);

        existingAccount.setHomeAddress_city("Cainta");
        existingAccount.setHomeAddress_street("Mangahan");
        existingAccount.setHomeAddress_province("Rizal");
        existingAccount.setHomeAddress_zipCode("1950");

        existingAccount.setPermanentAddress_city("Cainta");
        existingAccount.setPermanentAddress_street("Mangahan");
        existingAccount.setPermanentAddress_province("Rizal");
        existingAccount.setPermanentAddress_zipCode("1950");

        Accounts updatedAccount = new Accounts();
        updatedAccount.setAccountId(accountId);
        updatedAccount.setFirstName("Moon");
        updatedAccount.setMiddleName("K.");
        updatedAccount.setLastName("Wukong");

        updatedAccount.setMobileNumber("384");
        updatedAccount.setTelephoneNumber("2564");
        updatedAccount.setEmailAddress("test@3.com");

        updatedAccount.setMonthlySalary(56368);

        updatedAccount.setDateOfBirth("March 7 2007");

        updatedAccount.setCheckingAccount(false);
        updatedAccount.setSavingsAccount(true);
        updatedAccount.setMoneyMarketAccount(false);
        updatedAccount.setCertificateOfDepositAccount(false);

        updatedAccount.setHomeAddress_city("Cainta");
        updatedAccount.setHomeAddress_street("Mangahan");
        updatedAccount.setHomeAddress_province("Rizal");
        updatedAccount.setHomeAddress_zipCode("1950");

        updatedAccount.setPermanentAddress_city("Cainta");
        updatedAccount.setPermanentAddress_street("Mangahan");
        updatedAccount.setPermanentAddress_province("Rizal");
        updatedAccount.setPermanentAddress_zipCode("1950");

        when(accountsRepository.findById(accountId)).thenReturn(updatedAccount);
        when(accountsRepository.save(updatedAccount)).thenReturn(updatedAccount);

        Accounts returnedAccount = accountService.updateAccount(accountId, updatedAccount);

        assertEquals(updatedAccount, returnedAccount);

        verify(accountsRepository).findById(accountId);
        verify(accountsRepository).save(returnedAccount);
    }

    @Test//Negative
    void shouldNotUpdateAnExistingAccount(){
        long accountId = 1l;
        Accounts updatedAccount = new Accounts();

        updatedAccount.setAccountId(accountId);
        updatedAccount.setFirstName("Updated Account");

        doThrow(RuntimeException.class).when(accountsRepository).findById(accountId);

        assertThrows(RuntimeException.class, ()-> accountService.updateAccount(accountId, updatedAccount));

    }


}
