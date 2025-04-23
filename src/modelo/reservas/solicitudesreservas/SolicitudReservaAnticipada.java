package modelo.reservas.solicitudesreservas;

import java.time.LocalDateTime;

import modelo.gestoresplazas.GestorLocalidad;
import modelo.gestoresplazas.GestorZona;
import modelo.gestoresplazas.huecos.Hueco;
import modelo.vehiculos.Vehiculo;


public class SolicitudReservaAnticipada extends SolicitudReserva {

	public SolicitudReservaAnticipada(int i, int j, LocalDateTime tI, 
			LocalDateTime tF, Vehiculo vehiculo) {
		super(i, j, tI, tF, vehiculo);
	}
	
	/**
	 * Si el hueco sigue valiendo null, añade la solicitud a la lista de espera del gestor
	 * de localidad dado
	 * @param gestor Gestor de localidad dado
	 */
	public void gestionarSolicitudReserva(GestorLocalidad gestor) {
		super.gestionarSolicitudReserva(gestor);
		if(this.getHueco() == null) {
			gestor.getGestorZona(this.getIZona(), this.getJZona()).meterEnListaEspera(this);
		}
		
	}
	
	
	
}
