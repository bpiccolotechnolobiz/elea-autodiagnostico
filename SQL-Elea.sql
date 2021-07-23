truncate table ELEA_AUTODIAGNOSTICO.dbo.preguntas 

// --------------------------------------------------   Crear preguntas


INSERT INTO ELEA_AUTODIAGNOSTICO.dbo.preguntas

(idPantalla, descripcionPregunta, idOrdenEnPantalla, estadoLogico)

--VALUES(1,'¿Cual es tu temperatura corporal actual?',0,1);

--VALUES(2,'¿Percibiste una marcada pérdida del olfato de manera repentina?',0,1);

--VALUES(2,'¿Percibiste una marcada pérdida del gusto (sabor de los alimentos) de manera repentina?',1,1);

--VALUES(2,'¿Tenés tos?',2,1);

--VALUES(2,'¿Tenés dolor de garganta?',3,1);

--VALUES(2,'¿Tenés dificultad respiratoria o falta de aire?',4,1);

--VALUES(2,'¿Tenés dolor de cabeza?',5,1);

--VALUES(2,'¿Tenés diarrea?',6,1);

--VALUES(2,'¿Tenés vómitos?',7,1);

--VALUES(2,'¿Tenés dolor muscular?',8,1);

 

--VALUES(3,'Trabajo o convivo con una persona que actualmente es caso confirmado de COVID-19.',0,1);

--VALUES(3,'Pasé en los últimos 14 días al menos 15 minutos cerca de una persona que actualmente es caso confirmado de COVID-19.',1,1);

--VALUES(3,'Tengo/tuve cancer.',2,1);

--VALUES(3,'Tengo diabetes.',3,1);

--VALUES(3,'Tengo alguna enfermedad hepática.',4,1);

--VALUES(3,'Tengo enfermedad renal crónica.',5,1);

--VALUES(3,'Tengo alguna enfermedad respiratoria.',6,1);

--VALUES(3,'Tengo alguna enfermedad cardiológica.',7,1);

VALUES(3,'Tengo alguna condición que baja las defensas.',8,1);



// -----------------------------------


CREATE TABLE

-- ELEA_AUTODIAGNOSTICO.dbo.pantallas definition

-- Drop table

-- DROP TABLE ELEA_AUTODIAGNOSTICO.dbo.pantallas GO

CREATE TABLE ELEA_AUTODIAGNOSTICO.dbo.pantallas (
	idPantalla int IDENTITY(1,1) NOT NULL,
	descripcionPantalla varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	CONSTRAINT PK__pantalla__796EF6466B097BC8 PRIMARY KEY (idPantalla)
) GO;


// -----------------------------------

-- ELEA_AUTODIAGNOSTICO.dbo.autodiagnostico definition

-- Drop table

-- DROP TABLE ELEA_AUTODIAGNOSTICO.dbo.autodiagnostico GO

CREATE TABLE ELEA_AUTODIAGNOSTICO.dbo.autodiagnostico (
	idAutodiagnostico int IDENTITY(1,1) NOT NULL,
	nroLegajo varchar(10) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	dni varchar(10) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	nombre varchar(25) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	apellido varchar(25) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	telefono varchar(20) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	empresa varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	mail varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	lugarAcceso int NOT NULL,
	estadoSintomas bit NULL,
	estadoContactoEstrecho bit NULL,
	estadoAntecedentes bit NULL,
	resultado bit NULL,
	fecha_autodiagnostico datetime NOT NULL,
	fecha_hasta_resultado datetime NOT NULL,
	comentario varchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	modificadoPor varchar(10) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	modificadoEn datetime NULL,
	CONSTRAINT PK__autodiag__0A767AEC5B6FF7D0 PRIMARY KEY (idAutodiagnostico)
) GO;



// -------------------------------------------------------------


-- ELEA_AUTODIAGNOSTICO.dbo.preguntas definition

-- Drop table

-- DROP TABLE ELEA_AUTODIAGNOSTICO.dbo.preguntas GO

CREATE TABLE ELEA_AUTODIAGNOSTICO.dbo.preguntas (
	idPregunta int IDENTITY(1,1) NOT NULL,
	idPantalla int NOT NULL,
	descripcionPregunta varchar(150) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	idOrdenEnPantalla int NOT NULL,
	estadoLogico bit NOT NULL,
	CONSTRAINT PK__pregunta__623EEC429488FF9C PRIMARY KEY (idPregunta)
) GO;


-- ELEA_AUTODIAGNOSTICO.dbo.preguntas foreign keys

ALTER TABLE ELEA_AUTODIAGNOSTICO.dbo.preguntas ADD CONSTRAINT FK__preguntas__idPan__73BA3083 FOREIGN KEY (idPantalla) REFERENCES ELEA_AUTODIAGNOSTICO.dbo.pantallas(idPantalla) GO;
