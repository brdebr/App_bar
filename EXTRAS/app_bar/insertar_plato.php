<?php
/**
 * Insertar una nueva meta en la base de datos
 */
header('Content-Type: text/html; charset=UTF-8');
require 'plato.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

	// Decodificando formato Json
	$body = json_decode(file_get_contents("php://input"), true);

	$imagen_data = $body['imagen_data'];
	$nombre_ruta = iconv("UTF-8", "ISO-8859-1//TRANSLIT", $body['nombre_img']);

	// Insertar plato
	$retorno = plato::insert($body['nombre'], $body['descripcion'], $body['precio'], $body['nombre_img'], $body['id_categoria']);

	$ruta = "imgs/$nombre_ruta.png";

	if ($retorno) {
		// Código de éxito
		print json_encode(array('estado' => '1', 'mensaje' => 'Creacion exitosa'));
		if (strlen($imagen_data) > 0) {
			file_put_contents($ruta, base64_decode($imagen_data));
			print json_encode(array('imagen' => 'Subida exitosa'));
		}

	} else {
		// Código de falla
		print json_encode(array('estado' => '2', 'mensaje' => 'Creacion fallida'));
	}

}
