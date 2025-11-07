-- Create database
CREATE DATABASE mistoremanagement;

-- Use the database
USE mistoremanagement;

-- Create login table
CREATE TABLE login (
    username VARCHAR(20),
    password VARCHAR(20)
);

-- Insert a user for testing
INSERT INTO login VALUES ('admin', '12345');

-- Verify that data is inserted correctly
SELECT * FROM login;
