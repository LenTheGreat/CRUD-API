package com.metrobank.testapp.repository;

import com.metrobank.testapp.model.Accounts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Long> {

    Accounts findById(long accountId);
    Accounts findByEmailAddress(String emailAddress);
    Accounts findByMobileNumber(String mobileNumber);

    Page<Accounts> findByFirstName(String firstName, Pageable pageable);
    List<Accounts> findAll();


    @Query(value = "SELECT * from \"metrobank-intern\".accounts a  WHERE a.first_name LIKE CONCAT('%',:keyword,'%')", nativeQuery=true)
    List<Accounts> findByKeyword(@Param("keyword") String keyword);


}
