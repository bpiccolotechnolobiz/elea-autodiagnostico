Base de datos Autodiagnostico

SOLO UNA VEZ: CREATE DATABASE ELEA_AUTODIAGNOSTICO;

DROP TABLE respuestas;
DROP TABLE preguntas;
DROP TABLE autodiagnostico;
DROP TABLE empleadosActivos;
DROP TABLE pantallas;
DROP TABLE lugarAcceso;
DROP TABLE parametria;

CREATE TABLE parametria (
    idParametro INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
    descripcionParametro VARCHAR(200) NOT NULL,
    valorParametro VARCHAR(200) NOT NULL
);

INSERT INTO parametria
VALUES
('Temperatura mínima',35.9),
('Temperatura máxima',37.5),
('Email del consultorio','consultorio.medico@elea.com'),
('Texto de no habilitación a mostrar','Usted no está habilitado para concurrir al trabajo. Por favor, comuníquese con Servicio Médico por teléfono o Whatsapp al 011 3867-3669.');


CREATE TABLE lugarAcceso (
    idLugarAcceso INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
    descripcionLugarAcceso VARCHAR(50) NOT NULL
);

INSERT INTO lugarAcceso
VALUES
('Planta VDM'),
('Planta Disprofarma'),
('Planta Pilar'),
('Planta Humahuaca'),
('Fuerza de Venta'),
('Home Office');

CREATE TABLE pantallas (
    idPantalla INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
    descripcionPantalla VARCHAR(50) NOT NULL
);

INSERT INTO pantallas
VALUES
('Temperatura'),
('Síntomas'),
('Antecedentes');

CREATE TABLE empleadosActivos (
    idUsuario INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
    nroLegajo VARCHAR(10) NOT NULL,
    dni VARCHAR(10) NOT NULL,
    nombre VARCHAR(25) NOT NULL,
    apellido VARCHAR(25) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    emailLaboral VARCHAR(50) NOT NULL
);

INSERT INTO empleadosActivos
VALUES
(12345678,12345678,'Jose','Rodriguez',70758000,'jose.rodriguez@elea.com'),
(23456789,23456789,'Pedro','Mendoza',70758001,'pedro.mendoza@elea.com'),
(34567891,34567891,'Agustín','Perez',70758002,'agustin.perez@elea.com');

CREATE TABLE autodiagnostico (
    idAutodiagnostico INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
    nroLegajo VARCHAR(10) NULL,
    dni VARCHAR(10) NOT NULL,
    nombre VARCHAR(25) NOT NULL,
    apellido VARCHAR(25) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    empresa VARCHAR(50) NOT NULL,
    emailLaboral VARCHAR(50) NULL,
    emailUsuario VARCHAR(50) NOT NULL,
    idLugarAcceso INT NOT NULL FOREIGN KEY REFERENCES lugarAcceso(idLugarAcceso),
    estadoSintomas BIT NOT NULL,
    estadoContactoEstrecho BIT NOT NULL,
    estadoAntecedentes BIT NOT NULL,
    resultado BIT NOT NULL,
    fecha_autodiagnostico SMALLDATETIME NOT NULL,
    fecha_hasta_resultado SMALLDATETIME NOT NULL,
    comentario VARCHAR(100) NULL,
    modificadoPor VARCHAR(10) NULL,
    modificadoEn SMALLDATETIME NULL
);

CREATE TABLE preguntas (
    idPregunta INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
    idPantalla INT NOT NULL FOREIGN KEY REFERENCES pantallas(idPantalla),
    descripcionPregunta VARCHAR(200) NOT NULL,
    idOrdenEnPantalla INT NOT NULL,
    estadoLogico BIT NOT NULL
);

INSERT INTO preguntas
VALUES
(1,'¿Cuál es tu temperatura corporal actual?',0,1),
(2,'¿Percibiste una marcada pérdida del olfato de manera repentina?',0,1),
(2,'¿Percibiste una marcada pérdida del gusto (sabor de los alimentos) de manera repentina?',1,1),
(2,'¿Tenés tos?',2,1),
(2,'¿Tenés dolor de garganta?',3,1),
(2,'¿Tenés dificultad respiratoria o falta de aire?',4,1),
(2,'¿Tenés dolor de cabeza?',5,1),
(2,'¿Tenés diarrea?',6,1),
(2,'¿Tenés vómitos?',7,1),
(2,'¿Tenés dolor muscular?',8,1),
(3,'Trabajo o convivo con una persona que actualmente es caso confirmado o sospechoso de COVID-19.',0,1),
(3,'Pasé en los últimos 14 días al menos 15 minutos sin barbijo y a menos de 2 metros de distancia de una persona que actualmente es caso confirmado de COVID-19.',1,1),
(3,'Tengo/tuve cancer.',2,1),
(3,'Tengo diabetes.',3,1),
(3,'Tengo alguna enfermedad hepática.',4,1),
(3,'Tengo enfermedad renal crónica.',5,1),
(3,'Tengo alguna enfermedad respiratoria.',6,1),
(3,'Tengo alguna enfermedad cardiológica.',7,1),
(3,'Tengo alguna condición que baja las defensas.',8,1);

CREATE TABLE respuestas (
    id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
    idAutodiagnostico INT NOT NULL FOREIGN KEY REFERENCES autodiagnostico(idAutodiagnostico),
    idPregunta INT NOT NULL FOREIGN KEY REFERENCES preguntas(idPregunta),
    respuestaPregunta VARCHAR(10) NOT NULL
);
