package es.eldelbit.rest.resources;

import es.eldelbit.rest.models.Cliente;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author
 */
@Path("clientes")
public class ClientesResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Cliente> index() {

        var lista = new ArrayList<Cliente>();
        lista.add(new Cliente(1, "Nombre 1", 10));
        lista.add(new Cliente(2, "Nombre 2", 20));

        return lista;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Cliente show(@PathParam("id") int id) {

        return new Cliente(1, "Nombre 1", 10);

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Cliente store(Cliente cliente) {

        cliente.setId(1);
        var instant = Instant.now();
        cliente.setCreatedAt(instant);
        cliente.setUpdatedAt(instant);

        return cliente;

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Cliente update(@PathParam("id") int id, Cliente cliente) {
                
        var instant = Instant.now();        
        cliente.setUpdatedAt(instant);

        return cliente;
    }

    @DELETE
    @Path("/{id}")
    public Response destroy(@PathParam("id") int id) {
        
        return Response.ok("destroy " + id).build();
        
    }
}