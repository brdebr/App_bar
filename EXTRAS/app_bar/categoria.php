<?php
header('Content-Type: text/html; charset=UTF-8');
/**
 * Representa el la estructura de las metas
 * almacenadas en la base de datos
 */
require 'baseDatos.php';

class Categoria {

    function __construct() {
        
    }
	
    public static function getForDrawer() {
        $consulta = "SELECT nombre,imagen,color,color_letra
							FROM categorias ORDER BY idcategorias";
        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando->execute();

            return $comando->fetchAll(PDO::FETCH_ASSOC);
        } catch (PDOException $e) {
            return false;
        }
    }
	
	public static function getForList() {
        $consulta = "SELECT nombre,color
							FROM categorias ORDER BY idcategorias";
        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando->execute();

            return $comando->fetchAll(PDO::FETCH_ASSOC);
        } catch (PDOException $e) {
            return false;
        }
    }

}

?>