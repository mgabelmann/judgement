package ca.mikegabelmann.judgement.persistence.service;

import ca.mikegabelmann.judgement.persistence.model.AccountActivityLog;

import java.util.List;
import java.util.UUID;

public interface AccountActivityLogService {

    List<AccountActivityLog> getLogsByAccountId(UUID accountId);

}
