package com.metrobank.testapp.controller;

import com.metrobank.testapp.model.Accounts;
import com.metrobank.testapp.repository.AccountsRepository;
import com.metrobank.testapp.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.data.jpa.repository.Query;

import java.util.List;

@RestController
//@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    public  AccountService accountService;

    @Autowired
    AccountsRepository accountsRepository;


    @GetMapping({"/"})
    public ModelAndView viewHomePage(Accounts accounts,Model model, String keyword){

        return findPaginated(1,"firstName","asc",keyword,model);
    }

    /*@GetMapping("/Home")
    public ModelAndView viewHomePage1(){
        ModelAndView modelAndView = new ModelAndView("view/index");
        modelAndView.addObject("listAccounts", accountService.findAll());
        return modelAndView;
    }*/

    @GetMapping("/page/{pageNumber}")
    public ModelAndView findPaginated (@PathVariable (value = "pageNumber") int pageNumber,
                                       @RequestParam("sortField") String sortField,
                                       @RequestParam("sortDir") String sortDir,
                                       @RequestParam(name="keyword", required=false) String keyword,
                                       Model model){
        ModelAndView modelAndView = new ModelAndView("view/index");
        int pageSize = 3;

        Page<Accounts> page = accountService.findPaginated(pageNumber,pageSize,sortField,sortDir,keyword );
        List<Accounts> listAccounts =page.getContent();

        modelAndView.addObject("currentPage",pageNumber);
        modelAndView.addObject("totalPages",page.getTotalPages());
        modelAndView.addObject("totalItems",page.getTotalElements());

        modelAndView.addObject("sortField",sortField);
        modelAndView.addObject("sortDir",sortDir);
        modelAndView.addObject("reverseSortDir",sortDir.equals("asc")?"desc":"asc");

        model.addAttribute("listAccounts",listAccounts);

        return modelAndView ;
    }



    @GetMapping("/showNewAccountForm")
    public ModelAndView showNewAccountForm (Model model){

        ModelAndView modelAndView = new ModelAndView("view/new_accounts");
        Accounts accounts = new Accounts();
        model.addAttribute("accounts", accounts);
        return modelAndView;
    }

    @PostMapping("/saveAccount")
    public ModelAndView saveAccount(@Valid @ModelAttribute("accounts") Accounts accounts, ModelMap model, BindingResult bindingResult){
        System.out.println("Before saving: accountTimeCreated = " + accounts.getAccountTimeCreated());
        accountService.createAccount(accounts);
        System.out.println("After saving: accountTimeCreated = " + accounts.getAccountTimeCreated());
        return new ModelAndView("redirect:/", model);
    }

    @GetMapping("/showFormForUpdate/{id}")
    public ModelAndView showFormForUpdate(@PathVariable(value = "id") Long id, Model model){
        Accounts accounts = accountService.findById(id);
        ModelAndView modelAndView = new ModelAndView("view/update_accounts");
        model.addAttribute("accounts", accounts);
        return modelAndView;
    }

    @GetMapping("/deleteAccount/{id}")
    public ModelAndView deleteAccount(@PathVariable(value = "id") long id, Model model){
        this.accountService.deleteAccount(id);
        return new ModelAndView("redirect:/");
    }

    //POST MAN

    @GetMapping(path = "/findAccount/{accountId}")
    public Accounts findAccount(@PathVariable("accountId") long accountId) {
        // ModelAndView modelAndView = new ModelAndView("view/home");
        //modelAndView.addObject("message", accountService.processMessage("Greetings Stranger!"));

        //return modelAndView;

        return accountService.findById(accountId);
    }


    @GetMapping(path = "/allAccounts")
    public List<Accounts> getAllAccounts(){
        return accountService.findAll();
    }

    //Delete Account
    @DeleteMapping(path = "/deleteAccount/{accountId}")
    public ResponseEntity<String> deleteAccount(@PathVariable("accountId") Long accountId){
        accountService.deleteAccount(accountId);
        return ResponseEntity.ok("Account deleted successfully");
    }

    // Update Account
    @PutMapping(path ="/updateAccount/{accountId}")
    public ResponseEntity<String>  updateAccount(@PathVariable("accountId") long accountId, @RequestBody@Valid Accounts accounts,BindingResult bindingResult){
        Accounts existingEmailAddress = accountService.findByEmailAddress(accounts.getEmailAddress());
        Accounts existingMobileNumber = accountService.findByMobileNumber(accounts.getMobileNumber());
        Accounts existingAccount = accountService.findById(accountId);

        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body("Validation Failed");
        }

        //Check if there is an existing account
        if(existingAccount == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account does not exist");
        }

        //Check if there are existing email address or mobile number
        if((existingEmailAddress != null && existingEmailAddress.getAccountId() != accountId) ||
                (existingMobileNumber != null && existingMobileNumber.getAccountId() != accountId)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("An account with the same email address or mobile number already exists");
        }

        //Update Account
        accountService.updateAccount(accountId, accounts);
        return ResponseEntity.status(HttpStatus.OK).body("Account updated successfully");
    }

    //Create Account
    @PostMapping(path = "/createAccount")
    public ResponseEntity<String> createAccount(@RequestBody @Valid Accounts accounts, BindingResult bindingResult) {
        Accounts existingEmailAddress = accountService.findByEmailAddress(accounts.getEmailAddress());
        Accounts existingMobileNumber = accountService.findByMobileNumber(accounts.getMobileNumber());

        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body("Validation failed");
        }
        if(existingEmailAddress == null && existingMobileNumber == null){
            accountService.createAccount(accounts);
            return ResponseEntity.status(HttpStatus.OK).body("Account created succesfully");
        }
        else{
            return ResponseEntity.status(HttpStatus.CONFLICT).body("An account with the same email address or mobile number already exists");
        }
    }



}