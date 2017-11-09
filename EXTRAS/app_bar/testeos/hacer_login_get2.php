<?PHP
header('Content-Type: application/json');
/**
 * Obtiene el detalle de una meta especificada por
 * su identificador "idMeta"
 */

require 'usuario.php';

if ($_SERVER['REQUEST_METHOD'] == 'GET') {

    if (isset($_GET['nombre'], $_GET['pass'])) {

        // Obtener parámetro
        $parametro1 = $_GET['nombre'];
        $parametro2 = $_GET['pass'];
		
        // Tratar retorno
        $retorno = Usuario::loguear($parametro1, $parametro2);


        if ($retorno) {

            $usu["estado"] = "1";
			$usu["param1"] = $parametro1;
			$usu["param2"] = $parametro2;
            $usu["usuario"] = $retorno;
            // Enviar objeto json de la meta
            echo json_encode($usu);
        } else {
            // Enviar respuesta de error general
            echo json_encode(
                array(
                    'estado' => '2',
                    'mensaje' => 'No se obtuvo el registro'
                )
            );
        }

    } else {
        //Enviar respuesta de error
        //echo json_encode(array($parametro1);
    }
}