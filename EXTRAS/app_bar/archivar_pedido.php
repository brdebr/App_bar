<?php
/**
 * Obtiene todas las metas de la base de datos
 */
header('Content-Type: text/html; charset=UTF-8');
require 'pedido.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

	if (isset($_POST['id_pedido'])) {

		$parametro1 = $_POST['id_pedido'];

		$platos = Pedido::archivar_pedido($parametro1);

		if ($platos) {
			print json_encode(array("estado" => 1, "mensaje" => "Ok"));
		} else {
			print json_encode(array("estado" => 2, "mensaje" => "Error"));
		}
	}
}
