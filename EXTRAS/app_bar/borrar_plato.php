<?php
/**
 * Insertar una nueva meta en la base de datos
 */
header('Content-Type: text/html; charset=UTF-8');
require 'plato.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
	
	
	if(isset($_POST['idplato'],$_POST['usuario'])){

	$idplato = $_POST['idplato'];
	$usuario = $_POST['usuario'];
	$nombre_img = $_POST['nombre_img'];
	
	
	if ( strlen($nombre_img)!=0) {
		$nombre  = iconv("UTF-8", "ISO-8859-1//TRANSLIT", $nombre_img);
		$ruta = "imgs/$nombre.png";
	}else{
		$ruta = null;
	}
	
	$plato = Plato::borrar_plato($idplato);
	
    if ($plato) {
		
        $datos["estado"] = 1;
        $datos["mensaje"] = "Exito";
		if (strlen($ruta)>0) {
			unlink($ruta);
			$datos["img_borrada"] = true;
		}
		$datos["historial"] = Plato::delete_hist($usuario,$idplato);
        print json_encode($datos);
    } else {
        print json_encode(array(
            "estado" => 2,
            "mensaje" => "Ha ocurrido un error"
        ));
    }
	
	}
}