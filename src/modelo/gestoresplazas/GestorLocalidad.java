package modelo.gestoresplazas;

import list.ArrayList;
import list.IList;
import modelo.gestoresplazas.huecos.Hueco;
import modelo.reservas.solicitudesreservas.SolicitudReservaAnticipada;

//TO-DO alumno obligatorio

public class GestorLocalidad {
	private GestorZona[][] gestoresZonas;

	/**
	 * PRE: plazas y precios son matrices NxM (de igual dimensión)
	 * @param plazas Array con el número de plazas de cada gestor
	 * @param precios Array con los precios de cada gestor
	 */
	public GestorLocalidad(int[][] plazas, double[][] precios) {
		this.gestoresZonas = new GestorZona[plazas.length][plazas[0].length];
		for (int i = 0; i < plazas.length; i++) {
			for (int j = 0; j < plazas[i].length; j++) {
				this.gestoresZonas[i][j] = new GestorZona (i, j, plazas[i][j], precios[i][j]);
			}
		}
	}

	/**
	 * Devuelve N - 1 siendo N el máximo número de filas de la matriz de zonas
	 **/
	public int getRadioMaxI() {
		return this.gestoresZonas.length - 1;
	}

	/**
	 * Devuelve M - 1 siendo M el máximo número de columnas de la matriz de zonas
	 */
	public int getRadioMaxJ() {
		return this.gestoresZonas[0].length - 1;
	}

	/**
	 * Indica si las coordenadas de una zona dadas están en el rango válido de esta localidad
	 * @param i Fila de las coordenadas de la zona
	 * @param j Columna de las coordenadas de la zona
	 */
	public boolean existeZona(int i, int j) {
		return i >= 0 && j >= 0 && this.gestoresZonas.length > i && this.gestoresZonas[i].length > j;
	}

	public boolean existeHuecoReservado(Hueco hueco, int i, int j) {
		if (!existeZona(i, j)) {
			//LANZAR EXCEPCION
		}
		return this.gestoresZonas[i][j].existeHuecoReservado(hueco);
	}

	public GestorZona getGestorZona(int i, int j) {
		if (!existeZona(i, j)) {
			//LANZAR EXCEPCION
		}
		return this.gestoresZonas[i][j];
	}

	//TO-DO alumno opcional
	
	public IList<SolicitudReservaAnticipada> getSolicitudesAtendidasListaEspera(int i, int j) {
		return this.gestoresZonas[i][j].getSolicitudesAtendidasListaEspera();
	}
	 
}