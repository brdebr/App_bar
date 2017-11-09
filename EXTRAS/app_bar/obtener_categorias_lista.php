<?php
/**
 * Obtiene todas las metas de la base de datos
 */
header('Content-Type: text/html; charset=UTF-8');
require 'categoria.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    // Manejar petición GET
    $categorias = Categoria::getForList();

    if ($categorias) {
		
        $datos["estado"] = 1;
        $datos["categorias"] = $categorias;

        print json_encode($datos);
    } else {
        print json_encode(array(
            "estado" => 2,
            "mensaje" => "Ha ocurrido un error"
        ));
    }
}