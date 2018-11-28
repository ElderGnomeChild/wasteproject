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
	name VARCHAR(32) NOT NULL, 
	service INTEGER NOT NULL);
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