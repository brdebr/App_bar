<?php
/**
 * Obtiene todas las metas de la base de datos
 */
header('Content-Type: text/html; charset=UTF-8');
require 'pedido.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

	if (isset($_POST['creador'])) {
		
		$parametro1 = $_POST['creador'];
		$parametro2 = $_POST['comensales'];
		$parametro3 = $_POST['mesa'];
		
		if (Pedido::crearPedido($parametro1, $parametro2, $parametro3)) {
			print json_encode(
			array
			("id_pedido" => Pedido::comprobacionCreado($parametro1, $parametro2, $parametro3),
			 "estado" => 1
			));
		}
		else
		{
			//$datos['estado'] = 2;
			print json_encode(
			array
			("id_pedido" => -1,
			 "estado" => 2
			));
		}
		
		
	}
}
