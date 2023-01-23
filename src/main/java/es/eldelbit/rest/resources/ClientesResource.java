package es.eldelbit.rest.resources;

import es.eldelbit.rest.db.DB;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author
 */
@Path("clientes")
public class ClientesResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response index() {

        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.OK);

        var clientes = new ArrayList<Cliente>();

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {

            DB.registerDriver();
            conn = DB.getConnection();
            stmt = conn.createStatement();

            String sql = "SELECT id, nombre, edad, direccion, fecha_nacimiento, created_at, updated_at FROM clientes";
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                var cliente = new Cliente(
                        DB.getInt(rs, "id"),
                        rs.getString("nombre"),
                        DB.getInt(rs, "edad"),
                        rs.getString("direccion"),
                        rs.getTimestamp("fecha_nacimiento"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );

                clientes.add(cliente);
            }

            Thread.sleep(3000);

        } catch (ClassNotFoundException | SQLException | InterruptedException ex) {
            responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR);
            Logger.getLogger(ClientesResource.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(stmt);
            DB.closeConnection(conn);
        }

        return responseBuilder.entity(clientes).build();

    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response show(@PathParam("id") int id) {

        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.OK);

        Cliente cliente = null;

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            DB.registerDriver();
            conn = DB.getConnection();
            stmt = conn.prepareStatement("SELECT id, nombre, edad, direccion, fecha_nacimiento, created_at, updated_at FROM clientes WHERE id = ?");

            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                cliente = new Cliente(
                        DB.getInt(rs, "id"),
                        rs.getString("nombre"),
                        DB.getInt(rs, "edad"),
                        rs.getString("direccion"),
                        rs.getTimestamp("fecha_nacimiento"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );

            }

            Thread.sleep(3000);

        } catch (ClassNotFoundException | SQLException | InterruptedException ex) {
            responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR);      
            cliente = new Cliente();
            Logger.getLogger(ClientesResource.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(stmt);
            DB.closeConnection(conn);
        }

        return responseBuilder.entity(cliente).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Cliente store(Cliente cliente) {

        cliente.setId(1);
        // var instant = Instant.now();
        // cliente.setCreatedAt(instant);
        // cliente.setUpdatedAt(instant);

        return cliente;

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Cliente update(@PathParam("id") int id, Cliente cliente) {

        //var instant = Instant.now();        
        //cliente.setUpdatedAt(instant);
        return cliente;
    }

    @DELETE
    @Path("/{id}")
    public Response destroy(@PathParam("id") int id) {

        return Response.ok("destroy " + id).build();

    }
}
