DROP TABLE respuestasPerfilEmpleado;
DROP TABLE perfilEmpleados;

CREATE TABLE perfilEmpleados (
    -- idPerfil INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
    nroLegajo VARCHAR(10) NOT NULL PRIMARY KEY, -- PK en vez de idPerfil?
    emailUsuario VARCHAR(50) NOT NULL,
    estadoLogico BIT NOT NULL
);

CREATE TABLE respuestasPerfilEmpleado (
    idRespuestaPerfil INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
    -- idPerfil INT NOT NULL FOREIGN KEY REFERENCES perfilEmpleados(idPerfil), -- nroLegajo VARCHAR()?
    nroLegajo VARCHAR(10) NOT NULL FOREIGN KEY REFERENCES perfilEmpleados(nroLegajo),
    idPregunta INT NOT NULL FOREIGN KEY REFERENCES preguntas(idPregunta),
    respuestaPregunta VARCHAR(50) NOT NULL
);