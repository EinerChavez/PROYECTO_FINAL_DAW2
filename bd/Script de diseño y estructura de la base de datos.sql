-- Creamos la base de datos Innova Medicine para proyecto
CREATE DATABASE IF NOT EXISTS InnovaMedicine
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE InnovaMedicine;



-- Creamos la estructura de tablas para nuestra base de datos
-- Tabla para los usuarios, auqellos que usaran el sistema, tanto medicos como pacientes
CREATE TABLE USUARIOS (
	ID_USUARIO INT auto_increment PRIMARY KEY,
    NOMBRE VARCHAR(100) NOT NULL,
    APELLIDO VARCHAR(100) NOT NULL,
    SEXO ENUM('Masculino', 'Femenino', 'Otro') NOT NULL,
    TELEFONO VARCHAR(20) NOT NULL,
    EMAIL VARCHAR(100) NOT NULL UNIQUE,
    CONTRASENIA VARCHAR(150) NOT NULL,
    ROL ENUM('Paciente', 'Medico') NOT NULL
);

-- Tabla de Médicos, solo para aquellos usuarios que tengan un rol de 'Medico'
CREATE TABLE MEDICOS (
	ID_MEDICO INT PRIMARY KEY,
    ESPECIALIDAD VARCHAR(100) NOT NULL,
    NUMERO_COLEGIADO VARCHAR(50) NOT NULL,
    CODIGO_MEDICO_HOSPITAL VARCHAR(100) NOT NULL UNIQUE,
    FOREIGN KEY (ID_MEDICO) REFERENCES USUARIOS(ID_USUARIO) ON DELETE CASCADE
);

-- Tabla de Pacientes, solo para aquellos usuarios que tengan un rol de 'Paciente'
CREATE TABLE PACIENTES (
	ID_PACIENTE INT PRIMARY KEY,
    FECHA_NACIMIENTO DATE NOT NULL,
    TALLA VARCHAR(100) NOT NULL,
    GRUPO_SANGUINEO VARCHAR(5) NOT NULL,
    DIRECCION VARCHAR(255) NOT NULL,
    FOREIGN KEY (ID_PACIENTE) REFERENCES USUARIOS(ID_USUARIO)
);

-- Tabla para agendar las citas medicas realizadas
CREATE TABLE CITAS (
	ID_CITAS INT auto_increment PRIMARY KEY,
    ID_MEDICO INT NOT NULL,
    ID_PACIENTE INT NOT NULL,
    FECHA_HORA DATETIME NOT NULL,
    MOTIVO VARCHAR(255),
    ESTADO ENUM('Pendiente', 'Confirmada', 'Cancelada', 'Finalizada') DEFAULT 'Pendiente',
    FOREIGN KEY (ID_MEDICO) REFERENCES MEDICOS(ID_MEDICO),
    FOREIGN KEY (ID_PACIENTE) REFERENCES PACIENTES(ID_PACIENTE),
    UNIQUE (ID_MEDICO, FECHA_HORA)
);

-- Tabla para la disponibilidad de los medicos por día y horario
CREATE TABLE DISPONIBILIDAD_MEDICA (
	ID_DISPONIBILIDAD INT AUTO_INCREMENT PRIMARY KEY,
    ID_MEDICO INT NOT NULL,
    DIA_SEMANA ENUM('Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado', 'Domingo') NOT NULL,
    HORA_INICIO TIME NOT NULL,
    HORA_FIN TIME NOT NULL,
    FOREIGN KEY (ID_MEDICO) REFERENCES MEDICOS(ID_MEDICO) ON DELETE CASCADE
);



-- Triggers para validaciones de tipos de usuarios
-- Para validar los roles: Medico
DELIMITER $$
CREATE TRIGGER verificar_rol_medico
BEFORE INSERT ON MEDICOS
FOR EACH ROW
BEGIN
    DECLARE rol_usuario ENUM('Paciente', 'Medico');

    SELECT ROL INTO rol_usuario
    FROM USUARIOS
    WHERE ID_USUARIO = NEW.ID_MEDICO;

    IF rol_usuario != 'Medico' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El usuario no tiene rol de Medico';
    END IF;
END$$
DELIMITER ;

-- Para validar los roles: Paciente
DELIMITER $$
CREATE TRIGGER verificar_rol_paciente
BEFORE INSERT ON PACIENTES
FOR EACH ROW
BEGIN
    DECLARE rol_usuario ENUM('Paciente', 'Medico');

    SELECT ROL INTO rol_usuario
    FROM USUARIOS
    WHERE ID_USUARIO = NEW.ID_PACIENTE;

    IF rol_usuario != 'Paciente' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El usuario no tiene rol de Paciente';
    END IF;
END$$
DELIMITER ;



-- Procedimientos almacenados

-- Nota: Este procedure: 'registrar_usuario_condicional' solo es una muestra de como deberia funcionar 
-- la logica en spring boot, para conseguir el registro correcto, toda la toma de desiciones y
-- validaciones se hace de ese lado del servicio. Los procedimientos a utilizar seran los dos de hasta abajo,
-- ellos guardan la insersion de datos correctos para la validacion hecha y se aplicaran los datos segun el
-- tipo seleccionado de procedure.

-- Registro de usuario
DELIMITER $$
CREATE PROCEDURE registrar_usuario_condicional (
    IN p_nombre VARCHAR(100),
    IN p_apellido VARCHAR(100),
    IN p_sexo ENUM ('Masculino', 'Femenino', 'Otro'),
    IN p_telefono VARCHAR(20),
    IN p_email VARCHAR(100),
    IN p_contrasenia VARCHAR(150),

    -- Campos paciente (opcionales)
    IN p_fecha_nacimiento DATE,
    IN p_talla VARCHAR(100),
    IN p_grupo_sanguineo VARCHAR(5),
    IN p_direccion VARCHAR(255),

    -- Campos médico (opcionales)
    IN p_especialidad VARCHAR(100),
    IN p_numero_colegiado VARCHAR(50),
    IN p_codigo_hospital VARCHAR(50)
)
BEGIN
    DECLARE v_id_usuario INT;
    DECLARE v_rol ENUM('Paciente', 'Medico');
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        -- Si ocurre un error, revertimos la transacción
        ROLLBACK;
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Ocurrió un error durante el registro. Se cancelaron los cambios.';
    END;

    START TRANSACTION;

    -- Determinamos el rol en base al código hospital
    IF p_codigo_hospital IS NOT NULL AND p_codigo_hospital != '' THEN
        SET v_rol = 'Medico';
    ELSE
        SET v_rol = 'Paciente';
    END IF;

    -- Insertamos el usuario
    INSERT INTO USUARIOS (NOMBRE, APELLIDO, SEXO, TELEFONO, EMAIL, CONTRASENIA, ROL)
    VALUES (p_nombre, p_apellido, p_sexo, p_telefono, p_email, p_contrasenia, v_rol);

    SET v_id_usuario = LAST_INSERT_ID();

    -- Insertamos en la tabla correspondiente dependiendo de su rol
    IF v_rol = 'Paciente' THEN
        INSERT INTO PACIENTES (ID_PACIENTE, FECHA_NACIMIENTO, TALLA, GRUPO_SANGUINEO, DIRECCION)
        VALUES (v_id_usuario, p_fecha_nacimiento, p_talla, p_grupo_sanguineo, p_direccion);
    ELSEIF v_rol = 'Medico' THEN
        INSERT INTO MEDICOS (ID_MEDICO, ESPECIALIDAD, NUMERO_COLEGIADO, CODIGO_HOSPITAL)
        VALUES (v_id_usuario, p_especialidad, p_numero_colegiado, p_codigo_hospital);
    END IF;

    COMMIT;
END$$
DELIMITER ;


-- Procedure para registro de paciente
DELIMITER $$
CREATE PROCEDURE registrar_paciente (
    IN p_nombre VARCHAR(100),
    IN p_apellido VARCHAR(100),
    IN p_sexo ENUM('Masculino', 'Femenino', 'Otro'),
    IN p_telefono VARCHAR(20),
    IN p_email VARCHAR(100),
    IN p_contrasenia VARCHAR(150),
    IN p_fecha_nacimiento DATE,
    IN p_talla VARCHAR(100),
    IN p_grupo_sanguineo VARCHAR(5),
    IN p_direccion VARCHAR(255)
)
BEGIN
    DECLARE v_id_usuario INT;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error al registrar paciente.';
    END;

    START TRANSACTION;

    INSERT INTO USUARIOS (NOMBRE, APELLIDO, SEXO, TELEFONO, EMAIL, CONTRASENIA, ROL)
    VALUES (p_nombre, p_apellido, p_sexo, p_telefono, p_email, p_contrasenia, 'Paciente');

    SET v_id_usuario = LAST_INSERT_ID();

    INSERT INTO PACIENTES (ID_PACIENTE, FECHA_NACIMIENTO, TALLA, GRUPO_SANGUINEO, DIRECCION)
    VALUES (v_id_usuario, p_fecha_nacimiento, p_talla, p_grupo_sanguineo, p_direccion);

    COMMIT;
END$$
DELIMITER ;

-- Procedure para el registro de medicos
DELIMITER $$
CREATE PROCEDURE registrar_medico (
    IN p_nombre VARCHAR(100),
    IN p_apellido VARCHAR(100),
    IN p_sexo ENUM('Masculino', 'Femenino', 'Otro'),
    IN p_telefono VARCHAR(20),
    IN p_email VARCHAR(100),
    IN p_contrasenia VARCHAR(150),
    IN p_especialidad VARCHAR(100),
    IN p_numero_colegiado VARCHAR(50),
    IN p_codigo_hospital VARCHAR(50)
)
BEGIN
    DECLARE v_id_usuario INT;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error al registrar médico.';
    END;

    START TRANSACTION;

    INSERT INTO USUARIOS (NOMBRE, APELLIDO, SEXO, TELEFONO, EMAIL, CONTRASENIA, ROL)
    VALUES (p_nombre, p_apellido, p_sexo, p_telefono, p_email, p_contrasenia, 'Medico');

    SET v_id_usuario = LAST_INSERT_ID();

    INSERT INTO MEDICOS (ID_MEDICO, ESPECIALIDAD, NUMERO_COLEGIADO, CODIGO_HOSPITAL)
    VALUES (v_id_usuario, p_especialidad, p_numero_colegiado, p_codigo_hospital);

    COMMIT;
END$$
DELIMITER ;