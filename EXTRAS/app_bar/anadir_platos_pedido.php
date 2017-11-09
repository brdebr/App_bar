<?PHP

header('Content-Type: application/json');
header("Accept", "application/json");

require 'pedido.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

	$data = json_decode(file_get_contents('php://input'), true);

	if (isset($data)) {

		$parametro1 = $data['idpedido'];
		$parametro2 = $data['platos'];

		if (isset($parametro1)) {

			$pedido = Pedido::anadir_platos_en_pedido($parametro1, $parametro2);

			if ($pedido = "ok") {
				print json_encode(array("estado" => 1, "mensaje" => "Ok", ));

			} else {
				print json_encode(array("estado" => 2, "mensaje" => "Error", ));
			}

		}

	}

}
