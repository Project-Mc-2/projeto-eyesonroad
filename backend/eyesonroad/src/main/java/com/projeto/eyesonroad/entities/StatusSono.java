package com.projeto.eyesonroad.entities;

public class StatusSono {

	private Long idStatusSono;
	private String cochilo;
	private String sono;
	private String desmaio;
	
	public StatusSono() {}
	public StatusSono(String cochilo, String sono, String desmaio) {
		this.cochilo = cochilo;
		this.sono = sono;
		this.desmaio = desmaio;
	}
	public String getCochilo() {
		return cochilo;
	}
	public void setCochilo(String cochilo) {
		this.cochilo = cochilo;
	}
	public String getSono() {
		return sono;
	}
	public void setSono(String sono) {
		this.sono = sono;
	}
	public String getDesmaio() {
		return desmaio;
	}
	public void setDesmaio(String desmaio) {
		this.desmaio = desmaio;
	}
	public Long getIdStatusSono() {
		return idStatusSono;
	}
	public void setIdStatusSono(Long idStatusSono) {
		this.idStatusSono = idStatusSono;
	}
	
	
}
