package ca.mikegabelmann.judgement.persistence.service;

import ca.mikegabelmann.judgement.persistence.model.Account;

import java.util.Optional;

public interface AccountService {

    Optional<Account> findByEmail(String email);

    Account createAccount(Account account);

}
