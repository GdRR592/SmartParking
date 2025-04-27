package controladores;

import controladores.excepciones.PlazaOcupada;
import controladores.excepciones.ReservaInvalida;
import controladores.excepciones.SolicitudReservaInvalida;
import list.ArrayList;
import list.IList;
import modelo.gestoresplazas.GestorLocalidad;
import modelo.reservas.EstadoValidez;
import modelo.reservas.Reserva;
import modelo.reservas.Reservas;
import modelo.reservas.solicitudesreservas.SolicitudReserva;
import modelo.reservas.solicitudesreservas.SolicitudReservaAnticipada;
import modelo.vehiculos.Vehiculo;
import anotacion.Programacion2;

@Programacion2 (
		nombreAutor1 = "",
		apellidoAutor1 = "",
		emailUPMAutor1 = ""
		)

public class ControladorReservas {
	private Reservas registroReservas;
	private GestorLocalidad gestorLocalidad;

	public GestorLocalidad getGestorLocalidad() {
		return gestorLocalidad;
	}

	public Reservas getRegistroReservas() {
		return registroReservas;
	}

	public boolean esValidaReserva(int i, int j, int numPlaza, int numReserva, String noMatricula) {
		Reserva reserva = this.registroReservas.obtenerReserva(numReserva);
		if (reserva == null)
			return false;
		reserva.validar(i, j, numPlaza, noMatricula, gestorLocalidad);
		return reserva.getEstadoValidez() == EstadoValidez.OK;
	}

	//TO-DO alumno obligatorio

	/**
	 * PRE: plazas y precios son matrices NxM (de igual dimensión)
	 * @param plazas Array con el número de plazas de cada gestor
	 * @param precios Array con los precios de cada gestor
	 */
	public ControladorReservas(int[][] plazas, double[][] precios) {
		this.registroReservas = new Reservas();
		this.gestorLocalidad = new GestorLocalidad(plazas, precios);
	}

	/**
	 * PRE: la solicitud es válida
	 * Gestiona y registra la solicitud de reserva dada. Si la solicitud es registrada correctamente, devuelve su número de reserva.
	 * En caso contrario, devuelve -1
	 * @param solicitud Solicitud de reserva dada
	 * @throws SolicitudReservaInvalida
	 */
	public int hacerReserva(SolicitudReserva solicitud) throws SolicitudReservaInvalida {
		if (!solicitud.esValida(gestorLocalidad)) {
			throw new SolicitudReservaInvalida("La solicitud de reserva no es válida. Para que sea válida, la zona tiene que existir"
					+ " en la localidad, el intervalo de tiempo tiene que ser coherente y el vehículo no puede estar sancionado.");
		}
		solicitud.gestionarSolicitudReserva(gestorLocalidad);
		if (solicitud.getHueco() != null) {
			return this.registroReservas.registrarReserva(solicitud);
		}
		else {
			return -1;
		}
	}
	/**
	 * PRE: existe la reserva asociada al número dado
	 * Devuelve la reserva asociada al número dado
	 * @param numReserva Número de reserva dado
	 */
	public Reserva getReserva(int numReserva) {
		return this.registroReservas.obtenerReserva(numReserva);
	}

	/**
	 * PRE: la plaza dada está libre y la reserva está validada
	 * Ocupa la plaza asociada a la reserva dada con el vehículo dado
	 * @param i Fila de la zona en la localidad
	 * @param j Columna de la zona en la localidad
	 * @param numPlaza Número de plaza dado
	 * @param numReserva Número de reserva dado
	 * @param vehiculo Vehículo que intenta ocupar la plaza
	 * @throws PlazaOcupada
	 * @throws ReservaInvalida
	 */
	public void ocuparPlaza(int i, int j, int numPlaza, int numReserva, Vehiculo vehiculo) 
			throws PlazaOcupada, ReservaInvalida {
		if (!this.esValidaReserva(i, j, numPlaza, numReserva, vehiculo.getMatricula())){
			throw new ReservaInvalida("La reserva no ha sido validada aún.");
		}
		Reserva reserva = this.registroReservas.obtenerReserva(numReserva);
		if (reserva.getHueco().getPlaza().getVehiculo() != null) {
			throw new PlazaOcupada ("La plaza ya está ocupada.");
		}
		reserva.validar(i, j, numPlaza, vehiculo.getMatricula(), gestorLocalidad);
		reserva.getHueco().getPlaza().setVehiculo(vehiculo);
	}


	//TO-DO alumno opcional

	//Libera el hueco en una reserva
	private void liberarHuecoReservado(int numReserva) {
		this.registroReservas.obtenerReserva(numReserva).liberarHuecoReservado();
	}

	/**
	 * Desocupa la plaza asociada a la reserva dada y libera el hueco asociado a la reserva
	 * @param numReserva Número de reserva dado
	 */
	public void desocuparPlaza(int numReserva) {
		this.registroReservas.obtenerReserva(numReserva).getHueco().getPlaza().setVehiculo(null);
		this.liberarHuecoReservado(numReserva);
	}

	/**
	 * Anula la reserva, liberando el hueco asociado a la reserva y borrando la reserva del registro de reservas
	 * @param numReserva
	 */
	public void anularReserva(int numReserva) {
		this.liberarHuecoReservado(numReserva);
		this.registroReservas.borrarReserva(numReserva);
	}


	// PRE (no es necesario comprobar): todas las solicitudes atendidas son válidas.
	public IList<Integer> getReservasRegistradasDesdeListaEspera(int i, int j) {
		IList<SolicitudReservaAnticipada> reservasRegistradasDesdeListaEspera = 
				this.gestorLocalidad.getSolicitudesAtendidasListaEspera(i, j);
		IList<Integer> reservasRegistradasDesdeListaEsperaNumReserva = new ArrayList<>();
		for (int k = 0; k < reservasRegistradasDesdeListaEspera.size(); k++) {
			SolicitudReservaAnticipada solicitud = reservasRegistradasDesdeListaEspera.get(k);

			System.out.println(solicitud + "0\n\n");

			reservasRegistradasDesdeListaEsperaNumReserva.add
			(reservasRegistradasDesdeListaEsperaNumReserva.size(),
					this.registroReservas.registrarReserva(solicitud));

			System.out.println(solicitud + "1\n\n");
			System.out.println(solicitud.getHueco() + "2\n\n");
		}
		return reservasRegistradasDesdeListaEsperaNumReserva;
	}
	//No sé si el failure se debe a un error en esta función o en la clase GestorZona
	//Creo que no se está inicializando el hueco de la solicitud, pero no sé por qué
} 
