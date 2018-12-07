--SET SESSION sql_mode = 'STRICT_ALL_TABLES';
--DROP DATABASE IF EXISTS waste;
--CREATE DATABASE waste;
--USE waste;

--Create tables
-- CREATE TABLE WTEnum (type VARCHAR(32) PRIMARY KEY);
-- insert into WTEnum values("Recycling");
-- insert into WTEnum values("Trash");






CREATE TABLE Waste_Type (
	id INTEGER PRIMARY KEY AUTOINCREMENT, 
	name VARCHAR(32) NOT NULL); 
	-- service INTEGER NOT NULL);
	-- service VARCHAR(32) NOT NULL, 
	-- CONSTRAINT fk_column FOREIGN KEY (service) REFERENCES WTEnum (type)
-- );

CREATE TABLE Company (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	name VARCHAR(32) NOT NULL,
	address VARCHAR(128) NOT NULL,
	description TEXT
);

CREATE TABLE Site (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	name VARCHAR(32) NOT NULL,
	address VARCHAR(128) NOT NULL
);

CREATE TABLE Pickup (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	weight DECIMAL(8,3),
	date DATE,
	site_Id  INTEGER,
	waste_type_Id INTEGER,
	company_id INTEGER,

	CONSTRAINT fk_column FOREIGN KEY (site_Id) REFERENCES Site (id),
	CONSTRAINT fk_column FOREIGN KEY (company_id) REFERENCES Company (id),
	CONSTRAINT fk_column FOREIGN KEY (waste_type_Id) REFERENCES Waste_Type (id)
);


INSERT INTO Company (name, address) VALUES 
	("ACE", "2274 Technology Dr, West Valley City, UT 84119"),
	("METech", "2350 W Bridger Road, Salt Lake City, UT 84101"),
	("METech Recycling", "6200 Engle Way, Gilroy, CA 95020");

INSERT INTO Site (name, address) VALUES
	("Westminster//RYC-Recycling", "1700 S 1300 E"),
	("Westminster//East", "1700 S 1300 E"),
	("Westminster//West", "1700 S 1300 E"),
	("Westminster//Hogle", "1700 S 1300 E"),
	("Garfield Home", "1173 Garfield Ave");

INSERT INTO Waste_Type (name) VALUES
	("Recycling"),
	("Trash"),
	("Cardboard"),
	("C&D"),
	("Food"),
	("E-Waste");

INSERT INTO Pickup (weight, date, site_Id, waste_type_Id, company_id) VALUES
	(0.50, '07-01-2015', 1, 1, 1),
	(0.23, '07-03-2015', 1, 1, 1),
	(0.95, '04-01-2016', 3, 2, 1);



-- insert into Waste_Type values(1, "C&D", "Trash");
-- insert into Waste_Type values(2, "Cardboard", "Recycling");
-- CREATE TABLE Waste_Type (
-- 	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
-- 	name VARCHAR(32) NOT NULL,
-- 	service VARCHAR(32)("Recycling", "Trash") NOT NULL
-- );

-- CREATE TABLE Company (
-- 	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
-- 	name VARCHAR(32) NOT NULL,
-- 	address VARCHAR(128) NOT NULL,
-- 	description TEXT
-- );


-- insert into Company values(
-- 	1, "McDonalds", "69 S 420 E Salt Lake City, UT", "Danky McSpanky delicious patties direct to you. Also waste collection."
-- 	);

-- CREATE TABLE Site (
-- 	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
-- 	name VARCHAR(32) NOT NULL,
-- 	address VARCHAR(128) NOT NULL,

-- );

-- CREATE TABLE Destination (
-- 	id INTEGER PRIMARY KEY AUTOINCREMENT,
-- 	name VARCHAR(32) NOT NULL,
-- );

-- CREATE TABLE Pickup (
-- 	id INTEGER PRIMARY KEY AUTOINCREMENT,
-- 	weight DECIMAL(8,3),
-- 	date DATE,
-- 	site_Id  INTEGER,
-- 	destination_Id INTEGER,
-- 	waste_type_Id INTEGER,
-- 	company_id INTEGER.,

-- 	FOREIGN KEY (site_Id) REFERENCES Site (id),
-- 	FOREIGN KEY (destination_Id) REFERENCES Destination (id),
-- 	FOREIGN KEY (company_id) REFERENCES Company (id),
-- 	FOREIGN KEY (waste_type_Id) REFERENCES Waste_Type (id),
-- );

-- select name, service from Waste_Type where id == 1;