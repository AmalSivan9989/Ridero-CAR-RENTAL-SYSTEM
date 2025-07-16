CREATE DATABASE IF NOT EXISTS roadready;
drop database roadready;
USE roadready;
SELECT roles FROM user_data WHERE name = 'john';

CREATE TABLE user_data (
    uid INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    roles VARCHAR(20) NOT NULL
);
ALTER TABLE user_data
ADD COLUMN active BOOLEAN DEFAULT TRUE;


CREATE TABLE cars (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    make VARCHAR(50),
    model VARCHAR(50),
    location VARCHAR(100),
    type VARCHAR(50),
    image_url VARCHAR(255),
    price_per_day DOUBLE,
    available BOOLEAN DEFAULT TRUE
);


CREATE TABLE bookings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    pickup_date DATETIME,
    dropoff_date DATETIME,
    pickup_location VARCHAR(100),
    dropoff_location VARCHAR(100),
    car_id BIGINT,
    user_id INT,
    status VARCHAR(20),
    FOREIGN KEY (car_id) REFERENCES cars(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user_data(uid) ON DELETE CASCADE
);


CREATE TABLE payments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    amount DOUBLE NOT NULL,
    payment_method VARCHAR(50),
    payment_time DATETIME,
    booking_id BIGINT UNIQUE,
    status VARCHAR(20),
    FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE
);


CREATE TABLE feedback (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    comment TEXT,
    rating INT,
    user_id INT,
    car_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES user_data(uid) ON DELETE SET NULL,
    FOREIGN KEY (car_id) REFERENCES cars(id) ON DELETE CASCADE
);


CREATE TABLE maintenance_requests (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    issue_description TEXT,
    request_date DATETIME,
    status VARCHAR(20),
    car_id BIGINT,
    agent_id INT,
    FOREIGN KEY (car_id) REFERENCES cars(id) ON DELETE CASCADE,
    FOREIGN KEY (agent_id) REFERENCES user_data(uid) ON DELETE SET NULL
);
commit;
ALTER TABLE bookings
ADD COLUMN check_in_time DATETIME;

ALTER TABLE bookings
ADD COLUMN check_out_time DATETIME;

INSERT INTO cars (make, model, location, type, image_url, price_per_day, available)
VALUES ('Toyota', 'Innova', 'Bangalore', 'SUV', 'https://example.com/car.jpg', 2500, true);
SELECT * FROM payments WHERE booking_id IS NULL;

ALTER TABLE payments MODIFY booking_id BIGINT NOT NULL;
select * from bookings;

select * from cars;

CREATE TABLE wallet (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL UNIQUE,
    balance DOUBLE DEFAULT 0.0,
    FOREIGN KEY (user_id) REFERENCES user_data(uid) ON DELETE CASCADE
);
INSERT INTO wallet (user_id, balance) VALUES 
(1, 5000.00),   
(2, 1500.00),   
(3, 2000.00);   

ALTER TABLE cars 
ADD COLUMN available BOOLEAN DEFAULT TRUE;


ALTER TABLE cars
ADD COLUMN status VARCHAR(20) DEFAULT 'AVAILABLE';
UPDATE user_data SET roles = 'ROLE_ADMIN' WHERE name = 'john';

SET SQL_SAFE_UPDATES = 0;
UPDATE user_data SET roles = 'ROLE_CUSTOMER' WHERE uid = 2;
UPDATE user_data SET roles = 'ROLE_AGENT' WHERE uid = 3;
ALTER TABLE bookings ADD COLUMN total_amount DOUBLE NOT NULL;
