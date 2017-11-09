<?php
/**
 * Obtiene todas las metas de la base de datos
 */
header('Content-Type: text/html; charset=UTF-8');
require 'pedido.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

	if (isset($_POST['id_pedido'])){
		
	$parametro1 = $_POST['id_pedido'];
	$parametro2 = $_POST['comensales'];
		
	$pedido = Pedido::modificar_comensales($parametro1, $parametro2);		

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
