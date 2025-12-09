package com.example.demo.controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@RestController
@RequestMapping(value = "/api/clientes")
public class  controlerClientes {
	public static  ArrayList<Cliente> clientes= new ArrayList<>();
	public static ArrayList<Autorizado> autorizados = controlerAutorizado.autorizados;

	
	@PostMapping
	public ResponseEntity guardarCliente(@Valid @RequestBody  Cliente newCliente) {
		for (Integer i=0; i< this.clientes.size() ; i++) {
			if(this.clientes.get(i).getAdress().equals(newCliente.getAdress())) {
				System.out.println("Error: dirección ya registrada");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: dirección ya registrada");
			}
			if(this.clientes.get(i).getCorreo().equals(newCliente.getCorreo())) {
				System.out.println("Error: Correo ya registrada");
				
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Correo ya registrada");
			}
		}
		newCliente.setFechaRegistro(LocalDate.now());
		clientes.add(newCliente);
		return ResponseEntity.status(HttpStatus.OK).body("nuevo cliente");
	}
	
	public static class modificarClienteCorreoClase {
		@NotBlank
		@NotNull
		 @Pattern(
			        regexp = "^0x[a-fA-F0-9]{40}$",
			        message = "El address debe ser un address de Ethereum válido (0x + 40 caracteres hexadecimales)"
			    )
		public String _address;
		@NotBlank
		@NotNull
		@Size(min = 2, max = 100)
		public String nombreNuevo;
		@NotBlank
		@NotNull
		@Size(min = 5, max = 254)
		@Email(message = "El correo no tiene un formato válido")
		public String correoNuevo;
		@NotBlank
		@NotNull
		@Pattern(
		        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.#_-])[A-Za-z\\d@$!%*?&.#_-]{8,}$",
		        message = "La contraseña debe tener mínimo 8 caracteres, una mayúscula, una minúscula, un número y un caracter especial"
		    )
		@Size(min = 8, max = 50)
		public String contraseñaNuevo;
	}
	
	@PutMapping
	public ResponseEntity modificarClienteCorreo(
			@Valid @RequestBody modificarClienteCorreoClase contenido) {
		
		for(Cliente c : this.clientes) {
			if(c.getCorreo().equals(contenido.correoNuevo)) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Correo ya registrada");		
			}
			if(c.getAdress().equals(contenido._address)) {
				c.setNombre(contenido.nombreNuevo);
				c.setCorreo(contenido.correoNuevo);
				c.setContraseña(contenido.contraseñaNuevo);
				return ResponseEntity.status(HttpStatus.OK).body("Modificacion efectuada");			}
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Usuario no encontrado");
	}
	
	@DeleteMapping("/{_address}")
	public ResponseEntity eliminarCliente(@Valid @PathVariable @NotBlank @NotNull
			 @Pattern(
				        regexp = "^0x[a-fA-F0-9]{40}$",
				        message = "El address debe ser un address de Ethereum válido (0x + 40 caracteres hexadecimales)"
				    )
			String _address) {

		 for (int i = 0; i < this.clientes.size(); i++) {
		        if (this.clientes.get(i).getAdress().equals(_address)) {
		        	autorizados.removeIf(a -> a.getAdress().equals(_address));
		            this.clientes.remove(i);
		            return ResponseEntity.status(HttpStatus.OK).body("Cliente eliminado con sus autorizados");
		        }
		    }
		 return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Usuario no encontrado");

	}
	
	@GetMapping()
	public ArrayList<Cliente> obtenerTodosLosClientes() {
		return clientes;
	}
	
	@GetMapping("cliente/{adress}")
	public ResponseEntity<Cliente> obtenerClienteEspecifico(@PathVariable String adress) {
		for (Cliente c : this.clientes) {
	        if (c.getAdress().equals(adress)) {
	        	Cliente retornado =new Cliente();
	        	retornado.setCorreo(c.getCorreo());
	        	retornado.setFechaRegistro(c.getFechaRegistro());
	        	retornado.setNombre(c.getNombre());
	        	return ResponseEntity.status(HttpStatus.OK).body(retornado);
	        }
	    }
		return null;
		
	}
}
