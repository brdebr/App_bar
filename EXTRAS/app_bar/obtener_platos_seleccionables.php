<?php
/**
 * Obtiene todas las metas de la base de datos
 */
header('Content-Type: text/html; charset=UTF-8');
require 'plato.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    // Manejar petición GET
		
	$platos = Plato::obtener_seleccionables();		

    if ($platos) {

        $datos["estado"] = 1;
        $datos["platos"] = $platos;

        print json_encode($datos);
    } else {
        print json_encode(array(
            "estado" => 2,
            "mensaje" => "No hay platos en esa categoria",
        ));
    }
}