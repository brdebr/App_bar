<?php
/**
 * Obtiene todas las metas de la base de datos
 */
header('Content-Type: text/html; charset=UTF-8');
require 'pedido.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

	if (isset($_POST['id_pedido'])) {

		$parametro1 = $_POST['id_pedido'];

		$plato0 = 0;
		$plato1 = 0;
		$bebida0 = 0;
		$bebida1 = 0;

		// Manejar petición GET
		$pedidos = Pedido::getdatos_platos_pedido($parametro1);
		for ($i = 0; $i < count($pedidos); $i++) {
			if ($pedidos[$i]['is_beb_o_prep'] == 0) {
				$plato1++;
				if ($pedidos[$i]['estado'] == 2) {
					$plato0++;
				}
			}
			if ($pedidos[$i]['is_beb_o_prep'] == 1) {
				$bebida1++;
				if ($pedidos[$i]['estado'] == 2) {
					$bebida0++;
				}
			}
		}
		print "$plato0/$plato1 $bebida0/$bebida1";
	} else {
		print "?/? ?/?";
	}
}
