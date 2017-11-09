<?php
header('Content-Type: text/html; charset=UTF-8');
/**
 * Representa el la estructura de las metas
 * almacenadas en la base de datos
 */
require 'baseDatos.php';

class Pedido {

	function __construct() {

	}//WHERE creador =

	public static function getAll() {
		$consulta = "SELECT
        				 idpedido,
        				 fecha_hora,
        				 mesa,
        				 comensales,
        				 notas,
        				 cobrado,
        				 creador 
        					 FROM  pedidos";
		try {
			// Preparar sentencia
			$comando = Database::getInstance() -> getDb() -> prepare($consulta);
			// Ejecutar sentencia preparada
			$comando -> execute();
			return $comando -> fetchAll(PDO::FETCH_ASSOC);

		} catch (PDOException $e) {
			return false;
		}
	}

	public static function getByCreador($creador) {

		$consulta = "SELECT
        				 idpedido,
        				 fecha_hora,
        				 mesa,
        				 comensales,
        				 cobrado,
        				 creador 
        					 FROM  pedidos WHERE creador = ?";

		try {
			// Preparar sentencia
			$comando = Database::getInstance() -> getDb() -> prepare($consulta);
			// Ejecutar sentencia preparada
			$comando -> execute(array($creador));

			return $comando -> fetchAll(PDO::FETCH_ASSOC);
		} catch (PDOException $e) {
			return false;
		}
	}

	public static function get_detalles($id_pedido) {
		$consulta = "SELECT
        				 idpedido,
        				 fecha_hora,
        				 mesa,
        				 comensales,
        				 notas,
        				 cobrado,
        				 creador 
        			     FROM  pedidos
        				 WHERE 
        				 idpedido = ?";
		try {
			// Preparar sentencia
			$comando = Database::getInstance() -> getDb() -> prepare($consulta);
			// Ejecutar sentencia preparada
			$comando -> execute(array($id_pedido));
			return $comando -> fetchAll(PDO::FETCH_ASSOC);

		} catch (PDOException $e) {
			return false;
		}
	}

	public static function modificar_comensales($idpedido, $comensales) {

		$consulta = "UPDATE 
						pedidos 
					SET 
						comensales = :comensales 
					WHERE 
						idpedido = :idpedido;";
		try {
			// Preparar sentencia
			$comando = Database::getInstance() -> getDb() -> prepare($consulta);
			// Ejecutar sentencia preparada
			$comando -> bindValue(':comensales', (int) trim($comensales), PDO::PARAM_INT);
			$comando -> bindValue(':idpedido', (int) trim($idpedido), PDO::PARAM_INT);

			return $comando -> execute();
			;

		} catch (PDOException $e) {
			return false;
		}
	}

	public static function modificar_notas($idpedido, $notas) {

		$consulta = "UPDATE 
						pedidos 
					SET 
						notas = :notas 
					WHERE 
						idpedido = :idpedido;";
		try {
			// Preparar sentencia
			$comando = Database::getInstance() -> getDb() -> prepare($consulta);
			// Ejecutar sentencia preparada
			$comando -> bindValue(':notas', $notas, PDO::PARAM_STR);
			$comando -> bindValue(':idpedido', (int) trim($idpedido), PDO::PARAM_INT);

			return $comando -> execute();

		} catch (PDOException $e) {
			return false;
		}
	}
	
	public static function modificar_estado($id_plato_ped, $estado) {

		$consulta = "UPDATE 
						platos_en_pedidos 
					SET 
						estado = :estado 
					WHERE 
						id_plat_ped = :id_plat_ped;";
		try {
			// Preparar sentencia
			$comando = Database::getInstance() -> getDb() -> prepare($consulta);
			// Ejecutar sentencia preparada
			$comando -> bindValue(':estado', (int) trim($estado), PDO::PARAM_INT);
			$comando -> bindValue(':id_plat_ped', (int) trim($id_plato_ped), PDO::PARAM_INT);

			return $comando -> execute();

		} catch (PDOException $e) {
			return false;
		}
	}
	

	public static function getByCreador_estados($creador) {

		$consulta = "SELECT
        				 p.idpedido,
        				 p.fecha_hora,
        				 p.mesa,
        				 p.comensales,
        				 p.cobrado,
        				 p.creador,
        				 
                         SUM(IF (d.is_beb_o_prep = 0 AND d.estado = 2, 1, 0)) as p1,
                         SUM(IF (d.is_beb_o_prep = 0, 1, 0)) as p2,
                         SUM(IF (d.is_beb_o_prep = 0 AND d.estado = 1, 1, 0)) as p3,
                         
                         SUM(IF (d.is_beb_o_prep = 1 AND d.estado = 2, 1, 0)) as b1,
                         SUM(IF (d.is_beb_o_prep = 1, 1, 0)) as b2,
                         SUM(IF (d.is_beb_o_prep = 1 AND d.estado = 1, 1, 0)) as b3
                        
        				 FROM pedidos AS p
						 LEFT JOIN platos_en_pedidos as d
                         ON
							p.idpedido = d.id_pedido
                         WHERE creador = ?
                         
                         GROUP BY 
							idpedido  
						 ORDER BY idpedido";

		try {
			// Preparar sentencia
			$comando = Database::getInstance() -> getDb() -> prepare($consulta);
			// Ejecutar sentencia preparada
			$comando -> execute(array($creador));

			return $comando -> fetchAll(PDO::FETCH_ASSOC);

		} catch (PDOException $e) {
			return false;
		}
	}
	
	public static function getAll_estados() {

		$consulta = "SELECT
        				 p.idpedido,
        				 p.fecha_hora,
        				 p.mesa,
        				 p.comensales,
        				 p.cobrado,
        				 p.creador,
        				 
                         SUM(IF (d.is_beb_o_prep = 0 AND d.estado = 2, 1, 0)) as p1,
                         SUM(IF (d.is_beb_o_prep = 0, 1, 0)) as p2,
                         SUM(IF (d.is_beb_o_prep = 0 AND d.estado = 1, 1, 0)) as p3,
                         
                         SUM(IF (d.is_beb_o_prep = 1 AND d.estado = 2, 1, 0)) as b1,
                         SUM(IF (d.is_beb_o_prep = 1, 1, 0)) as b2,
                         SUM(IF (d.is_beb_o_prep = 1 AND d.estado = 1, 1, 0)) as b3
                        
        				 FROM pedidos AS p
						 LEFT JOIN platos_en_pedidos as d
                         ON
							p.idpedido = d.id_pedido
                         
                         GROUP BY 
							idpedido  
						 ORDER BY idpedido";

		try {
			// Preparar sentencia
			$comando = Database::getInstance() -> getDb() -> prepare($consulta);
			// Ejecutar sentencia preparada
			$comando -> execute();

			return $comando -> fetchAll(PDO::FETCH_ASSOC);

		} catch (PDOException $e) {
			return false;
		}
	}

	public static function anadir_platos_en_pedido($idpedido, $platos) {

		//$rows = [(1,2,3), (4,5,6), (7,8,9) ... ];
		
		$aux_platos = $platos;

		$consulta = "INSERT INTO platos_en_pedidos
					(id_plat_ped, id_plato, id_pedido, cantidad, estado, precio, is_beb_o_prep)
					VALUES
					(NULL, :idplato, :idpedido, :cantidad, 0, NULL, NULL)";

		//$stmt = $db -> prepare($sql);
		
		$comando = Database::getInstance() -> getDb() -> prepare($consulta);

		foreach ($aux_platos as $plato) {
			
			//$stmt -> execute($row);
			//$consulta -> execute($row);
			// Ejecutar sentencia preparada
			$comando -> bindValue(':idplato', (int) trim($plato['idPlato']), PDO::PARAM_INT);
			$comando -> bindValue(':idpedido', (int) trim($idpedido), PDO::PARAM_INT);
			$comando -> bindValue(':cantidad', (int) trim($plato['cantidad']), PDO::PARAM_INT);

			$comando -> execute();
		}
		
		return "ok";

	}

	public static function crearPedido($creador, $comensales, $mesa) {

		$consulta = "INSERT INTO 
						pedidos 
						(idpedido, fecha_hora, mesa, comensales, notas, cobrado, creador)
						VALUES
						(NULL, NULL, :mesa, :comensales, NULL, NULL, :creador);";
		try {
			// Preparar sentencia
			$comando = Database::getInstance() -> getDb() -> prepare($consulta);
			// Ejecutar sentencia preparada
			$comando -> bindValue(':mesa', (int) trim($mesa), PDO::PARAM_INT);
			$comando -> bindValue(':comensales', (int) trim($comensales), PDO::PARAM_INT);
			$comando -> bindValue(':creador', $creador, PDO::PARAM_STR);

			return $comando -> execute();

		} catch (PDOException $e) {
			return false;
		}
	}

	public static function comprobacionCreado($creador, $comensales, $mesa) {
		$consulta = "SELECT
        				 idpedido
        				 FROM pedidos
        				 WHERE 
        				 creador = :creador AND mesa = :mesa AND comensales = :comensales";
		try {
			// Preparar sentencia
			$comando = Database::getInstance() -> getDb() -> prepare($consulta);
			// Ejecutar sentencia preparada
			$comando -> bindValue(':mesa', (int) trim($mesa), PDO::PARAM_INT);
			$comando -> bindValue(':comensales', (int) trim($comensales), PDO::PARAM_INT);
			$comando -> bindValue(':creador', $creador, PDO::PARAM_STR);

			$comando -> execute();
			return $comando -> fetchColumn();

		} catch (PDOException $e) {
			return false;
		}
	}

	public static function get_platos_pedido($id_pedido) {

		$consulta = "SELECT 		
						d.id_plat_ped,
						d.id_plato,
            			p.nombre as nombre_plato,
						d.id_pedido,
						d.cantidad,
						d.estado,
						d.precio,
            			p.precio as precio_plato,
						d.is_beb_o_prep 
            
            			FROM platos_en_pedidos as d
						INNER JOIN platos AS p
            			ON
						d.id_plato = p.idplato
            			WHERE d.id_pedido = :id_pedido ";

		//$aux = trim($id_pedido);

		//$consulta = str_replace("?", "$aux", $consultaa);

		try {
			// Preparar sentencia

			$comando = Database::getInstance() -> getDb() -> prepare($consulta);
			// Meter el valor como INT
			$comando -> bindValue(':id_pedido', (int) trim($id_pedido), PDO::PARAM_INT);
			// Ejecutar sentencia preparada
			$comando -> execute();

			return $comando -> fetchAll(PDO::FETCH_ASSOC);
		} catch (PDOException $e) {
			return false;
		}
	}
	
	public static function get_pedidos_activos(){
		
		$consulta = "SELECT
        				 COUNT(idpedido)
        				 FROM pedidos";
        				//WHERE 
        				//creador = :creador
		try {
			// Preparar sentencia
			$comando = Database::getInstance() -> getDb() -> prepare($consulta);
			// Ejecutar sentencia preparada
			$comando -> execute();
			return $comando -> fetchColumn();

		} catch (PDOException $e) {
			return false;
		}
		
	}
	
	public static function get_platos_pedido_sin_entregar($id_pedido) {

		$consulta = "SELECT 		
						d.id_plat_ped,
						d.id_plato,
            			p.nombre as nombre_plato,
						d.id_pedido,
						d.cantidad,
						d.estado,
						d.precio,
            			p.precio as precio_plato,
						d.is_beb_o_prep 
            
            			FROM platos_en_pedidos as d
						INNER JOIN platos AS p
            			ON
						d.id_plato = p.idplato
            			WHERE d.id_pedido = :id_pedido AND
            			d.estado != 2
						 ";

		//$aux = trim($id_pedido);

		//$consulta = str_replace("?", "$aux", $consultaa);

		try {
			// Preparar sentencia

			$comando = Database::getInstance() -> getDb() -> prepare($consulta);
			// Meter el valor como INT
			$comando -> bindValue(':id_pedido', (int) trim($id_pedido), PDO::PARAM_INT);
			// Ejecutar sentencia preparada
			$comando -> execute();

			return $comando -> fetchAll(PDO::FETCH_ASSOC);
		} catch (PDOException $e) {
			return false;
		}
	}
	
	public static function archivar_pedido($idpedido){
		
		$consulta = "INSERT INTO `archivo_pedidos`
							(`idpedido`, `fecha_hora`, `mesa`, `comensales`, `notas`, `cobrado`, `creador`)
					 SELECT * FROM `pedidos` WHERE pedidos.idpedido = :idpedido";

		try {
			// Preparar sentencia

			$comando = Database::getInstance() -> getDb() -> prepare($consulta);
			// Meter el valor como INT
			$comando -> bindValue(':idpedido', (int) trim($idpedido), PDO::PARAM_INT);
			// Ejecutar sentencia preparada
			//$comando -> execute();

			return $comando -> execute();
			//return $comando -> fetchAll(PDO::FETCH_ASSOC);
		} catch (PDOException $e) {
			return false;
		}
		
	}
	
	public static function borrar_plato_pedido($idplatoped){
		
		$consulta = "DELETE FROM platos_en_pedidos WHERE platos_en_pedidos.id_plat_ped = :idplatoped; ";

		//$aux = trim($id_pedido);

		//$consulta = str_replace("?", "$aux", $consultaa);

		try {
			// Preparar sentencia

			$comando = Database::getInstance() -> getDb() -> prepare($consulta);
			// Meter el valor como INT
			$comando -> bindValue(':idplatoped', (int) trim($idplatoped), PDO::PARAM_INT);
			// Ejecutar sentencia preparada
			//$comando -> execute();

			return $comando -> execute();
			//return $comando -> fetchAll(PDO::FETCH_ASSOC);
		} catch (PDOException $e) {
			return false;
		}
	}

}
?>