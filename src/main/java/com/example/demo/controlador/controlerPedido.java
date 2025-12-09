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

import com.example.demo.model.Autorizado;
import com.example.demo.model.Cliente;
import com.example.demo.model.Empresa;
import com.example.demo.model.Pedido;
import com.example.demo.model.Pedido.EstadoPedido;
import com.example.demo.model.Repartidor;
import com.example.demo.model.Repartidor.Estado;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@RestController
@RequestMapping(value = "/api/pedidos")
public class controlerPedido {
	public static ArrayList<Pedido> pedidos=new ArrayList<>();
	public static ArrayList<Cliente> clientes= controlerClientes.clientes;
	public static ArrayList<Autorizado> autorizados=controlerAutorizado.autorizados;
	public static  ArrayList<Empresa> empresas= controlerEmpresas.empresas;
	public static  ArrayList<Repartidor> repartidores= controlerRepartidor.repartidores;
	
	
	@PostMapping
	public ResponseEntity guardarCliente(@Valid @RequestBody  Pedido newPedido) {
		Boolean existeAutorizado=false;
		for (Integer i=0; i< this.clientes.size() ; i++) {
			if(this.clientes.get(i).getAdress().equals(newPedido.getDireccion_destinatario())) {
					existeAutorizado=true;
			}
		}
		for (Integer i=0; i< this.autorizados.size() ; i++) {
			if(this.autorizados.get(i).getAdress().equals(newPedido.getDireccion_destinatario())) {
					existeAutorizado=true;
			}
		}
		
		
		if(existeAutorizado) {
			newPedido.setEstado(EstadoPedido.pendiente);
			pedidos.add(newPedido);
			return ResponseEntity.status(HttpStatus.OK).body("nuevo pedido");
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No hay autorizacion");
		}

	}
	
	public static class AsignarPedidoRepartidor {
	    public Pedido pedido;
	    @Valid 	
	    @NotNull 
	    @Size(min = 5, max = 254) 
	    @Email(message = "El correo no tiene un formato válido")
	    public String correoRepartidor;
	    public String nombreEmpresa;
	}
	
	
	@PutMapping("/repartidor")
	public ResponseEntity asignarPedidoARepartidor(@RequestBody  AsignarPedidoRepartidor contenido) {
		
		boolean empresa=false;
		boolean repartidor=false;
		for(Integer i=0; i< this.empresas.size() ; i++) {
			if(empresas.get(i).getNombreEmpresa().equals(contenido.nombreEmpresa)) {
				empresa=true;	
			}
		}
		
		
		if (!empresa) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Emppresa no encontrada");
		}
		
		
		for(Integer c=0;c< this.repartidores.size() ; c++) {
			if(repartidores.get(c).getEstado()==Estado.activo 
					&& repartidores.get(c).getIdEmpresa().equals(contenido.nombreEmpresa) 
					&&  repartidores.get(c).getCorreo().equals(contenido.correoRepartidor) ) {
				repartidor=true;
				
			}else if (repartidores.get(c).getEstado()!=Estado.activo 
					&& repartidores.get(c).getIdEmpresa().equals(contenido.nombreEmpresa)
					&&  repartidores.get(c).getCorreo().equals(contenido.correoRepartidor)) {
				return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Este repartidor no esta activo");
			}
		}
		
		
		if (!repartidor) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Repartidor no encontrada");
		}
		
		
		for(Integer e=0; e< this.pedidos.size() ; e++) {
			if(pedidos.get(e).getDescripcion().equals(contenido.pedido.getDescripcion()) && 
				pedidos.get(e).getDireccion().equals(contenido.pedido.getDireccion()) && 
				pedidos.get(e).getDireccion_destinatario().equals(contenido.pedido.getDireccion_destinatario())) 
			{
				pedidos.get(e).setCorreoRepartidor((contenido.correoRepartidor));
				return ResponseEntity.status(HttpStatus.OK).body(pedidos.get(e));
				
			}
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido no encontrada");
	}
	
	
	
	@GetMapping("cliente/{idCliente}")
	public ResponseEntity<ArrayList<Pedido>> obtenerTodosRepartidoresDeEmpresa(@PathVariable String idCliente) {
		 ArrayList<Pedido> miPedidos= new ArrayList();
		 for(int i=0; i<clientes.size();i++) {
			 if(clientes.get(i).getAdress().equals(idCliente)) {
				 for(Pedido c:pedidos) {
						if(c.getDireccion_destinatario().equals(idCliente)) {
							miPedidos.add(c);
						}
					}
				 return ResponseEntity.ok(miPedidos); 
			 }
		 }

		 return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>());
		 
	}
	
	public static class asignarEstadoPedidoPorRepartidorclase{
	    public Pedido pedidoQueQueremos;
	    @Valid 	
	    @NotNull 
	    @Size(min = 5, max = 254) 
	    @Email(message = "El correo no tiene un formato válido")
	    public String correoRepartidor;
	    public EstadoPedido estadoPedido;
	}
	
	
	@PutMapping("/estado")
	public ResponseEntity asignarEstadoPedidoPorRepartidor(@Valid @RequestBody asignarEstadoPedidoPorRepartidorclase contenido) {
		boolean hayRepartidor=false;
		for(Integer i=0; i< this.repartidores.size() ; i++) {
			if(repartidores.get(i).getCorreo().equals(contenido.correoRepartidor)) {
				hayRepartidor=true;
			}
		}
		if(!hayRepartidor) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Direccion de Repartidor no encontrado");
		}
		
		for (Pedido p : pedidos) {
	        if (p.getDescripcion().equals(contenido.pedidoQueQueremos.getDescripcion()) &&
	            p.getDireccion().equals(contenido.pedidoQueQueremos.getDireccion()) &&
	            p.getDireccion_destinatario().equals(contenido.pedidoQueQueremos.getDireccion_destinatario())) {
	            p.setEstado(contenido.estadoPedido);
	            return ResponseEntity.ok("Modificado el estado");
	        }
	    }

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido no encontrado");
		
	}
	
	
	public static class eliminarPedidoClase {
	    public Pedido pedidoEliminar;
	    @Valid
		@NotBlank
		@NotNull
		 @Pattern(
			        regexp = "^0x[a-fA-F0-9]{40}$",
			        message = "El address debe ser un address de Ethereum válido (0x + 40 caracteres hexadecimales)"
			    )
	    public String adreesCliente;
	}
	
	
	@DeleteMapping
	public ResponseEntity eliminarPedido( @Valid @RequestBody eliminarPedidoClase contenido) {
		boolean hayCliente=false;
		for (Integer i=0; i< this.clientes.size() ; i++) {
			if(this.clientes.get(i).getAdress().equals(contenido.adreesCliente)) {
				hayCliente=true;
			}
		}
		
		if(hayCliente) {
			boolean eliminado = pedidos.removeIf(a -> 
			a.getDescripcion().equals(contenido.pedidoEliminar.getDescripcion()) &&
			a.getDireccion_destinatario().equals(contenido.pedidoEliminar.getDireccion_destinatario()) &&
			a.getDireccion().equals(contenido.pedidoEliminar.getDireccion())
			&& a.getEstado().equals(EstadoPedido.pendiente));
			
			if(eliminado) {
			    return ResponseEntity.status(HttpStatus.OK).body("Empresa pedidos");
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Pedido no encontrado o el estado no estaba pendiente");
			}
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Cliente no encontrado");
		
	}
	
	@GetMapping()
	public ArrayList<Pedido> obtenerTodosLosPedidos() {
		return pedidos;
	}

      
	
}
