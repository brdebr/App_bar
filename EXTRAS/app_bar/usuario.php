<?php

/**
 * Representa el la estructura de las metas
 * almacenadas en la base de datos
 */
require 'baseDatos.php';

class Usuario {

    function __construct() {
        
    }

    public static function loguear($nombre,$password) {
        // Consulta de la meta
        $consulta = "SELECT nombre, password, grupo FROM usuarios
                             WHERE nombre = ? AND password = ?";

        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando->execute(array($nombre,$password));
            // Capturar primera fila del resultado
            $row = $comando->fetch(PDO::FETCH_ASSOC);
            return $row;
        } catch (PDOException $e) {
            // Aquí puedes clasificar el error dependiendo de la excepción
            // para presentarlo en la respuesta Json
            return -1;
        }
    }

}

?>