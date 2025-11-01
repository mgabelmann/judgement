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
INSERT INTO ACCOUNT(account_id, email, username, password, salt, account_status, account_role, createdby, modifiedby, createdon_dtm, modifiedon_dtm, version) VALUES ('40b04e44-fd4d-4b5a-86af-c8e5bbd9d348', 'test@test.com', 'SYSTEM', '{noop}password', '', 'ACTIVE', 'SYSTEM','SYSTEM', 'SYSTEM', datetime(), datetime(), 0);

INSERT INTO ACCOUNT(account_id, email, username, password, salt, account_status, account_role, createdby, modifiedby, createdon_dtm, modifiedon_dtm, version) VALUES ('41858569-dca9-43a2-8227-d3dbdefc7398', 'admin@test.com', 'ADMIN', '{argon2@SpringSecurity_v5_8}$argon2id$v=19$m=16384,t=2,p=1$uYlDg042kQrev2kLcENV0w$+42gve/41uPFTCfBJ9PcyvM1SC0ud9ER6uyO8RjqrsQ', '', 'ACTIVE', 'ADMINISTRATOR','SYSTEM', 'SYSTEM', datetime(), datetime(), 0);

INSERT INTO ACCOUNT(account_id, email, username, password, salt, account_status, account_role, createdby, modifiedby, createdon_dtm, modifiedon_dtm, version) VALUES ('c25d0b83-ae0a-479e-a2d0-a442335bd88f', 'pm@test.com', 'PM', '{argon2@SpringSecurity_v5_8}$argon2id$v=19$m=16384,t=2,p=1$uYlDg042kQrev2kLcENV0w$+42gve/41uPFTCfBJ9PcyvM1SC0ud9ER6uyO8RjqrsQ', '', 'ACTIVE', 'ADMINISTRATOR','SYSTEM', 'SYSTEM', datetime(), datetime(), 0);



--DON'T NEED A COMMIT STATEMENT
