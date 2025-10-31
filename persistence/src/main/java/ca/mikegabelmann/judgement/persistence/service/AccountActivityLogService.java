package ca.mikegabelmann.judgement.persistence.service;

import ca.mikegabelmann.judgement.persistence.model.Account;
import ca.mikegabelmann.judgement.persistence.model.AccountActivityLog;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface AccountActivityLogService {

    /**
     *
     * @param accountId
     * @param pageable
     * @return
     */
    List<AccountActivityLog> getLogsByAccountId(UUID accountId, Pageable pageable);

    /**
     *
     * @param account
     * @param message
     */
    void saveLog(final Account account, final String message);

}
