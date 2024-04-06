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
class ApplicationTests {
	@Autowired
	private MockMvc mockMvc;
	@Mock
	private AccountsRepository accountsRepository;


	@InjectMocks
	private AccountServiceImpl accountService;

	private  Accounts accounts;

	@Autowired
	private ObjectMapper objectMapper;

	//Postive

	@Test
	void creatingAnAccountTest(){

		Accounts newAccounts = new Accounts(5l,"Yoda","Luke","Skywalker","32645","S45454","email@email.com","January 20 2006",true,false,false,false,"36","Binangonan","RIzal","1945","36","Binangonan","RIzal","1945",698653);
		when(accountsRepository.save(newAccounts)).thenReturn(newAccounts);
		assertThat(newAccounts).isNotNull();
		assertThat(newAccounts.getAccountId()).isGreaterThan(3l);
	}

	@Test
	void updatingAnExistingAccountTest(){
		Accounts existingAccount = new Accounts();
		//existingAccount.getAccountId();

		existingAccount.setAccountId(7l);
		existingAccount.setFirstName("Sun");
		existingAccount.setMiddleName("K.");
		existingAccount.setLastName("Wukong");

		existingAccount.setMobileNumber("384");
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


		when(this.accountsRepository.save(existingAccount)).thenReturn(existingAccount);
		assertThat(existingAccount).isNotNull();
		assertThat(existingAccount.getAccountId()).isEqualTo(7L);
	}

	@Test
	void deleteById(){
		accountsRepository.deleteById(2l);
		Optional<Accounts> accountsdb = Optional.ofNullable(this.accountsRepository.findById(2l));

		assertThat(accountsdb.isPresent()).isFalse();
	}

	@Test
	void updatingAnAccountTest(){
		Accounts updatedAccount = new Accounts();
		when(accountsRepository.findById(1l)).thenReturn(updatedAccount);
		updatedAccount.setEmailAddress("test@email.com");

		assertThat(updatedAccount).isNotNull();
		assertThat(updatedAccount.getEmailAddress()).isEqualTo("test@email.com");
	}



	// Unit Testing Service Layer (Viewing ALl accounts)
	@Test
	void shouldFindAndReturnAllAccounts(){

		when(accountsRepository.findAll()).thenReturn(List.of(new Accounts(), new Accounts())); // Returning the List of All Accounts when accessing the Repository

		assertThat(accountService.findAll()).hasSize(2); // Assume the total number of accounts
		verify(accountsRepository, times(1)).findAll(); // expected # of times the findALl() method is called in the repository
		verifyNoMoreInteractions(accountsRepository); // validate if there is no more interaction with the repository
	}
	@Test
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

	@Test
	void shouldDeleteUserWhenGivenIdIsFound(){
		Accounts accounts =new Accounts();
		Long accountIdToDelete = 5l;
		//when(accountsRepository.findById(accounts.getAccountId())).thenReturn(null);
		accountService.deleteAccount(accounts.getAccountId());

		verify(accountsRepository, times(1)).deleteById(accountIdToDelete);
	}
	//Negative
	@Test
	void shouldNotFindAnAccountWithUnknownId() throws Exception{
		long accountIdtoFind = 21L;
		Accounts nullAccount = new Accounts();
		when(accountsRepository.findById(accountIdtoFind)).thenReturn(null);

		Accounts foundAccount = accountService.findById(accountIdtoFind);
		assertThat(foundAccount).isNotEqualTo(nullAccount);

		verify(accountsRepository, times(1)).findById(accountIdtoFind);

		verifyNoMoreInteractions(accountsRepository);
	}

	@Test
	void shouldNotUpdateAnAccountWithUnknownId(){
		long unknownId = 7l;
		Accounts updatingAccount = new Accounts();
		updatingAccount.setAccountId(unknownId);
		updatingAccount.setFirstName("Sun");
		updatingAccount.setMiddleName("K.");
		updatingAccount.setLastName("Wukong");

		updatingAccount.setMobileNumber("384");
		updatingAccount.setTelephoneNumber("2564");
		updatingAccount.setEmailAddress("test@email.com");

		updatingAccount.setMonthlySalary(56368);

		updatingAccount.setDateOfBirth("March 7 2007");

		updatingAccount.setCheckingAccount(false);
		updatingAccount.setSavingsAccount(true);
		updatingAccount.setMoneyMarketAccount(false);
		updatingAccount.setCertificateOfDepositAccount(false);

		updatingAccount.setHomeAddress_city("Cainta");
		updatingAccount.setHomeAddress_street("Mangahan");
		updatingAccount.setHomeAddress_province("Rizal");
		updatingAccount.setHomeAddress_zipCode("1950");

		updatingAccount.setPermanentAddress_city("Cainta");
		updatingAccount.setPermanentAddress_street("Mangahan");
		updatingAccount.setPermanentAddress_province("Rizal");
		updatingAccount.setPermanentAddress_zipCode("1950");

		long legitAccountId = 2l;

		when(accountService.findById(unknownId)).thenReturn(updatingAccount);
		assertThat(updatingAccount.getAccountId()).isNotEqualTo(accountService.findById(legitAccountId));
	}

	@Test
	void shouldNotDeleteAccountWithUnknownId(){
		//GIVEN
		long unknownId = 86l;
		Accounts accounts = new Accounts();
		accounts.setAccountId(unknownId);
		//WHEN
		Accounts accountToDelete = accountService.findById(accounts.getAccountId());
		when(accountToDelete.getAccountId()).thenReturn(null);
		//THEN
		//assertThat()




	}




	/*
	@Test
	public void updateAccountTest(){

		Accounts accounts = accountsRepository.findById(1l).get();

		accounts.setEmailAddress("rararara@gmail.com");

		Accounts updatedAccounts = accountsRepository.save(accounts);

		assertThat(updatedAccounts.getEmailAddress()).isEqualTo("rararara@gmail.com");
	}

*/

	@Test
	void shouldReturnDefaultMessage() throws Exception {
		this.mockMvc.perform(get("/accounts")).andDo(print()).andExpect(status().isOk())
				.andExpect(content()
						.string(containsString("Greetings Stranger! - Service added me")));
	}


	//Testing if AccountNotBlank Works
	/*
	@Test
	public void whenNotBlankName_thenNoConstraintViolations(){
		Accounts accounts = new Accounts(1,"Mark ALlen", "Alpuerto","Gullon","09473158739","0233","markallengullon@gmail.com","April 21, 2002",true,false,false,false,"C.valle","Taytay","Rizal","1970","C.valle","Taytay","Rizal","1970",60000);
		Set<ConstraintViolation<Accounts>> violations.
	}

	 */
}
