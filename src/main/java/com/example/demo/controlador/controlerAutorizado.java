package com.example.demo.controlador;
import com.example.demo.controlador.controlerClientes;
import com.example.demo.model.Autorizado;
import com.example.demo.model.Cliente;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.ArrayList;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/autorizados")
public class controlerAutorizado {
    public static ArrayList<Autorizado> autorizados = new ArrayList<>();
	public static ArrayList<Cliente> clientes= controlerClientes.clientes;
	
	
	@PostMapping
	public ResponseEntity guardarAutorizado(@Valid @RequestBody Autorizado newAutorizado) {
		
		for (Integer i=0; i < this.autorizados.size() ; i++) {
			if(autorizados.get(i).getNombre().equals(newAutorizado.getNombre()) || autorizados.get(i).getTelefono().equals(newAutorizado.getTelefono()) ) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: ese telefono o nombre ya esta en uso");
			}
		}
		
		for (Integer i=0; i< this.clientes.size() ; i++) {
			if(this.clientes.get(i).getAdress().equals(newAutorizado.getAdress())) {
				autorizados.add(newAutorizado);
				System.out.print(newAutorizado);
				return ResponseEntity.status(HttpStatus.OK).body("agregado autorizado");
			}

		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: dirección no existente");
	}
	
	@GetMapping("/autorizadoPor/{address}")
	public ResponseEntity<ArrayList<Autorizado>> verAutorizadoPorXCliente(@PathVariable String address) {
		ArrayList<Autorizado> verUtorizados=new ArrayList<>();
		for (Integer i=0; i < this.autorizados.size() ; i++) {
			if(autorizados.get(i).getAdress().equals(address)) {
				verUtorizados.add(autorizados.get(i));
			}
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(verUtorizados);
		
		
	}
	
	public static class modificarAutorizadoCLase{
		public @Valid Autorizado autorizadoModificar;
		@NotBlank
		@NotNull
		@Size(min = 2, max = 100)
		public String nombreNuevo;
		@NotBlank
		@NotNull
		@Pattern(regexp = "\\d{10}", message = "El teléfono debe tener 10 números")
		public String TelefonoNuevo;
	}
	
	@PutMapping
	public ResponseEntity modificarAutorizado(
			@Valid @RequestBody modificarAutorizadoCLase cosa) {
		
		for (Integer i=0; i < this.autorizados.size() ; i++) {
			if(autorizados.get(i).getAdress().equals(cosa.autorizadoModificar.getAdress()) && 
				autorizados.get(i).getNombre().equals(cosa.autorizadoModificar.getNombre()) && 
				autorizados.get(i).getTelefono().equals(cosa.autorizadoModificar.getTelefono())) 
			{
				
				autorizados.get(i).setNombre(cosa.nombreNuevo);
				autorizados.get(i).setTelefono(cosa.TelefonoNuevo);
				return ResponseEntity.status(HttpStatus.OK).body("Autorizado Modificado");
			}

		}
		System.out.println("Error: Autorizado no encontrado");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Autorizado no encontradoo");
		
	}
	
	@DeleteMapping
	public ResponseEntity eliminarAutorizado(@Valid @RequestBody Autorizado eliminaraAutorizado) {
		boolean eliminado = autorizados.removeIf(i ->
	    i.getNombre().trim().equals(eliminaraAutorizado.getNombre().trim()) &&
	    i.getTelefono().trim().equals(eliminaraAutorizado.getTelefono().trim())
	);

	if(eliminado) {
	    return ResponseEntity.status(HttpStatus.OK).body("Autorizado eliminado");
	} else {
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Autorizado no encontrado");
	}
	}
	
	@GetMapping
	public ArrayList<Autorizado> obtenerTodosLosAutorizados() {
		return autorizados;
	}
	
	
}
