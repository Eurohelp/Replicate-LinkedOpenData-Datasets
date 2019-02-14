package eus.eurohelp.blazegraphvalidators;

/**
 * Clase para comprobar si existe el namespace, pasado por parametro, en el blazegraph
 * Para la ejecucion de esta clase hay que pasar dos parametros
 * 1- <nombre del namespace> ejemplo : eurohelp
 * 2- <url del blazegraph> ejemplo: http://prueba.test.es:8080/blazegraph
 * @author eurohelp
 *
 */
public class BlazeGraphValidator {
	// En primer se pasa el nombre del namespaces a comprobar y si no existe lo crea
	// En segundo se pasa en nombre la url de blaze ejemplo(http://blzg-write:8080/blazegraph)

	public static void main(String[] args) throws Exception {
		Namespace namespace = new Namespace();
		namespace.comprobarCrear(args[0], args[1]);
	}
}