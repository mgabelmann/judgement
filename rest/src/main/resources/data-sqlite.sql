--populate CODE tables and any other required records

--ROLE_CODE
INSERT INTO ROLE_CODE(code, label, active) VALUES ('SYSTEM', 'System', 'N');
INSERT INTO ROLE_CODE(code, label, active) VALUES ('ADMINISTRATOR', 'Administrator', 'Y');
INSERT INTO ROLE_CODE(code, label, active) VALUES ('DEVELOPER', 'Developer', 'Y');
INSERT INTO ROLE_CODE(code, label, active) VALUES ('TESTER', 'Tester', 'Y');
INSERT INTO ROLE_CODE(code, label, active) VALUES ('USER', 'User', 'Y');
--INSERT INTO ROLE_CODE(code, label, active) VALUES ('API_CLIENT', 'API CLient', 'Y');


--ACCOUNT_STATUS_CODE
INSERT INTO ACCOUNT_STATUS_CODE(code, label, active) VALUES ('NEW', 'New', 'Y');
INSERT INTO ACCOUNT_STATUS_CODE(code, label, active) VALUES ('PENDING', 'Pending', 'Y');
INSERT INTO ACCOUNT_STATUS_CODE(code, label, active) VALUES ('ACTIVE', 'Active', 'Y');
INSERT INTO ACCOUNT_STATUS_CODE(code, label, active) VALUES ('INACTIVE', 'Inactive', 'Y');
INSERT INTO ACCOUNT_STATUS_CODE(code, label, active) VALUES ('SUSPENDED', 'Suspended', 'Y');
INSERT INTO ACCOUNT_STATUS_CODE(code, label, active) VALUES ('BLOCKED', 'Blocked', 'Y');
INSERT INTO ACCOUNT_STATUS_CODE(code, label, active) VALUES ('PASSWORD_RESET', 'Password Reset', 'Y');


--PROJECT_ROLE_CODE
INSERT INTO PROJECT_ROLE_CODE(code, label, active) VALUES ('PROJECT_OWNER', 'Project Owner', 'Y');
INSERT INTO PROJECT_ROLE_CODE(code, label, active) VALUES ('PROJECT_JUDGE', 'Project Judge', 'Y');
INSERT INTO PROJECT_ROLE_CODE(code, label, active) VALUES ('PROJECT_USER', 'Project User', 'Y');
INSERT INTO PROJECT_ROLE_CODE(code, label, active) VALUES ('PROJECT_VIEWER', 'Project Viewer', 'Y');


--ACCOUNT
INSERT INTO ACCOUNT(account_id, email, username, password, salt, account_status, account_role, createdby, modifiedby, createdon_dtm, modifiedon_dtm, version) VALUES ('40b04e44-fd4d-4b5a-86af-c8e5bbd9d348', 'test@test.com', 'SYSTEM', '', '', 'ACTIVE', 'SYSTEM','SYSTEM', 'SYSTEM', datetime(), datetime(), 0);
/*
{
    "secret": "XXX",
    "saltLength": 16,
    "iterations": 310000,
    "algorithm": "PBKDF2WithHmacSHA256",
    "salt": "QkhT97vlBUo=",
    "hashedPassword": "sW45Y1AYiyBba/BSdjrCzE30hfGwuOsucLGq4qwsvnz5zACvZLGicCuJQt/HUIHJ"
}
 */
INSERT INTO ACCOUNT(account_id, email, username, password, salt, account_status, account_role, createdby, modifiedby, createdon_dtm, modifiedon_dtm, version) VALUES ('41858569-dca9-43a2-8227-d3dbdefc7398', 'admin@test.com', 'ADMIN', 'sW45Y1AYiyBba/BSdjrCzE30hfGwuOsucLGq4qwsvnz5zACvZLGicCuJQt/HUIHJ', 'QkhT97vlBUo=', 'ACTIVE', 'ADMINISTRATOR','SYSTEM', 'SYSTEM', datetime(), datetime(), 0);



--DON'T NEED A COMMIT STATEMENT
