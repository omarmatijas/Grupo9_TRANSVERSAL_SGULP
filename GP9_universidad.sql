SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";
-- Esto permite borrar y recrear las tablas aunque tengan relaciones entre sí.
SET FOREIGN_KEY_CHECKS = 0;
--
-- Base de datos: `grupo9_universidad`
--
DROP DATABASE IF EXISTS `grupo9_universidad`;
CREATE DATABASE IF NOT EXISTS `grupo9_universidad` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `grupo9_universidad`;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `alumno`
--

DROP TABLE IF EXISTS `alumno`;
CREATE TABLE IF NOT EXISTS `alumno` (
  `idAlumno` int(11) NOT NULL AUTO_INCREMENT,
  `dni` int(11) NOT NULL,
  `nombre` varchar(30) NOT NULL,
  `fecNac` date NOT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`idAlumno`),
  UNIQUE KEY `uk_alumno_dni` (`dni`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `materia`
--

DROP TABLE IF EXISTS `materia`;
CREATE TABLE IF NOT EXISTS `materia` (
  `idMateria` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) NOT NULL,
  `estado` tinyint(4) NOT NULL,
  PRIMARY KEY (`idMateria`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cursada`
--

DROP TABLE IF EXISTS `cursada`;
CREATE TABLE IF NOT EXISTS `cursada` (
  `idCursada` int(11) NOT NULL AUTO_INCREMENT,
  `idAlumno` int(11) NOT NULL,
  `idMateria` int(11) NOT NULL,
  `nota` float NOT NULL,
  `asist` float NOT NULL,
  `cursa` year(4) NOT NULL,
  PRIMARY KEY (`idCursada`),
  KEY `fk_cursada_alumno` (`idAlumno`),
  KEY `fk_cursada_materia` (`idMateria`),
  CONSTRAINT `fk_cursada_alumno` FOREIGN KEY (`idAlumno`) REFERENCES `alumno` (`idAlumno`),
  CONSTRAINT `fk_cursada_materia` FOREIGN KEY (`idMateria`) REFERENCES `materia` (`idMateria`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


SET FOREIGN_KEY_CHECKS = 1;
COMMIT;