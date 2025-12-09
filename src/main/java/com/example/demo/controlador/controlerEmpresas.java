package com.example.demo.controlador;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;

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
import com.example.demo.model.Repartidor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@RestController
@RequestMapping(value = "/api/empresa")
public class controlerEmpresas {
	public static  ArrayList<Empresa> empresas= new ArrayList<>();
	public static ArrayList<Cliente> clientes= controlerClientes.clientes;
	public static  ArrayList<Repartidor> repartidores= controlerRepartidor.repartidores;
	
	@PostMapping
	public ResponseEntity guardarCliente(@Valid @RequestBody  Empresa newEmpresa) {
		for (Integer i=0; i< this.empresas.size() ; i++) {
			if(this.empresas.get(i).getNombreEmpresa().equals(newEmpresa.getNombreEmpresa())) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Nombre ya registrado");
			}

		}
		empresas.add(newEmpresa);
		return ResponseEntity.status(HttpStatus.OK).body("Empresa agregada");
	}
	
	@GetMapping()
	public ArrayList<Empresa> obtenerTodosLasEmpresas() {
		return empresas;
	}
	
	@GetMapping("empresas/{adress}")
	public ResponseEntity<ArrayList<Empresa>> obtenerTodosLasEmpresas(@PathVariable String adress) {
		Cliente sujeto= new Cliente();
		sujeto.setAdress(adress);
		for(Cliente c:clientes) {
			if(c.getAdress().equals(sujeto.getAdress())) {
				return ResponseEntity.status(HttpStatus.OK).body(empresas);
			}
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>());
	}
	
	public static class modificarEmpresaRepartidoraClase {
		@Valid 
		public Empresa newEmpresas;
		public String nombreOriginalEmpresa;
	}
	
	@PutMapping("/modificar")
	public ResponseEntity modificarEmpresaRepartidora(
			@Valid @RequestBody modificarEmpresaRepartidoraClase contenido) {
		for(Empresa c : this.empresas) {
			if(c.getNombreEmpresa().equals(contenido.newEmpresas.getNombreEmpresa())) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Nombre ya registrada");
			}
		}
		
		for(Empresa c : this.empresas) {
			if(c.getNombreEmpresa().equals(contenido.nombreOriginalEmpresa)) {
				c.setCorreo(contenido.newEmpresas.getCorreo());
				c.setNombreEmpresa(contenido.newEmpresas.getNombreEmpresa());
				c.setTelefono(contenido.newEmpresas.getTelefono());
				return ResponseEntity.status(HttpStatus.OK).body("Empresa modificada");
			}
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Empresa no encontrada");
	}
	
	@DeleteMapping("/{nombreEmpresa}")
	public ResponseEntity eliminarEmpresa(
			@Valid @PathVariable @NotBlank
			@NotNull
			@Size(min = 2, max = 100) String nombreEmpresa) {
		if(this.repartidores!=null || repartidores.isEmpty()) {
			repartidores.removeIf(a -> a.getIdEmpresa().equals(nombreEmpresa));
		}
		
		boolean eliminado = empresas.removeIf(a -> a.getNombreEmpresa().equals(nombreEmpresa));
		
		if(eliminado) {
		    return ResponseEntity.status(HttpStatus.OK).body("Empresa eliminada");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(" Empresa no encontrada");
		}
		
		
	}

}
