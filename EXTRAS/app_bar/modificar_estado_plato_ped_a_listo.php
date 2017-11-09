<?php
/**
 * Obtiene todas las metas de la base de datos
 */
header('Content-Type: text/html; charset=UTF-8');
require 'pedido.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

	if (isset($_POST['id_plato_ped'])){
		
	$parametro1 = $_POST['id_plato_ped'];
	$parametro2 = $_POST['estado'];
		
	switch ($parametro2) {
		case 0:
			$aux = 1;
			break;
		case 1:
			$aux = 2;
			break;
		default:
			$aux = 0;
			break;	
	}
		
	$pedido = Pedido::modificar_estado($parametro1, $aux);		

    if ($pedido) {

        $datos["estado"] = 1;
        print json_encode($datos);
    } else {
        print json_encode(array(
            "estado" => 2,
            "mensaje" => "Ha ocurrido un error"
        ));
    }
		
	}
	else{
		print json_encode(array(
            "estado" => 3,
            "mensaje" => "Falta el id",
        ));
	}
}
