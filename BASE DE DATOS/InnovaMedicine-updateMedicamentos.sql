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

SELECT*FROM USUARIOS;
SELECT*FROM MEDICOS;
SELECT*FROM CITAS;
SELECT*FROM RECETAS;
SELECT*FROM MEDICAMENTOS_RECETA;
SELECT*FROM PACIENTES;
SELECT*FROM DISPONIBILIDAD_MEDICA;

-- Insertar usuarios
INSERT INTO USUARIOS (NOMBRE, APELLIDO, SEXO, TELEFONO, EMAIL, CONTRASENIA) VALUES
('Carlos', 'Perez', 'Masculino', '999111222', 'carlos.medico@mail.com', '$2a$10$8KEaxGHJ9sGjFZK2E7Jjz.DIcKVR0dMkjEUV7Yo3BwH3OvPZ9BGSW'), -- carlos123
('Maria', 'Gomez', 'Femenino', '999333444', 'maria.medico@mail.com', '$2a$10$8KEaxGHJ9sGjFZK2E7Jjz.DIcKVR0dMkjEUV7Yo3BwH3OvPZ9BGSW'), -- maria123
('Luis', 'Ramirez', 'Masculino', '999555666', 'luis.medico@mail.com', '$2a$10$8KEaxGHJ9sGjFZK2E7Jjz.DIcKVR0dMkjEUV7Yo3BwH3OvPZ9BGSW'), -- luis123
('Ana', 'Salas', 'Femenino', '999777888', 'ana.medico@mail.com', '$2a$10$8KEaxGHJ9sGjFZK2E7Jjz.DIcKVR0dMkjEUV7Yo3BwH3OvPZ9BGSW'), -- ana123
('Pedro', 'Lopez', 'Masculino', '999999000', 'pedro.paciente@mail.com', '$2a$10$8KEaxGHJ9sGjFZK2E7Jjz.DIcKVR0dMkjEUV7Yo3BwH3OvPZ9BGSW'); -- pedro123

-- Insertar medicos
INSERT INTO MEDICOS (ID_USUARIO, ESPECIALIDAD, NUMERO_COLEGIADO, CODIGO_MEDICO_HOSPITAL) VALUES
(1, 'Cardiologia', 'COL12345', 'MED-HOSP-001'),
(2, 'Pediatria', 'COL12346', 'MED-HOSP-002'),
(3, 'Dermatologia', 'COL12347', 'MED-HOSP-003'),
(4, 'Neurologia', 'COL12348', 'MED-HOSP-004');

-- Insertar paciente (usuario 5)
INSERT INTO PACIENTES (ID_USUARIO, FECHA_NACIMIENTO, TALLA, GRUPO_SANGUINEO, DIRECCION) VALUES
(5, '1990-06-10', '1.70m', 'O+', 'Av. Los Olivos 123, Lima');

-- Insertar citas
INSERT INTO CITAS (ID_MEDICO, ID_PACIENTE, FECHA, HORA, TRATAMIENTO, NOTAS_MEDICAS, DIAGNOSTICO, ESTADO) VALUES
(3, 5, '2025-06-21', '10:30:00', 'Evaluacion pediatrica', 'Nino con fiebre.', 'Faringitis', 'Finalizada'),
(1, 5, '2025-06-20', '09:00:00', 'Revision general', 'Paciente estable.', 'Sin novedad', 'Finalizada'),
(2, 5, '2025-06-21', '10:30:00', 'Evaluacion pediatrica', 'Nino con fiebre.', 'Faringitis', 'Finalizada'),
(3, 5, '2025-06-22', '11:15:00', 'Tratamiento dermatologico', 'Reaccion alergica leve.', 'Dermatitis', 'Pendiente');

-- Insertar recetas
INSERT INTO RECETAS (ID_CITA, INSTRUCCIONES_ADICIONALES, FIRMA_MEDICO, FECHA) VALUES
(1, 'Tomar con alimentos.', 'Dr. Carlos Perez', '2025-06-20'),
(2, 'No exponer al sol.', 'Dra. Maria Gomez', '2025-06-21'),
(3, 'Evitar lacteos durante tratamiento.', 'Dr. Luis Ramirez', '2025-06-22');

-- Insertar medicamentos para cada receta
INSERT INTO MEDICAMENTOS_RECETA (ID_RECETA, MEDICAMENTO, DOSIS, FRECUENCIA, DURACION, OBSERVACIONES) VALUES
(1, 'Paracetamol 500mg', '1 tableta', 'Cada 8 horas', '3 dias', 'Tomar con agua'),
(2, 'Ibuprofeno 200mg', '1 tableta', 'Cada 6 horas', '2 dias', 'Suspender si hay dolor estomacal'),
(3, 'Clorfenamina', '1 capsula', 'Cada 12 horas', '5 dias', 'No manejar vehiculos');

-- Insertar disponibilidad medica
INSERT INTO DISPONIBILIDAD_MEDICA (ID_MEDICO, DIA_SEMANA, HORA_INICIO, HORA_FIN) VALUES
(1, 'Lunes', '08:00:00', '12:00:00'),
(1, 'Lunes', '14:00:00', '18:00:00'),
(1, 'Martes', '08:00:00', '12:00:00'),
(1, 'Martes', '14:00:00', '18:00:00'),
(1, 'Miercoles', '08:00:00', '12:00:00'),
(1, 'Miercoles', '14:00:00', '18:00:00'),
(1, 'Jueves', '08:00:00', '12:00:00'),
(1, 'Jueves', '14:00:00', '18:00:00'),
(1, 'Viernes', '08:00:00', '12:00:00'),
(1, 'Viernes', '14:00:00', '18:00:00'),
(1, 'Sabado', '08:00:00', '12:00:00'),

(2, 'Lunes', '08:00:00', '12:00:00'), (2, 'Lunes', '14:00:00', '18:00:00'),
(2, 'Martes', '08:00:00', '12:00:00'), (2, 'Martes', '14:00:00', '18:00:00'),
(2, 'Miercoles', '08:00:00', '12:00:00'), (2, 'Miercoles', '14:00:00', '18:00:00'),
(2, 'Jueves', '08:00:00', '12:00:00'), (2, 'Jueves', '14:00:00', '18:00:00'),
(2, 'Viernes', '08:00:00', '12:00:00'), (2, 'Viernes', '14:00:00', '18:00:00'),
(2, 'Sabado', '08:00:00', '12:00:00'),

(3, 'Lunes', '08:00:00', '12:00:00'), (3, 'Lunes', '14:00:00', '18:00:00'),
(3, 'Martes', '08:00:00', '12:00:00'), (3, 'Martes', '14:00:00', '18:00:00'),
(3, 'Miercoles', '08:00:00', '12:00:00'), (3, 'Miercoles', '14:00:00', '18:00:00'),
(3, 'Jueves', '08:00:00', '12:00:00'), (3, 'Jueves', '14:00:00', '18:00:00'),
(3, 'Viernes', '08:00:00', '12:00:00'), (3, 'Viernes', '14:00:00', '18:00:00'),
(3, 'Sabado', '08:00:00', '12:00:00'),

(4, 'Lunes', '08:00:00', '12:00:00'), (4, 'Lunes', '14:00:00', '18:00:00'),
(4, 'Martes', '08:00:00', '12:00:00'), (4, 'Martes', '14:00:00', '18:00:00'),
(4, 'Miercoles', '08:00:00', '12:00:00'), (4, 'Miercoles', '14:00:00', '18:00:00'),
(4, 'Jueves', '08:00:00', '12:00:00'), (4, 'Jueves', '14:00:00', '18:00:00'),
(4, 'Viernes', '08:00:00', '12:00:00'), (4, 'Viernes', '14:00:00', '18:00:00'),
(4, 'Sabado', '08:00:00', '12:00:00');


