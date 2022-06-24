CREATE TABLE stationlist (
	id serial PRIMARY KEY,
	station_id int NOT NULL,
	avialable boolean NOT NULL,
	latitude float NOT NULL,
	longitude float NOT NULL
);


insert into stationlist (station_id, avialable, latitude, longitude) values (1, 'true', 48.267492, 16.487607),
(2, 'true', 48.345442, 16.34949),
(3, 'true', 48.461581, 16.304212),
(4, 'true', 48.201938, 16.481203),
(5, 'true', 48.459901, 16.282501);