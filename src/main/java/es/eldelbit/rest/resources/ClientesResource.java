package es.eldelbit.rest.resources;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

/**
 *
 * @author
 */
@Path("clientes")
public class ClientesResource {

    @GET
    public Response index() {
        return Response.ok("index").build();
    }

    @GET
    @Path("/{id}")
    public Response show(@PathParam("id") int id) {
        return Response.ok("show " + id).build();
    }

    @POST
    public Response store() {
        return Response.ok("post").build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") int id) {
        return Response.ok("update " + id).build();
    }

    @DELETE
    @Path("/{id}")
    public Response destroy(@PathParam("id") int id) {
        return Response.ok("destroy " + id).build();
    }
}
