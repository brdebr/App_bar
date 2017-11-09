<?php
/**
 * Obtiene todas las metas de la base de datos
 */
header('Content-Type: text/html; charset=UTF-8');
require 'pedido.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

	$pedidos = Pedido::get_pedidos_activos();

	print json_encode(array(
	 "n_pedidos" => $pedidos));

}
