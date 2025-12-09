package com.example.demo.controlador;

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
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Cliente;
import com.example.demo.model.Empresa;
import com.example.demo.model.Pedido;
import com.example.demo.model.Repartidor;
import com.example.demo.model.Repartidor.Estado;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
@RestController
@RequestMapping(value = "/api/repartidor")
public class controlerRepartidor {
	public static  ArrayList<Repartidor> repartidores= new ArrayList<>();
	public static  ArrayList<Empresa> empresas= controlerEmpresas.empresas;
	
	
	@PostMapping
	public ResponseEntity guardarCliente(@Valid @RequestBody  Repartidor newRepartidor) {
		for (Integer i=0; i< this.empresas.size() ; i++) {
			if(this.empresas.get(i).getNombreEmpresa().equals(newRepartidor.getIdEmpresa())) {
				for (Integer c=0; c< this.repartidores.size() ;c++) {
					if(repartidores.get(c).getCorreo().contentEquals(newRepartidor.getCorreo())) {
						System.out.println("Error: Correo ya en uso en un repartidor");
						return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Correo ya en uso en un repartidor");
					}
				}
				newRepartidor.setEstado(Estado.activo);
				repartidores.add(newRepartidor);
				return ResponseEntity.status(HttpStatus.OK).body("Nuevo repartido creado");
			}

		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Empresa no existente para asociar al repartidor");
	}
	
	
	@GetMapping("repartidores/{idEmpresa}")
	public  ResponseEntity<ArrayList<Repartidor>> obtenerTodosRepartidoresDeEmpresa(@PathVariable String idEmpresa) {
		 ArrayList<Repartidor> miRepartidores= new ArrayList();
		 Repartidor sujeto= new Repartidor();
		for(Repartidor c:repartidores) {
			if(c.getIdEmpresa().equals(idEmpresa)) {
				sujeto.setCorreo(c.getCorreo());
				sujeto.setNombre(c.getNombre());
				sujeto.setEstado(c.getEstado());
				miRepartidores.add(c);
			}
		}

		 return ResponseEntity.status(HttpStatus.OK).body(miRepartidores);
	}
	
	
	public static class modificarRepartidorClase {
		@NotBlank
		@NotNull
		@Size(min = 2, max = 100)
		public String _nombreNuevo;
		@NotBlank
		@NotNull
		@Size(min = 5, max = 254)
		@Email(message = "El correo no tiene un formato válido")
		public String _correoNuevo;
		@NotBlank
		@NotNull
		@Pattern(regexp = "\\d{10}", message = "El teléfono debe tener 10 números")
		public String _telefonoNuevo;
		@NotBlank
		@NotNull
		@Size(min = 5, max = 254)
		@Email(message = "El correo no tiene un formato válido")
		public String _correoUsuario;
	}
	
	
	@PutMapping("/modificar")
	public ResponseEntity modificarRepartidor(@Valid  @RequestBody modificarRepartidorClase contenido) {
		for(Repartidor c : this.repartidores) {
			if(c.getCorreo().equals(contenido._correoNuevo)) {
				System.out.println("Error: Correo ya registrada");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Correo ya registrada");
			}
		}
		
		for(Integer i=0; i< this.repartidores.size() ; i++){
			if(repartidores.get(i).getCorreo().equals(contenido._correoUsuario)) {
				repartidores.get(i).setCorreo(contenido._correoNuevo);
				repartidores.get(i).setNombre(contenido._nombreNuevo);
				repartidores.get(i).setTelefono(contenido._telefonoNuevo);
				return ResponseEntity.status(HttpStatus.OK).body(repartidores.get(i));
			}
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: repartidor no encontrado");
	}
	

	
	@DeleteMapping("/{correoRepartidor}")
	public ResponseEntity eliminarRepartidor(@PathVariable 
			@NotBlank
			@NotNull
			@Size(min = 5, max = 254)
			@Email(message = "El correo no tiene un formato válido")
			String correoRepartidor) {
		
		boolean eliminado = repartidores.removeIf(a -> a.getCorreo().equals(correoRepartidor));
		
		if(eliminado) {
		    return ResponseEntity.status(HttpStatus.OK).body("Repartidor eliminada");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(" repartidor no encontrada");
		}
	}
	
	@GetMapping()
	public ArrayList<Repartidor> obtenerTodosLosRepartidores() {
		return repartidores;
	}

}
