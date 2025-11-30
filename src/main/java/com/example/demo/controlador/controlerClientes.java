package com.example.demo.controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Autorizado;
import com.example.demo.model.Cliente;

import java.time.LocalDate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping(value = "/api/clientes")
public class  controlerClientes {
	public static  ArrayList<Cliente> clientes= new ArrayList<>();
	public static ArrayList<Autorizado> autorizados=controlerAutorizado.autorizados;
	
	@PostMapping
	public void guardarCliente(@Valid @RequestBody  Cliente newCliente) {
		for (Integer i=0; i< this.clientes.size() ; i++) {
			if(this.clientes.get(i).getAdress().equals(newCliente.getAdress())) {
				System.out.println("Error: dirección ya registrada");
	            return;
			}
			if(this.clientes.get(i).getCorreo().equals(newCliente.getCorreo())) {
				System.out.println("Error: Correo ya registrada");
				
	            return;
			}
		}
		newCliente.setFechaRegistro(LocalDate.now());
		clientes.add(newCliente);
		System.out.print(newCliente);
	}
	
	@PutMapping
	public void modificarClienteCorreo(
			@Valid @RequestHeader String _address, 
			@RequestHeader String nombreNuevo, 
			@RequestHeader String correoNuevo,
			@RequestHeader String contraseñaNuevo) {
		
		for(Cliente c : this.clientes) {
			if(c.getCorreo().equals(correoNuevo)) {
				System.out.println("Error: Correo ya registrada");
	            return;
			}
			if(c.getAdress().equals(_address)) {
				if(nombreNuevo!=null || nombreNuevo!="") {
					c.setNombre(nombreNuevo);
				}
				if(correoNuevo!=null || correoNuevo!="") {
					c.setCorreo(correoNuevo);
				}
				if(contraseñaNuevo!=null || contraseñaNuevo!="") {
					c.setContraseña(contraseñaNuevo);
				}
				return;
			}
		}
		System.out.println("Error: Usuario no encontrado");
		return;
	}
	
	@DeleteMapping
	public void eliminarCliente(@Valid @RequestHeader String _address) {
		Cliente deletecliente =new Cliente();
		deletecliente.setAdress(_address);
		 for (int i = 0; i < this.clientes.size(); i++) {
		        if (this.clientes.get(i).getAdress().equals(deletecliente.getAdress())) {
		        	for (int c = 0; c < this.autorizados.size(); c++) {
						if(autorizados.get(c).getAdress().equals(_address)) {
							autorizados.remove(c);
						}
					}
		            this.clientes.remove(i);
		            return;
		        }
		    }

	}
	
	@GetMapping()
	public ArrayList<Cliente> obtenerTodosLosClientes() {
		return clientes;
	}
	
	@GetMapping("/{adress}")
	public Cliente obtenerClienteEspecifico(@PathVariable String adress) {
		for (Cliente c : this.clientes) {
	        if (c.getAdress().equals(adress)) {
	        	Cliente retornado =new Cliente();
	        	retornado.setCorreo(c.getCorreo());
	        	retornado.setFechaRegistro(c.getFechaRegistro());
	        	retornado.setNombre(c.getNombre());
	            return retornado;
	        }
	    }
		return null;
		
	}
}
