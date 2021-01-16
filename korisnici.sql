BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "korisnik" (
	"Ime"	TEXT,
	"Prezime"	TEXT,
	"email"	TEXT,
	"username"	TEXT,
	"password"	TEXT,
	"korisnik_id"	INTEGER,
	PRIMARY KEY("korisnik_id")
);
INSERT INTO "korisnik" VALUES ('Vedran','Ljubović','vljubovic@etf.unsa.ba','vedranlj','test',1);
INSERT INTO "korisnik" VALUES ('Amra','Delić','adelic@etf.unsa.ba','amrad','test',2);
INSERT INTO "korisnik" VALUES ('Tarik','Sijerčić','tsijercic1@etf.unsa.ba','tariks','test',3);
INSERT INTO "korisnik" VALUES ('Rijad','Fejzić','rfejzic1@etf.unsa.ba','rijadf','test',4);
COMMIT;
