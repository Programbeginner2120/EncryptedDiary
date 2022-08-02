/* Creating Encrypted Diary Database */

/* CREATE DATABASE IF NOT EXISTS ENCRYPTED_DIARY_DB; */

/* Changing to the ENCRYPTED_DIARY_DB database */

USE ENCRYPTED_DIARY_DB;


/* CREATING TABLE OF USERS */

CREATE TABLE IF NOT EXISTS Users (
    userID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username NVARCHAR(50) NOT NULL,
    passHash NVARCHAR(255) NOT NULL,
    dateCreated DATE
);

/* CREATING TABLE OF userData */
CREATE TABLE IF NOT EXISTS UserData (
    userDataId INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    securityQuestion NVARCHAR(255) NULL,
    securityAnswer NVARCHAR(255) NULL,
    numDocuments INT NOT NULL,
    userID INT, FOREIGN KEY (userID) REFERENCES Users(userID)
);

/* CREATING TABLE OF userDocuments */
CREATE TABLE IF NOT EXISTS UserDocuments(
    userDocumentsID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    userDocumentName VARCHAR (25) NOT NULL,
    userDocumentContents MEDIUMBLOB NOT NULL,
    encryptionMethod NVARCHAR(30) NOT NULL,
    decryptionMethod NVARCHAR(30) NOT NULL,
    userID INT, FOREIGN KEY (userID) REFERENCES Users(userID)
);

