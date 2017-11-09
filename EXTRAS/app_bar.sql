-- phpMyAdmin SQL Dump
-- version 4.6.4
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 09-11-2017 a las 05:07:56
-- Versión del servidor: 5.7.14
-- Versión de PHP: 5.6.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `app_bar`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `archivo_pedidos`
--

CREATE TABLE `archivo_pedidos` (
  `idarchivo_pedidos` int(11) NOT NULL,
  `idpedido` int(11) NOT NULL,
  `fecha_hora` timestamp(2) NULL DEFAULT NULL,
  `mesa` varchar(5) COLLATE utf8_spanish_ci NOT NULL,
  `comensales` int(3) NOT NULL,
  `notas` varchar(300) COLLATE utf8_spanish_ci DEFAULT NULL,
  `cobrado` tinyint(1) DEFAULT NULL,
  `creador` varchar(50) COLLATE utf8_spanish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `archivo_pedidos`
--

INSERT INTO `archivo_pedidos` (`idarchivo_pedidos`, `idpedido`, `fecha_hora`, `mesa`, `comensales`, `notas`, `cobrado`, `creador`) VALUES
(1, 4, '2017-02-13 23:00:00.00', '2', 4, NULL, NULL, 'Bryan');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `archivo_platos_en_pedidos`
--

CREATE TABLE `archivo_platos_en_pedidos` (
  `idarchivo_platos_en_pedidos` int(11) NOT NULL,
  `id_plat_ped` int(11) NOT NULL,
  `id_plato` int(11) NOT NULL,
  `id_pedido` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT '0',
  `precio` decimal(6,2) DEFAULT NULL,
  `is_beb_o_prep` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categorias`
--

CREATE TABLE `categorias` (
  `idcategorias` int(11) NOT NULL,
  `nombre` varchar(25) NOT NULL,
  `imagen` varchar(250) DEFAULT NULL,
  `color` varchar(11) NOT NULL DEFAULT '#eeeeee',
  `color_letra` varchar(11) NOT NULL DEFAULT '#000000'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `categorias`
--

INSERT INTO `categorias` (`idcategorias`, `nombre`, `imagen`, `color`, `color_letra`) VALUES
(1, 'Categoria 1', 'meat-1.png', '#33B5e5', '#000000'),
(2, 'Categoria 2', 'pizza-1.png', '#00c853', '#000000'),
(3, 'Categoria test', 'risotto.png', '#FF4081', '#000000');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `historial_platos`
--

CREATE TABLE `historial_platos` (
  `idhistorial_platos` int(11) NOT NULL,
  `idplato` int(11) NOT NULL,
  `nombre` varchar(30) NOT NULL,
  `descripcion` varchar(350) DEFAULT NULL,
  `precio` decimal(5,2) NOT NULL,
  `imagen` varchar(200) DEFAULT NULL,
  `id_categoria` int(11) DEFAULT NULL,
  `usuario` varchar(50) DEFAULT NULL,
  `timestamp` timestamp(2) NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Disparadores `historial_platos`
--
DELIMITER $$
CREATE TRIGGER `historial_platos_BEFORE_INSERT` BEFORE INSERT ON `historial_platos` FOR EACH ROW BEGIN
	SET new.timestamp = CURRENT_TIMESTAMP();
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `historial_platos_en_pedidos`
--

CREATE TABLE `historial_platos_en_pedidos` (
  `idhistorial_platos_en_pedidos` int(11) NOT NULL,
  `id_plat_ped` int(11) NOT NULL,
  `id_plato` int(11) NOT NULL,
  `id_pedido` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT '0',
  `precio` decimal(6,2) DEFAULT NULL,
  `is_beb_o_prep` tinyint(1) DEFAULT NULL,
  `timestamp` timestamp(2) NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `historial_platos_en_pedidos`
--

INSERT INTO `historial_platos_en_pedidos` (`idhistorial_platos_en_pedidos`, `id_plat_ped`, `id_plato`, `id_pedido`, `cantidad`, `estado`, `precio`, `is_beb_o_prep`, `timestamp`) VALUES
(1, 25, 46, 4, 5, 0, '12.50', 1, '2017-05-14 06:18:22.00'),
(2, 24, 40, 69, 2, 0, '40.24', 0, '2017-05-14 06:18:24.00'),
(3, 23, 46, 4, 3, 0, '7.50', 1, '2017-05-14 06:21:08.00'),
(4, 12, 44, 69, 22, 1, '570.90', 2, '2017-05-14 06:30:58.00'),
(5, 11, 44, 69, 5, 1, '130.00', 2, '2017-05-14 06:31:06.00'),
(6, 15, 45, 69, 1, 1, '20.10', 2, '2017-05-14 06:31:08.00'),
(7, 16, 46, 69, 1, 0, '2.50', 1, '2017-05-14 06:31:10.00'),
(8, 17, 43, 69, 2, 0, '40.60', 1, '2017-05-14 06:31:13.00'),
(9, 22, 46, 3, 3, 0, '7.50', 1, '2017-05-14 06:39:47.00'),
(10, 21, 46, 3, 2, 0, '5.00', 1, '2017-05-14 06:40:28.00'),
(11, 10, 40, 3, 2, 1, '40.00', 0, '2017-05-14 06:40:35.00'),
(12, 3, 44, 3, 8, 2, '208.00', 2, '2017-05-15 05:21:24.00'),
(13, 30, 45, 69, 13, 1, '261.30', 2, '2017-05-15 05:41:35.00'),
(14, 29, 40, 69, 12, 0, '241.44', 0, '2017-05-15 05:41:37.00'),
(15, 28, 43, 69, 5, 0, '101.50', 1, '2017-05-15 05:41:39.00'),
(16, 2, 43, 4, 1, 2, '20.00', 1, '2017-05-15 05:41:59.00'),
(17, 9, 43, 4, 3, 2, '61.00', 1, '2017-05-15 05:42:03.00'),
(18, 27, 46, 69, 3, 1, '7.50', 1, '2017-05-18 12:44:19.00'),
(19, 36, 45, 70, 1, 1, '20.10', 2, '2017-05-18 12:45:50.00'),
(20, 35, 46, 70, 2, 1, '5.00', 1, '2017-05-18 12:45:52.00'),
(21, 34, 40, 70, 4, 1, '80.48', 0, '2017-05-18 12:45:54.00'),
(22, 33, 44, 70, 5, 1, '129.75', 2, '2017-05-18 12:45:56.00'),
(23, 43, 46, 70, 44, 0, '110.00', 1, '2017-05-18 13:29:51.00'),
(24, 44, 45, 70, 12, 1, '241.20', 2, '2017-05-18 13:29:53.00'),
(25, 42, 43, 70, 2, 0, '40.60', 1, '2017-05-18 13:29:57.00'),
(26, 41, 40, 70, 4, 1, '80.48', 0, '2017-05-18 13:29:59.00'),
(27, 40, 44, 70, 6, 1, '155.70', 2, '2017-05-18 13:30:01.00'),
(28, 48, 45, 70, 1, 1, '20.10', 2, '2017-05-18 13:33:30.00'),
(29, 47, 46, 70, 2, 0, '5.00', 1, '2017-05-18 13:33:32.00'),
(30, 46, 43, 70, 3, 0, '60.90', 1, '2017-05-18 13:33:34.00'),
(31, 45, 40, 70, 4, 0, '80.48', 0, '2017-05-18 13:33:36.00'),
(32, 50, 45, 70, 1, 1, '20.10', 2, '2017-05-18 13:35:14.00'),
(33, 49, 46, 70, 2, 0, '5.00', 1, '2017-05-18 13:35:17.00'),
(34, 51, 43, 70, 3, 0, '60.90', 1, '2017-05-18 13:35:53.00'),
(35, 52, 46, 70, 2, 0, '5.00', 1, '2017-05-18 13:35:55.00'),
(36, 54, 43, 70, 2, 0, '40.60', 1, '2017-05-18 13:36:43.00'),
(37, 53, 40, 70, 3, 0, '60.36', 0, '2017-05-18 13:36:45.00'),
(38, 56, 43, 70, 2, 0, '40.60', 1, '2017-05-18 13:37:30.00'),
(39, 55, 40, 70, 3, 0, '60.36', 0, '2017-05-18 13:37:45.00'),
(40, 58, 43, 70, 2, 0, '40.60', 1, '2017-05-18 13:38:29.00'),
(41, 57, 40, 70, 2, 0, '40.24', 0, '2017-05-18 13:38:31.00'),
(42, 43, 45, 70, 2, 1, '40.20', 2, '2017-05-22 15:28:37.00'),
(43, 42, 46, 70, 3, 0, '7.50', 1, '2017-05-22 15:28:39.00'),
(44, 41, 43, 70, 4, 0, '81.20', 1, '2017-05-22 15:50:39.00'),
(45, 40, 40, 70, 5, 0, '100.60', 0, '2017-05-22 15:50:42.00'),
(46, 37, 44, 69, 5, 1, '129.75', 2, '2017-05-22 15:51:30.00'),
(47, 14, 44, 1, 3, 1, '77.85', 2, '2017-05-22 15:52:18.00'),
(48, 45, 40, 70, 1, 0, '20.12', 0, '2017-05-22 15:55:31.00'),
(49, 46, 43, 70, 6, 0, '121.80', 1, '2017-05-22 15:56:27.00'),
(50, 50, 43, 70, 5, 0, '101.50', 1, '2017-05-22 15:57:15.00'),
(51, 49, 46, 70, 6, 0, '15.00', 1, '2017-05-22 15:57:18.00'),
(52, 47, 46, 70, 2, 2, '5.00', 1, '2017-05-22 15:57:25.00');

--
-- Disparadores `historial_platos_en_pedidos`
--
DELIMITER $$
CREATE TRIGGER `historial_platos_en_pedidos_BEFORE_INSERT` BEFORE INSERT ON `historial_platos_en_pedidos` FOR EACH ROW BEGIN
	SET new.timestamp = CURRENT_TIMESTAMP();
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pedidos`
--

CREATE TABLE `pedidos` (
  `idpedido` int(11) NOT NULL,
  `fecha_hora` timestamp(2) NULL DEFAULT NULL,
  `mesa` varchar(5) NOT NULL,
  `comensales` int(3) NOT NULL,
  `notas` varchar(300) DEFAULT NULL,
  `cobrado` tinyint(1) DEFAULT NULL,
  `creador` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `pedidos`
--

INSERT INTO `pedidos` (`idpedido`, `fecha_hora`, `mesa`, `comensales`, `notas`, `cobrado`, `creador`) VALUES
(1, '2017-02-13 23:00:00.00', '3', 1, NULL, 1, 'Bryan'),
(3, '2017-02-13 23:00:00.00', '1', 21, NULL, NULL, 'Bryan'),
(4, '2017-02-13 23:00:00.00', '2', 4, NULL, NULL, 'Bryan'),
(5, '2017-02-25 23:00:00.00', '5', 5, NULL, NULL, 'Casa'),
(69, '2017-04-26 11:40:02.00', '22', 11, 'NOTAAAAS', 1, 'Bryan');

--
-- Disparadores `pedidos`
--
DELIMITER $$
CREATE TRIGGER `pedidos_BEFORE_INSERT` BEFORE INSERT ON `pedidos` FOR EACH ROW BEGIN
	set new.fecha_hora = CURRENT_TIMESTAMP;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `platos`
--

CREATE TABLE `platos` (
  `idplato` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `descripcion` varchar(350) DEFAULT NULL,
  `precio` decimal(5,2) NOT NULL,
  `imagen` varchar(100) DEFAULT NULL,
  `id_categoria` int(11) DEFAULT NULL,
  `is_beb_o_prep` tinyint(1) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `platos`
--

INSERT INTO `platos` (`idplato`, `nombre`, `descripcion`, `precio`, `imagen`, `id_categoria`, `is_beb_o_prep`) VALUES
(40, 'Producto 1', 'Cosas', '20.12', 'meat-1.png', 2, 0),
(43, 'Producto 2', 'Cosas lalalalalalalaa', '20.30', 'Producto_2', 1, 1),
(44, 'Gatinho', 'Un gato negro ', '25.95', 'Gatinho', 3, 2),
(45, 'Preparado', 'nada', '20.10', NULL, 1, 2),
(46, 'Cerveza', 'aaaaaa', '2.50', NULL, 1, 1),
(47, 'Cosa', 'ddd', '12.00', 'Cosa', 3, 0),
(48, 'Celofan', 'ssss', '22.00', 'Celofan', 3, 0);

--
-- Disparadores `platos`
--
DELIMITER $$
CREATE TRIGGER `backup_plato` BEFORE DELETE ON `platos` FOR EACH ROW INSERT INTO historial_platos (	historial_platos.idplato,
                             	historial_platos.nombre,
                             	historial_platos.descripcion,
                             	historial_platos.precio,
                             	historial_platos.imagen,
                             	historial_platos.id_categoria)
                        SELECT *
                                FROM platos 
                                WHERE platos.idplato = OLD.idplato
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `platos_en_pedidos`
--

CREATE TABLE `platos_en_pedidos` (
  `id_plat_ped` int(11) NOT NULL,
  `id_plato` int(11) NOT NULL,
  `id_pedido` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT '0',
  `precio` decimal(6,2) DEFAULT NULL,
  `is_beb_o_prep` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `platos_en_pedidos`
--

INSERT INTO `platos_en_pedidos` (`id_plat_ped`, `id_plato`, `id_pedido`, `cantidad`, `estado`, `precio`, `is_beb_o_prep`) VALUES
(1, 40, 4, 4, 2, '80.00', 0),
(4, 45, 1, 2, 2, '40.00', 2),
(5, 46, 4, 3, 2, '7.50', 1),
(13, 40, 1, 2, 0, '40.24', 0),
(18, 43, 1, 43, 0, '872.90', 1),
(19, 43, 3, 3, 2, '60.90', 1),
(20, 46, 1, 3, 2, '7.50', 1),
(21, 44, 3, 1, 1, '25.95', 2),
(22, 40, 3, 1, 2, '20.12', 0),
(23, 43, 3, 1, 2, '20.30', 1),
(24, 46, 3, 1, 1, '2.50', 1),
(25, 45, 3, 1, 2, '20.10', 2),
(26, 44, 69, 2, 1, '51.90', 2),
(31, 45, 69, 3, 2, '60.30', 2),
(32, 43, 4, 2, 2, '40.60', 1),
(38, 40, 69, 1, 2, '20.12', 0),
(39, 46, 69, 2, 0, '5.00', 1),
(49, 40, 1, 3, 2, '60.36', 0),
(50, 40, 3, 5, 0, '100.60', 0),
(51, 43, 3, 3, 0, '60.90', 1),
(52, 44, 3, 2, 1, '51.90', 2);

--
-- Disparadores `platos_en_pedidos`
--
DELIMITER $$
CREATE TRIGGER `backup_plato_en_pedido` BEFORE DELETE ON `platos_en_pedidos` FOR EACH ROW INSERT INTO historial_platos_en_pedidos (	
    
    historial_platos_en_pedidos.id_plat_ped,
    historial_platos_en_pedidos.id_plato,
    historial_platos_en_pedidos.id_pedido,
    historial_platos_en_pedidos.cantidad,
    historial_platos_en_pedidos.estado,
    historial_platos_en_pedidos.precio,
    historial_platos_en_pedidos.is_beb_o_prep
    
                                        )
                        SELECT *
                                FROM platos_en_pedidos 
                                WHERE platos_en_pedidos.id_plat_ped = OLD.id_plat_ped
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `platos_en_pedidos_BEFORE_INSERT` BEFORE INSERT ON `platos_en_pedidos` FOR EACH ROW BEGIN
    declare ais_beb_o_prep TINYINT(1);
	declare precio_prod DECIMAL(4,2);
    declare platoid INT;
    
    select precio from
    app_bar.platos where idplato=new.id_plato into precio_prod;
	set new.precio = precio_prod*new.cantidad;
    
    select is_beb_o_prep from
    app_bar.platos where idplato=new.id_plato into ais_beb_o_prep;
	set new.is_beb_o_prep = ais_beb_o_prep;
    
    if ais_beb_o_prep = 2 then
		set new.estado = 1;
    end if;
	
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `platos_en_pedidos_BEFORE_UPDATE` BEFORE UPDATE ON `platos_en_pedidos` FOR EACH ROW BEGIN
	declare ais_beb_o_prep TINYINT(1);
	declare precio_prod DECIMAL(4,2);
    declare platoid INT;
    
    select precio from
    app_bar.platos where idplato=new.id_plato into precio_prod;
	set new.precio = precio_prod*new.cantidad;
    
    select is_beb_o_prep from
    app_bar.platos where idplato=new.id_plato into ais_beb_o_prep;
	set new.is_beb_o_prep = ais_beb_o_prep;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `nombre` varchar(50) NOT NULL,
  `password` varchar(20) NOT NULL,
  `grupo` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`nombre`, `password`, `grupo`) VALUES
('Bryan', 'asdzxc', 1),
('Caja 01', 'asdzxc', 3),
('Camarero 01', 'asdzxc', 1),
('Casa', 'asdzxc', 1),
('Cocina 01', 'asdzxc', 2);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `archivo_pedidos`
--
ALTER TABLE `archivo_pedidos`
  ADD PRIMARY KEY (`idarchivo_pedidos`);

--
-- Indices de la tabla `archivo_platos_en_pedidos`
--
ALTER TABLE `archivo_platos_en_pedidos`
  ADD PRIMARY KEY (`idarchivo_platos_en_pedidos`);

--
-- Indices de la tabla `categorias`
--
ALTER TABLE `categorias`
  ADD PRIMARY KEY (`idcategorias`),
  ADD UNIQUE KEY `nombre` (`nombre`);

--
-- Indices de la tabla `historial_platos`
--
ALTER TABLE `historial_platos`
  ADD PRIMARY KEY (`idhistorial_platos`);

--
-- Indices de la tabla `historial_platos_en_pedidos`
--
ALTER TABLE `historial_platos_en_pedidos`
  ADD PRIMARY KEY (`idhistorial_platos_en_pedidos`);

--
-- Indices de la tabla `pedidos`
--
ALTER TABLE `pedidos`
  ADD PRIMARY KEY (`idpedido`),
  ADD UNIQUE KEY `mesa` (`mesa`),
  ADD KEY `fk_creador_pedido_idx` (`creador`);

--
-- Indices de la tabla `platos`
--
ALTER TABLE `platos`
  ADD PRIMARY KEY (`idplato`),
  ADD UNIQUE KEY `nombre` (`nombre`),
  ADD KEY `fk_categoria_nn_platos_idx` (`id_categoria`);

--
-- Indices de la tabla `platos_en_pedidos`
--
ALTER TABLE `platos_en_pedidos`
  ADD PRIMARY KEY (`id_plat_ped`),
  ADD KEY `fk_pedido_pla&ped_idx` (`id_pedido`),
  ADD KEY `fk_plato_pla&ped` (`id_plato`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`nombre`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `archivo_pedidos`
--
ALTER TABLE `archivo_pedidos`
  MODIFY `idarchivo_pedidos` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT de la tabla `archivo_platos_en_pedidos`
--
ALTER TABLE `archivo_platos_en_pedidos`
  MODIFY `idarchivo_platos_en_pedidos` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT de la tabla `categorias`
--
ALTER TABLE `categorias`
  MODIFY `idcategorias` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT de la tabla `historial_platos`
--
ALTER TABLE `historial_platos`
  MODIFY `idhistorial_platos` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=137;
--
-- AUTO_INCREMENT de la tabla `historial_platos_en_pedidos`
--
ALTER TABLE `historial_platos_en_pedidos`
  MODIFY `idhistorial_platos_en_pedidos` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=53;
--
-- AUTO_INCREMENT de la tabla `pedidos`
--
ALTER TABLE `pedidos`
  MODIFY `idpedido` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=70;
--
-- AUTO_INCREMENT de la tabla `platos`
--
ALTER TABLE `platos`
  MODIFY `idplato` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=49;
--
-- AUTO_INCREMENT de la tabla `platos_en_pedidos`
--
ALTER TABLE `platos_en_pedidos`
  MODIFY `id_plat_ped` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=53;
--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `pedidos`
--
ALTER TABLE `pedidos`
  ADD CONSTRAINT `fk_creador_pedido` FOREIGN KEY (`creador`) REFERENCES `usuarios` (`nombre`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `platos`
--
ALTER TABLE `platos`
  ADD CONSTRAINT `fk_categoria_nn_platos` FOREIGN KEY (`id_categoria`) REFERENCES `categorias` (`idcategorias`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Filtros para la tabla `platos_en_pedidos`
--
ALTER TABLE `platos_en_pedidos`
  ADD CONSTRAINT `fk_pedido_pla&ped` FOREIGN KEY (`id_pedido`) REFERENCES `pedidos` (`idpedido`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_plato_pla&ped` FOREIGN KEY (`id_plato`) REFERENCES `platos` (`idplato`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
