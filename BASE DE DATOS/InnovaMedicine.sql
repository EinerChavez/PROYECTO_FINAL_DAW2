-- Creamos la base de datos Innova Medicine para proyecto
CREATE DATABASE IF NOT EXISTS InnovaMedicine
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE InnovaMedicine;


-- Creamos la estructura de tablas para nuestra base de datos
-- Tabla para los usuarios, auqellos que usaran el sistema, tanto medicos como pacientes
CREATE TABLE USUARIOS (
    ID_USUARIO INT AUTO_INCREMENT PRIMARY KEY,
    NOMBRE VARCHAR(100) NOT NULL,
    APELLIDO VARCHAR(100) NOT NULL,
    SEXO ENUM('Masculino', 'Femenino', 'Otro') NOT NULL,
    TELEFONO VARCHAR(20) NOT NULL,
    EMAIL VARCHAR(100) NOT NULL UNIQUE,
    CONTRASENIA VARCHAR(150) NOT NULL
);

/* correco@.com password123 = paciente
   thv.chamakito@example.com password123 = medico
   Token = "QhwOQohWEdy6hrtEVpCR8IMJFgkSl57g"
   */

-- Tabla de Médicos, solo para aquellos usuarios que tengan un rol de 'Medico'
CREATE TABLE MEDICOS (
    ID_USUARIO INT PRIMARY KEY, -- Igual que ID_USUARIO
    ESPECIALIDAD VARCHAR(100) NOT NULL,
    NUMERO_COLEGIADO VARCHAR(50) NOT NULL,
    CODIGO_MEDICO_HOSPITAL VARCHAR(100) NOT NULL UNIQUE,
    FOREIGN KEY (ID_USUARIO) REFERENCES USUARIOS(ID_USUARIO) ON DELETE CASCADE
);

-- Tabla de Pacientes, solo para aquellos usuarios que tengan un rol de 'Paciente'
CREATE TABLE PACIENTES (
    ID_USUARIO INT PRIMARY KEY, -- Igual que ID_USUARIO
    FECHA_NACIMIENTO DATE NOT NULL,
    TALLA VARCHAR(100) NOT NULL,
    GRUPO_SANGUINEO VARCHAR(5) NOT NULL,
    DIRECCION VARCHAR(255) NOT NULL,
    FOREIGN KEY (ID_USUARIO) REFERENCES USUARIOS(ID_USUARIO) ON DELETE CASCADE
);

-- Tabla para agendar las citas medicas realizadas
CREATE TABLE CITAS (
	ID_CITAS INT auto_increment PRIMARY KEY,
    ID_MEDICO INT NOT NULL,
    ID_PACIENTE INT NOT NULL,
    FECHA DATE NOT NULL,
    HORA TIME NOT NULL,
    TRATAMIENTO VARCHAR(255),
    NOTAS_MEDICAS TEXT,
    DIAGNOSTICO VARCHAR(255),
    ESTADO ENUM('Pendiente', 'Finalizada') DEFAULT 'Pendiente',
    FOREIGN KEY (ID_MEDICO) REFERENCES MEDICOS(ID_USUARIO) ON DELETE CASCADE,
    FOREIGN KEY (ID_PACIENTE) REFERENCES PACIENTES(ID_USUARIO) ON DELETE CASCADE,
    UNIQUE (ID_MEDICO, FECHA, HORA)
);


CREATE TABLE RECETAS (
	ID_RECETA INT auto_increment PRIMARY KEY,
    ID_CITA INT NOT NULL UNIQUE,
    INSTRUCCIONES_ADICIONALES TEXT,
    FIRMA_MEDICO VARCHAR(255),
    FECHA DATE NOT NULL,
    FOREIGN KEY (ID_CITA) REFERENCES CITAS(ID_CITAS) ON DELETE CASCADE
);

CREATE TABLE MEDICAMENTOS_RECETA (
	ID_MEDICAMENTO INT auto_increment PRIMARY KEY,
    ID_RECETA INT NOT NULL,
    MEDICAMENTO VARCHAR(100) NOT NULL,
    DOSIS VARCHAR(50),
    FRECUENCIA VARCHAR(50),
    DURACION VARCHAR(50),
    OBSERVACIONES TEXT,
    CONSTRAINT fk_medicamento_receta
    FOREIGN KEY (ID_RECETA) REFERENCES RECETAS(ID_RECETA) ON DELETE CASCADE
);


-- Tabla para la disponibilidad de los medicos por día y horario
CREATE TABLE DISPONIBILIDAD_MEDICA (
	ID_DISPONIBILIDAD INT AUTO_INCREMENT PRIMARY KEY,
    ID_MEDICO INT NOT NULL,
    DIA_SEMANA ENUM('Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado', 'Domingo') NOT NULL,
    HORA_INICIO TIME NOT NULL,
    HORA_FIN TIME NOT NULL,
    FOREIGN KEY (ID_MEDICO) REFERENCES MEDICOS(ID_USUARIO) ON DELETE CASCADE
);

DELIMITER //
CREATE PROCEDURE registrar_cita_con_receta_vacia(
    IN p_id_medico INT,
    IN p_id_paciente INT,
    IN p_fecha DATE,
    IN p_hora TIME,
    IN p_tratamiento VARCHAR(255),
    OUT p_id_cita_generada INT
)
BEGIN
    -- Iniciar transacción
    START TRANSACTION;

    -- 1. Insertar cita médica
    INSERT INTO CITAS (
        ID_MEDICO, ID_PACIENTE, FECHA, HORA, TRATAMIENTO,
        NOTAS_MEDICAS, DIAGNOSTICO
    ) VALUES (
        p_id_medico, p_id_paciente, p_fecha, p_hora, p_tratamiento,
        'aun no detallado', 'aun no detallado'
    );

    -- 2. Obtener el ID_CITAS recién generado
    set p_id_cita_generada = LAST_INSERT_ID();

    -- 3. Insertar receta vacía asociada a la cita
    INSERT INTO RECETAS (
        ID_CITA, INSTRUCCIONES_ADICIONALES, FIRMA_MEDICO, FECHA
    ) VALUES (
        p_id_cita_generada, 'aun no detallado', 'aun no detallado', p_fecha
    );

    -- 4. Confirmar transacción
    COMMIT;
END //
DELIMITER ;

SELECT*FROM USUARIOS;
SELECT*FROM MEDICOS;
SELECT*FROM CITAS;
SELECT*FROM RECETAS;
SELECT*FROM MEDICAMENTOS_RECETA;
SELECT*FROM PACIENTES;

