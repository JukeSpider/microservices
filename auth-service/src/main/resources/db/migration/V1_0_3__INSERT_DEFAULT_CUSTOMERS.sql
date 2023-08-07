INSERT INTO USERS(id, email, role, status)
VALUES (
        'ccde2c48-2b24-4a1b-aa23-9db60f489792',
        'user@user.com',
        'ROLE_ADMIN',
        'VERIFIED'
);

INSERT INTO USERS(id, email, role, status)
VALUES (
        'd96288d9-3eba-43e9-92c4-0ea7fb633730',
        'user@admin.com',
        'ROLE_USER',
        'ACTIVE'
);

INSERT INTO USERS(id, email, role, status)
VALUES (
        'e16ccc45-f1c0-4cf1-9b6a-85da3a5f74f0',
        'user1@user.com',
        'ROLE_USER',
        'CREATED'
);

INSERT INTO PASSWORDS(user_id, pwd, status)
VALUES ('ccde2c48-2b24-4a1b-aa23-9db60f489792',
        '$2a$12$uXv.9/POxfia3qZxT8KBsuOhuIfB.CCuvW.Le3ziIehk1unte4mqq',
        'ACTIVE');

INSERT INTO PASSWORDS(user_id, pwd, status)
VALUES ('d96288d9-3eba-43e9-92c4-0ea7fb633730',
        '$2a$12$8yOD6x0n3Zzz4PzzeaS7iumOWLnCKQGY8/RG9ZEXFyovrUepD.Yiu',
        'ACTIVE');