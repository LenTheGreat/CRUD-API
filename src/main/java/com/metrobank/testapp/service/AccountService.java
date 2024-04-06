package com.metrobank.testapp.service;

import com.metrobank.testapp.model.Accounts;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountService {
   // String processMessage(String input);
    Accounts createAccount(Accounts accounts);

    Accounts updateAccount (long accountId, Accounts accounts);

    Accounts findById(long accountId);

    Accounts findByMobileNumber(String mobileNumber);
    Accounts findByEmailAddress(String emailAddress);

    //Accounts findByEmailandMobileNumber(String emailAddress, String mobileNumber);

    List<Accounts> findAll();

    void deleteAccount(Long accountId);

    Page<Accounts> findPaginated(int pageNumber, int pageSize, String sortField, String sortDirection, String keyword );

    List<Accounts> getByKeyword(String keyword);

}
