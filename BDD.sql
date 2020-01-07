DROP DATABASE IF EXISTS BDD_gestion_ressources_universitaires;
CREATE DATABASE BDD_gestion_ressources_universitaires;
USE BDD_gestion_ressources_universitaires;

CREATE TABLE Utilisateur(
   loginUser VARCHAR(100),
   passwordUser VARCHAR(50) NOT NULL,
   nomUser VARCHAR(50) NOT NULL,
   prenomUser VARCHAR(50) NOT NULL,
   typeUser ENUM('ENSEIGNANT','ETUDIANT','TECHNICIEN','ADMINISTRATIF') NOT NULL,
   PRIMARY KEY(loginUser)
);

CREATE TABLE Message(
   idMessage INT AUTO_INCREMENT,
   corpsMessage TEXT NOT NULL,
   dateMessage INT NOT NULL,
   loginUser VARCHAR(100) NOT NULL,
   PRIMARY KEY(idMessage),
   FOREIGN KEY(loginUser) REFERENCES Utilisateur(loginUser)
);

CREATE TABLE GroupeUser(
   idGroupe INT AUTO_INCREMENT,
   nomGroupe VARCHAR(50) NOT NULL,
   PRIMARY KEY(idGroupe)
);

CREATE TABLE Fil(
   idFil INT,
   titre VARCHAR(50),
   PRIMARY KEY(idFil),
   FOREIGN KEY(idFil) REFERENCES Message(idMessage)
);

CREATE TABLE Appartenir(
   loginUser VARCHAR(100),
   idGroupe INT,
   PRIMARY KEY(loginUser, idGroupe),
   FOREIGN KEY(loginUser) REFERENCES Utilisateur(loginUser),
   FOREIGN KEY(idGroupe) REFERENCES GroupeUser(idGroupe)
);

CREATE TABLE Correspondre(
   idGroupe INT,
   idFil INT,
   PRIMARY KEY(idGroupe, idFil),
   FOREIGN KEY(idGroupe) REFERENCES GroupeUser(idGroupe),
   FOREIGN KEY(idFil) REFERENCES Fil(idFil)
);

CREATE TABLE Contenir(
   idMessage INT,
   idFil INT,
   PRIMARY KEY(idMessage, idFil),
   FOREIGN KEY(idMessage) REFERENCES Message(idMessage),
   FOREIGN KEY(idFil) REFERENCES Fil(idFil)
);
