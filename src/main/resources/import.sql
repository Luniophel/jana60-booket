INSERT INTO categories (name, description) VALUES ('test01', 'Lorem ipsum dolor sit amet, laborum.');

INSERT INTO categories (name, description) VALUES ('test02', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit, laborum.');

INSERT INTO categories (name, description) VALUES ('test03', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.');

INSERT INTO location (id, name, description, address, capacity) VALUES (1, 'Location non selezionata', 'Questa location non é selezionata, per selezionare una location non presente nel DataBase per favre vai nella sezione Location e aggiungila', 'via Sconosciuto', '1000');

INSERT INTO events (description, end_date, location_id, name, published_status, start_date, visible) VALUES ('lorem ipsum aaa', CAST('2023-10-30T10:59:00' AS DATETIME), 1, 'eventtest01', 1, CAST('2023-10-27T10:59:00' AS DATETIME), 0);

INSERT INTO events (description, end_date, location_id, name, published_status, start_date, visible) VALUES ('lorem ipsum eee', CAST('2024-10-30T10:59:00' AS DATETIME), 1, 'eventtest02', 1, CAST('2024-10-27T10:59:00' AS DATETIME), 0);

