<?php
/**
 * Obtiene todas las metas de la base de datos
 */
header('Content-Type: text/html; charset=UTF-8');
require 'plato.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

	if (isset($_POST['categoria'])){
		
	$parametro1 = $_POST['categoria'];
		
    $platos = Plato::getByCategoria($parametro1);

    if ($platos) {

        $datos["estado"] = 1;
        $datos["platos"] = $platos;

        print json_encode($datos);
		
    } else {
        print json_encode(array(
            "estado" => 2,
            "mensaje" => "No hay platos en esa categoría",
        ));
    }
		
	}
	else{
		print json_encode(array(
            "estado" => 2,
            "mensaje" => "Falta la categoria",
        ));
	}
    
}