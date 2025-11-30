package com.example.constructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import com.example.model.Cliente;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "api/clientes")
public class controlerClientes {
	public ArrayList<Cliente> clientes= new ArrayList<>();
	
	
	@PostMapping
	public void guardarLibro(@Valid @RequestBody String _nombre, String _correo, String _contraseña, String _address) {
		for (Integer i=0; i< this.clientes.size() ; i++) {
			if(this.clientes.get(i).getAdress().equals(_address)) {
				System.out.println("Error: dirección ya registrada");
	            return;
			}
			if(this.clientes.get(i).getCorreo().equals(_correo)) {
				System.out.println("Error: Correo ya registrada");
	            return;
			}
		}
		Cliente newCliente = new Cliente();
		newCliente.setNombre(_nombre);
		newCliente.setCorreo(_correo);
		newCliente.setAdress(_address);
		newCliente.setContraseña(_contraseña);
		newCliente.setFechaRegistro(LocalDate.now());
		clientes.add(newCliente);
		System.out.print(newCliente);
	}
	
	@GetMapping()
	public ArrayList<Cliente> obtenerTodosLosClientes() {
		return clientes;
	}
	@GetMapping("/cliente/{adress}")
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
