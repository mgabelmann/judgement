--populate CODE tables and any other required records

--ROLE_CODE
INSERT INTO ROLE_CODE(code, label, active) VALUES ('SYSTEM', 'System', 'N');
INSERT INTO ROLE_CODE(code, label, active) VALUES ('ADMINISTRATOR', 'Administrator', 'Y');
INSERT INTO ROLE_CODE(code, label, active) VALUES ('PROJECT_ADMIN', 'Project Administrator', 'Y');
INSERT INTO ROLE_CODE(code, label, active) VALUES ('PROJECT_JUDGE', 'Project Judge', 'Y');
INSERT INTO ROLE_CODE(code, label, active) VALUES ('PROJECT_CLIENT', 'Project Client', 'Y');
INSERT INTO ROLE_CODE(code, label, active) VALUES ('VIEWER', 'Viewer', 'Y');

--ACCOUNT_STATUS_CODE
INSERT INTO ACCOUNT_STATUS_CODE(code, label, active) VALUES ('NEW', 'New', 'Y');
INSERT INTO ACCOUNT_STATUS_CODE(code, label, active) VALUES ('PENDING', 'Pending', 'Y');
INSERT INTO ACCOUNT_STATUS_CODE(code, label, active) VALUES ('ACTIVE', 'Active', 'Y');
INSERT INTO ACCOUNT_STATUS_CODE(code, label, active) VALUES ('INACTIVE', 'Inactive', 'Y');
INSERT INTO ACCOUNT_STATUS_CODE(code, label, active) VALUES ('SUSPENDED', 'Suspended', 'Y');
INSERT INTO ACCOUNT_STATUS_CODE(code, label, active) VALUES ('BLOCKED', 'Blocked', 'Y');
INSERT INTO ACCOUNT_STATUS_CODE(code, label, active) VALUES ('PASSWORD_RESET', 'Password Reset', 'Y');

--ACCOUNT
INSERT INTO ACCOUNT(account_id, email, username, password, account_status, createdby, modifiedby, createdon_dtm, modifiedon_dtm, version) VALUES ('40b04e44-fd4d-4b5a-86af-c8e5bbd9d348', 'test@test.com', 'system', 'xxx', 'ACTIVE', 'system', 'system', datetime(), datetime(), 0);


--DON'T NEED A COMMIT STATEMENT
