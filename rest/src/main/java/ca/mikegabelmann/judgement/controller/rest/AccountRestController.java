package ca.mikegabelmann.judgement.controller.rest;

import ca.mikegabelmann.judgement.persistence.model.Account;
import ca.mikegabelmann.judgement.persistence.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountRestController.class);

    public static final String PATH_LOGIN = "/login";
    public static final String PATH_LOGOUT = "/logout";
    public static final String PATH_REGISTER = "/register";
    public static final String PATH_RESET = "/reset";


    private AccountService accountService;

    @Autowired
    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(value = PATH_LOGIN, method = RequestMethod.POST)
    public Account login(@RequestBody Account account) {
        return null;
    }

    @RequestMapping(value = PATH_REGISTER, method = RequestMethod.POST)
    public Account createAccount(@RequestBody Account account) {
        return null;
    }

    @RequestMapping(value = PATH_LOGOUT, method = RequestMethod.POST)
    public Account logout(@RequestBody Account account) {
        return null;
    }

}
