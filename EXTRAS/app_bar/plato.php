<?php
header('Content-Type: text/html; charset=UTF-8');
/**
 * Representa el la estructura de las metas
 * almacenadas en la base de datos
 */
require 'baseDatos.php';

class Plato {

    function __construct() {
        
    }

    /**
     * Retorna en la fila especificada de la tabla 'plato'
     *
     * @param $idMeta Identificador del registro
     * @return array Datos del registro
     */
    public static function getAll() {
        $consulta = "SELECT 						
							p.idPlato,
							p.nombre,
                            p.descripcion,
                            p.precio,
							p.imagen,
                            c.nombre AS categoria,
							c.color
							
							FROM platos AS p
							INNER JOIN categorias as c 
                            ON
							p.id_categoria = c.idcategorias
							
							ORDER BY 
							idcategorias";
        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando->execute();

            return $comando->fetchAll(PDO::FETCH_ASSOC);
        } catch (PDOException $e) {
            return false;
        }
    }
	
	public static function obtener_seleccionables(){
		
		$consulta = "SELECT 						
							p.idPlato,
							p.nombre,
                            p.precio,
							p.imagen,
                            c.nombre AS categoria,
							c.color
							
							FROM platos AS p
							INNER JOIN categorias as c 
                            ON
							p.id_categoria = c.idcategorias
							
							ORDER BY 
							idcategorias";
        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
            // Ejecutar sentencia preparada
            $comando->execute();

            return $comando->fetchAll(PDO::FETCH_ASSOC);
        } catch (PDOException $e) {
            return false;
        }
		
	}


    public static function getById($idPlato) {
        // Consulta de la meta
        $consulta = "SELECT p.idPlato,
							p.nombre,
                            p.descripcion,
                            p.precio,
							p.imagen,
                            c.nombre AS categoria,
							c.color
							
							FROM platos AS p
							INNER JOIN categorias as c 
                            ON
							p.id_categoria = c.idcategorias
							
                            WHERE idPlato = :idPlato";

        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
			
			$comando->bindValue(':idPlato',(int) trim($idPlato),PDO::PARAM_INT);
            // Ejecutar sentencia preparada
            $comando->execute();
            // Capturar primera fila del resultado
            $row = $comando->fetch(PDO::FETCH_ASSOC);
            return $row;
        } catch (PDOException $e) {
            // Aquí puedes clasificar el error dependiendo de la excepción
            // para presentarlo en la respuesta Json
            return -1;
        }
    }
	
	public static function getByCategoria($categoria) {
       
        $consulta = "SELECT 						
							p.idPlato,
							p.nombre,
                            p.descripcion,
                            p.precio,
							p.imagen,
                            c.nombre AS categoria,
							c.color
							
							FROM platos AS p
							INNER JOIN categorias as c 
                            ON
							p.id_categoria = c.idcategorias
                            WHERE c.nombre = (SELECT nombre FROM `categorias` 
							ORDER BY 
							idcategorias LIMIT :categoria ,1)";

        try {
            // Preparar sentencia
            $comando = Database::getInstance()->getDb()->prepare($consulta);
			// Meter el valor como INT
			$comando->bindValue(':categoria',(int) trim($categoria),PDO::PARAM_INT);
            // Ejecutar sentencia preparada
            $comando->execute();
            return $comando->fetchAll(PDO::FETCH_ASSOC);
        } catch (PDOException $e) {
            // Aquí puedes clasificar el error dependiendo de la excepción
            // para presentarlo en la respuesta Json
            return $e;
        }
    }

    /**
     * Actualiza un registro de la bases de datos basado
     * en los nuevos valores relacionados con un identificador
     *
     * @param $idMeta      identificador
     * @param $titulo      nuevo titulo
     * @param $descripcion nueva descripcion
     * @param $fechaLim    nueva fecha limite de cumplimiento
     * @param $categoria   nueva categoria
     * @param $prioridad   nueva prioridad
     */
    public static function update(
    $idPlato, $nombre, $descripcion, $precio, $categoria
    ) {
        // Creando consulta UPDATE
        $consulta = "UPDATE Plato" .
                " SET nombre=?, descripcion=?, precio=?, categoria=? " .
                "WHERE idPlato=?";

        // Preparar la sentencia
        $cmd = Database::getInstance()->getDb()->prepare($consulta);

        // Relacionar y ejecutar la sentencia
        $cmd->execute(array($nombre, $descripcion, $precio, $categoria, $idPlato));

        return $cmd;
    }

    /**
     * Insertar una nueva meta
     *
     * @param $nombre      	nombre del plato nuevo
     * @param $descripcion 	descripción del plato nuevo
     * @param $precio    	precio del plato nuevo
     * @param $nombre_img   nombre imagen del plato nuevo
     * @param $id_categoria id de la categoria del plato nuevo
     * @return PDOStatement
     */
    public static function insert( $nombre, $descripcion, $precio, $nombre_img, $id_categoria ) 
	{
        // Sentencia INSERT
		
        $consulta = "INSERT INTO platos ( " .
                " nombre," .
                " descripcion," .
                " precio," .
                " imagen," .
                " id_categoria)" .
                " VALUES( ?,?,?,?,?)";

        // Preparar la sentencia
		$comando = Database::getInstance()->getDb()->prepare($consulta);
		$comando->execute(array(
					$nombre, $descripcion, $precio, $nombre_img, $id_categoria
		));
	
        return $comando;
    }

    /**
     * Eliminar el plato con el idPlato especificado
     *
     * @param $idPlato identificador del plato
     * @return bool Respuesta de la eliminación
     */
    public static function borrar_plato($idplato) {
        // Sentencia DELETE
        
        $consulta = "DELETE FROM platos
					WHERE idplato = :idplato";

        // Preparar la sentencia
        $comando = Database::getInstance()->getDb()->prepare($consulta);
		$comando->bindValue(':idplato',(int) trim($idplato),PDO::PARAM_INT);

        return $comando->execute();
    }
    
	
	/**
	 * Crea una entrada en la tabla historial_platos
	 * que marca quien ha borrado el plato y cuando.
	 */
	public static function delete_hist($usuario, $idplato) {
        
        $consulta = "UPDATE historial_platos 
					SET usuario = :usuario
					WHERE idplato = :idplato";

        // Preparar la sentencia
        $comando = Database::getInstance()->getDb()->prepare($consulta);
		$comando->bindValue(':usuario',$usuario,PDO::PARAM_STR);
		$comando->bindValue(':idplato',(int) trim($idplato),PDO::PARAM_INT);

        return $comando->execute();
    }

}

?>