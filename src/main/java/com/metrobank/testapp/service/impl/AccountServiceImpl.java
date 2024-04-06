package com.metrobank.testapp.service.impl;

import com.metrobank.testapp.model.Accounts;
import com.metrobank.testapp.repository.AccountsRepository;
import com.metrobank.testapp.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountsRepository accountsRepository;

  /*  public String processMessage(String input) {
        String message = input +
                " - Service added me";

        return message;
    }*/

    //Create Account
    public Accounts createAccount(final Accounts accounts) {

        return this.accountsRepository.save(accounts);
    }

    @Override
    public List<Accounts> getByKeyword(final String keyword) {
        return this.accountsRepository.findByKeyword(keyword);
    }

    //Read
    public Accounts findById(final long accountId) {

        return this.accountsRepository.findById(accountId);
    }

    public List<Accounts> findAll() {
        return this.accountsRepository.findAll();
    }

    public Accounts findByMobileNumber(final String mobileNumber) {
        return this.accountsRepository.findByMobileNumber(mobileNumber);
    }

    public Accounts findByEmailAddress(final String emailAddress) {
        return this.accountsRepository.findByEmailAddress(emailAddress);
    }

    //Delete
    public void deleteAccount(final Long accountId) {
        this.accountsRepository.deleteById(accountId);
    }

    //Update
    public Accounts updateAccount(final long accountId, final Accounts accounts) {
        final Optional<Accounts> accountsData = Optional.ofNullable(this.accountsRepository.findById(accountId));

        if (accountsData.isPresent()) {
            final Accounts accountsDb = accountsData.get();
            accountsDb.setFirstName(accounts.getFirstName());
            accountsDb.setMiddleName(accounts.getMiddleName());
            accountsDb.setLastName(accounts.getLastName());

            accountsDb.setMobileNumber(accounts.getMobileNumber());
            accountsDb.setTelephoneNumber(accounts.getTelephoneNumber());
            accountsDb.setEmailAddress(accounts.getEmailAddress());

            accountsDb.setMonthlySalary(accounts.getMonthlySalary());

            accountsDb.setDateOfBirth(accounts.getDateOfBirth());

            accountsDb.setCheckingAccount(accounts.getCheckingAccount());
            accountsDb.setSavingsAccount(accounts.getSavingsAccount());
            accountsDb.setMoneyMarketAccount(accounts.getMoneyMarketAccount());
            accountsDb.setCertificateOfDepositAccount(accounts.getCertificateOfDepositAccount());

            accountsDb.setHomeAddress_city(accounts.getHomeAddress_city());
            accountsDb.setHomeAddress_street(accounts.getHomeAddress_street());
            accountsDb.setHomeAddress_province(accounts.getHomeAddress_province());
            accountsDb.setHomeAddress_zipCode(accounts.getHomeAddress_zipCode());

            accountsDb.setPermanentAddress_city(accounts.getPermanentAddress_city());
            accountsDb.setPermanentAddress_street(accounts.getPermanentAddress_street());
            accountsDb.setPermanentAddress_province(accounts.getPermanentAddress_province());
            accountsDb.setPermanentAddress_zipCode(accounts.getPermanentAddress_zipCode());

            return this.accountsRepository.save(accountsDb);

        } else {
            return null;
        }

    }

    // Pagination
    public Page<Accounts> findPaginated(final int pageNumber, final int pageSize, final String sortField, final String sortDirection, final String keyword) {
        final Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        if ((keyword != null ) && (keyword != "")) {
            return accountsRepository.findByFirstName(keyword, pageable);
        } else {
            return accountsRepository.findAll(pageable);
        }
    }

}



