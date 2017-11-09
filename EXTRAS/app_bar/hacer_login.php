<?PHP

header('Content-Type: application/json');
header("Accept", "application/json");
/**
 * Obtiene el detalle de una meta especificada por
 * su identificador "idMeta"
 */
require 'usuario.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    if ($_REQUEST['nombre']== "" || $_REQUEST['password'] == "") {

		// Enviar respuesta de error
        echo json_encode(
                array(
                    'estado' => '3',
                    'mensaje' => 'Faltan datos',
                )
        );
		
    } else {
        
		
		// Obtener parámetro
        $parametro1 = $_REQUEST['nombre'];
        $parametro2 = $_REQUEST['password'];


        // Tratar retorno
        $retorno = Usuario::loguear($parametro1, $parametro2);


        if ($retorno) {

            $usu["estado"] = "1";
            $usu["usuario"] = $retorno;
            // Enviar objeto json de la meta
            echo json_encode($usu);
        } else {
            // Enviar respuesta de error general
            echo json_encode(
			array(
                    'estado' => '2',
                    'mensaje' => 'Usuario o contraseña incorrecto',
                ));
        }
		
    }
}