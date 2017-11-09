<?php
/**
 * Obtiene todas las metas de la base de datos
 */
header('Content-Type: text/html; charset=UTF-8');
require 'pedido.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

	if (isset($_POST['creador'])) {
		
		$parametro1 = $_POST['creador'];
		
		// Manejar petición GET
		$pedidos = Pedido::getByCreador_estados($parametro1);
		
		if ($pedidos) {
			$datos["estado"] = 1;
			$datos["pedidos"] = $pedidos;

			print json_encode($datos);
		} else {
			print json_encode(array("estado" => 2, "mensaje" => "Ha ocurrido un error"));
		}
	}
}
