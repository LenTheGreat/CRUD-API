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
import org.springframework.http.MediaType;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.security.auth.login.AccountNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc

public class AccountsRepositoryTest {
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private AccountsRepository accountsRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private  Accounts accounts;

    @Autowired
    private ObjectMapper objectMapper;

@Test
    void shouldFindAnExisitngAccount_findById(){

    Accounts exisitingAccount = new Accounts();
    long existingAccountId = 3l;
    exisitingAccount.setAccountId(existingAccountId);

    when(accountsRepository.findById(existingAccountId)).thenReturn(exisitingAccount);
    Accounts foundAccount = accountService.findById(existingAccountId);

    assertThat(foundAccount).isNotNull();
    verify(accountsRepository).findById(existingAccountId);
}

@Test
    void shouldFindAnExistingAccount_findByEmailAddress(){

    Accounts existingAccount= new Accounts();
    String existingEmail = "test@email.com";
    existingAccount.setEmailAddress(existingEmail);

    when(accountsRepository.findByEmailAddress(existingEmail)).thenReturn(existingAccount);
    Accounts foundAccount = accountService.findByEmailAddress(existingEmail);

    assertThat(foundAccount).isNotNull();
    verify(accountsRepository).findByEmailAddress(existingEmail);
}
@Test
    void shouldFindAnExistingAccount_findByMobileNumber(){

    Accounts existingAccount= new Accounts();
    String existingMobileNumber = "79336";
    existingAccount.setEmailAddress(existingMobileNumber);

    when(accountsRepository.findByEmailAddress(existingMobileNumber)).thenReturn(existingAccount);
    Accounts foundAccount = accountService.findByEmailAddress(existingMobileNumber);

    assertThat(foundAccount).isNotNull();
    verify(accountsRepository).findByEmailAddress(existingMobileNumber);
}
@Test
    void shouldFindAllExistingAccount(){

    Accounts mockAccount1 = new Accounts(5l,"Thalia","G","Grace","6531","78641","thalia@testemail.com","February 29, 2005",false,true,false,true, "Atis", "Angono","RIZAL","1930","Atis","Angono","Rizal","1930",654721);
    Accounts mockAccount2 = new Accounts(5l,"Percy","K","Jackson","9834","89732","percy@testemail.com","February 29, 2005",true,false,false,true, "Kaimito", "Cainta","RIZAL","1950","Kaimito","Cainta","Rizal","1950",99921);

    when(accountsRepository.findAll()).thenReturn(List.of(mockAccount1,mockAccount2));

    var accountList = accountService.findAll();

    assertThat(accountList).isNotNull();
    assertThat(accountList.size()).isEqualTo(2);

}
}
