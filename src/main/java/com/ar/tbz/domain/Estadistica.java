package com.ar.tbz.domain;

public class Estadistica {

	private int empleadosActivos;
	private int autodiagnosticosEmpleados;
	private int empleadosHabilitados;
	private int empleadosNoHabilitados;
	private int empleadosContactoEstrecho;
	private int empleadosSintomas;
	private int empleadosSintomasYContactoEstrecho;
	private double pctAutodiagnosticoSobreEmpleadosActivos;

	public Estadistica() {

	}

	public Estadistica(Integer empleadosActivos, Integer autodiag, Integer empHabilitados, Integer empNoHabilitados,
			Integer empEstrechos, Integer empSintomas, Integer empSintomasContEstrecho, double pctAutodiagPorEmpAct) {
		this.empleadosActivos = empleadosActivos;
		this.autodiagnosticosEmpleados = autodiag;
		this.empleadosHabilitados = empHabilitados;
		this.empleadosNoHabilitados = empNoHabilitados;
		this.empleadosContactoEstrecho = empEstrechos;
		this.empleadosSintomas = empSintomas;
		this.empleadosSintomasYContactoEstrecho = empSintomasContEstrecho;
		this.pctAutodiagnosticoSobreEmpleadosActivos = pctAutodiagPorEmpAct;
	}

	public int getEmpleadosActivos() {
		return empleadosActivos;
	}

	public void setEmpleadosActivos(int empleadosActivos) {
		this.empleadosActivos = empleadosActivos;
	}

	public int getAutodiagnosticosEmpleados() {
		return autodiagnosticosEmpleados;
	}

	public void setAutodiagnosticosEmpleados(int autodiagnosticosEmpleados) {
		this.autodiagnosticosEmpleados = autodiagnosticosEmpleados;
	}

	public int getEmpleadosHabilitados() {
		return empleadosHabilitados;
	}

	public void setEmpleadosHabilitados(int empleadosHabilitados) {
		this.empleadosHabilitados = empleadosHabilitados;
	}

	public int getEmpleadosNoHabilitados() {
		return empleadosNoHabilitados;
	}

	public void setEmpleadosNoHabilitados(int empleadosNoHabilitados) {
		this.empleadosNoHabilitados = empleadosNoHabilitados;
	}

	public int getEmpleadosContactoEstrecho() {
		return empleadosContactoEstrecho;
	}

	public void setEmpleadosContactoEstrecho(int empleadosContactoEstrecho) {
		this.empleadosContactoEstrecho = empleadosContactoEstrecho;
	}

	public int getEmpleadosSintomas() {
		return empleadosSintomas;
	}

	public void setEmpleadosSintomas(int empleadosSintomas) {
		this.empleadosSintomas = empleadosSintomas;
	}

	public int getEmpleadosSintomasYContactoEstrecho() {
		return empleadosSintomasYContactoEstrecho;
	}

	public void setEmpleadosSintomasYContactoEstrecho(int empleadosSintomasYContactoEstrecho) {
		this.empleadosSintomasYContactoEstrecho = empleadosSintomasYContactoEstrecho;
	}

	public double getPctAutodiagnosticoSobreEmpleadosActivos() {
		return pctAutodiagnosticoSobreEmpleadosActivos;
	}

	public void setPctAutodiagnosticoSobreEmpleadosActivos(double pctAutodiagnosticoSobreEmpleadosActivos) {
		this.pctAutodiagnosticoSobreEmpleadosActivos = pctAutodiagnosticoSobreEmpleadosActivos;
	}

}
