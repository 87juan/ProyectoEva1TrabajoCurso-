package com.example.demo.model;

import com.example.demo.model.Repartidor.Estado;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Pedido {
	public enum EstadoPedido {pendiente, en_proceso, entregado}
	@JsonProperty("estado")
    private EstadoPedido estado;
	

	
	@NotBlank
	@NotNull
	@Size(min = 2, max = 100)
	private String descripcion;

	@NotBlank
	@NotNull
	@Pattern(
		        regexp = "^0x[a-fA-F0-9]{40}$",
		        message = "El address debe ser un address de Ethereum válido (0x + 40 caracteres hexadecimales)"
		    )
	private String direccion_destinatario;
	@NotBlank
	@NotNull
	private String direccion;


	private String correoRepartidor;
	public String getCorreoRepartidor() {
		return correoRepartidor;
	}
	public void setCorreoRepartidor(String correoRepartidor) {
		this.correoRepartidor = correoRepartidor;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getDireccion_destinatario() {
		return direccion_destinatario;
	}
	public void setDireccion_destinatario(String direccion_destinatario) {
		this.direccion_destinatario = direccion_destinatario;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public EstadoPedido getEstado() {
		return estado;
	}
	public void setEstado(EstadoPedido pendiente) {
		this.estado = pendiente;
	}
}
