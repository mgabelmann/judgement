--populate CODE tables and any other required records

--ROLE_CODE
INSERT INTO ROLE_CODE(code, label, active) VALUES ('SYSTEM', 'System', 'N');
INSERT INTO ROLE_CODE(code, label, active) VALUES ('ADMINISTRATOR', 'Administrator', 'Y');
INSERT INTO ROLE_CODE(code, label, active) VALUES ('PROJECT_ADMIN', 'Project Administrator', 'Y');
INSERT INTO ROLE_CODE(code, label, active) VALUES ('CLIENT', 'Client', 'Y');

--ACCOUNT_STATUS_CODE
INSERT INTO ACCOUNT_STATUS_CODE(code, label, active) VALUES ('NEW', 'New', 'Y');
INSERT INTO ACCOUNT_STATUS_CODE(code, label, active) VALUES ('PENDING', 'Pending', 'Y');
INSERT INTO ACCOUNT_STATUS_CODE(code, label, active) VALUES ('ACTIVE', 'Active', 'Y');
INSERT INTO ACCOUNT_STATUS_CODE(code, label, active) VALUES ('INACTIVE', 'Inactive', 'Y');
INSERT INTO ACCOUNT_STATUS_CODE(code, label, active) VALUES ('SUSPENDED', 'Suspended', 'Y');
INSERT INTO ACCOUNT_STATUS_CODE(code, label, active) VALUES ('BLOCKED', 'Blocked', 'Y');
INSERT INTO ACCOUNT_STATUS_CODE(code, label, active) VALUES ('PASSWORD_RESET', 'Password Reset', 'Y');
