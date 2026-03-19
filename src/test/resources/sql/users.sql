INSERT INTO user_account (id, version, first_name, last_name)
VALUES (1, 0, 'Edward', 'Legaspi'),
       (2, 0, 'Bob', 'Smith'),
       (3, 0, 'Carol', 'Williams'),
       (4, 0, 'David', 'Brown'),
       (5, 0, 'Eve', 'Jones'),
       (6, 0, 'Frank', 'Garcia'),
       (7, 0, 'Grace', 'Miller'),
       (8, 0, 'Hank', 'Davis'),
       (9, 0, 'Ivy', 'Rodriguez'),
       (10, 0, 'Jack', 'Martinez'),
       (11, 0, 'Karen', 'Hernandez'),
       (12, 0, 'Leo', 'Lopez'),
       (13, 0, 'Mia', 'Gonzalez'),
       (14, 0, 'Noah', 'Wilson'),
       (15, 0, 'Olivia', 'Anderson'),
       (16, 0, 'Paul', 'Thomas'),
       (17, 0, 'Quinn', 'Taylor'),
       (18, 0, 'Ruby', 'Moore'),
       (19, 0, 'Sam', 'Jackson'),
       (20, 0, 'Tina', 'Martin'),
       (21, 0, 'Alice', 'Johnson')
;

INSERT INTO user_account (id, version, first_name, last_name, birth_date)
VALUES (22, 0, 'Larry', 'Johnson', '1984-10-13 14:0:00'),
       (23, 0, 'Edward', 'CzetsuyaTech', '1984-10-13 14:0:00');

INSERT INTO user_hobby (user_id, hobby)
VALUES (1, 'Chess'),
       (23, 'Anime');
