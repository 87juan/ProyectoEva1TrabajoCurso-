package com.example.demo.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Repartidor {
	@NotBlank
	@NotNull
	@Size(min = 2, max = 100)
	private String nombre;
	@NotBlank
	@NotNull
	@Pattern(regexp = "\\d{10}", message = "El teléfono debe tener 10 números")
	private String telefono;
	@NotBlank
	@NotNull
	@Size(min = 5, max = 254)
	@Email(message = "El correo no tiene un formato válido")
	private String correo;
	@NotBlank
	@NotNull
	private String idEmpresa;

	public enum Estado {activo, inactivo}
	

    private Estado estado;
	
	public String getIdEmpresa() {
		return idEmpresa;
	}
	public void setIdEmpresa(String idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombreRepartidor) {
		this.nombre = nombreRepartidor;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	
	
	public Estado  getEstado() {
		return estado;
	}

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

}
