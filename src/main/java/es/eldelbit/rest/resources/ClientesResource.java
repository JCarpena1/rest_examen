package es.eldelbit.rest.resources;

import es.eldelbit.rest.db.DB;
import es.eldelbit.rest.models.Cliente;
import es.eldelbit.rest.services.ClienteService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author
 */
@Path("clientes")
@RequestScoped
public class ClientesResource {

    @Inject
    private ClienteService clienteService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response index() {

        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.OK);
        Object entity = null;

        try {

            entity = clienteService.get();

            // Thread.sleep(3000);
        } catch (Exception /*| InterruptedException*/ ex) {
            responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR);
            entity = ex.getMessage();
            Logger.getLogger(ClientesResource.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

        }

        return responseBuilder.entity(entity).build();

    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response show(@PathParam("id") long id) {

        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.OK);
        Object entity = null;

        try {

            entity = clienteService.find(id);

            if (entity == null) {
                responseBuilder.status(Response.Status.NOT_FOUND);
                entity = "not found";
            }

            // Thread.sleep(3000);
        } catch (Exception /*| InterruptedException*/  ex) {
            responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR);
            entity = ex.getMessage();
            Logger.getLogger(ClientesResource.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

        }

        return responseBuilder.entity(entity).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response store(Cliente cliente) {

        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.OK);
        Object entity = null;

        Connection conn = null;

        PreparedStatement stmtIns = null;

        ResultSet rsKeys = null;
        int id = 0;

        PreparedStatement stmtSel = null;
        ResultSet rsSel = null;

        try {

            conn = DB.getConnection();

            conn.setAutoCommit(false);

            // insertar
            stmtIns = conn.prepareStatement("INSERT INTO clientes (nombre, edad, direccion, fecha_nacimiento) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

            stmtIns.setString(1, cliente.getNombre());
            stmtIns.setObject(2, cliente.getEdad());
            stmtIns.setString(3, cliente.getDireccion());
            stmtIns.setTimestamp(4, cliente.getFechaNacimiento());

            if (stmtIns.executeUpdate() == 1) {

                responseBuilder.status(Response.Status.CREATED);

                // obtener id
                rsKeys = stmtIns.getGeneratedKeys();
                while (rsKeys.next()) {
                    id = rsKeys.getInt(1);
                }
            }

            // obtener datos
            if (id > 0) {

                stmtSel = conn.prepareStatement("SELECT id, nombre, edad, direccion, fecha_nacimiento, created_at, updated_at FROM clientes WHERE id = ?");

                stmtSel.setInt(1, id);
                rsSel = stmtSel.executeQuery();

                if (rsSel.next()) {
                    cliente = new Cliente(
                            DB.getLong(rsSel, "id"),
                            rsSel.getString("nombre"),
                            DB.getInt(rsSel, "edad"),
                            rsSel.getString("direccion"),
                            rsSel.getTimestamp("fecha_nacimiento"),
                            rsSel.getTimestamp("created_at"),
                            rsSel.getTimestamp("updated_at")
                    );

                    entity = cliente;
                }
            }

            conn.commit();

            // Thread.sleep(3000);
        } catch (SQLException /*| InterruptedException*/ ex) {
            DB.rollback(conn);
            responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR);
            entity = ex.getMessage();
            Logger.getLogger(ClientesResource.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DB.closeResultSet(rsSel);
            DB.closeStatement(stmtSel);

            DB.closeResultSet(rsKeys);

            DB.closeStatement(stmtIns);
            DB.closeConnection(conn);
        }

        return responseBuilder.entity(entity).build();

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response update(@PathParam("id") int id, Cliente cliente) {

        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.OK);
        Object entity = null;

        Connection conn = null;

        PreparedStatement stmtIns = null;

        PreparedStatement stmtSel = null;
        ResultSet rsSel = null;

        try {

            conn = DB.getConnection();

            conn.setAutoCommit(false);

            // modificar
            stmtIns = conn.prepareStatement("UPDATE clientes SET nombre = ?, edad = ?, direccion = ?, fecha_nacimiento = ? WHERE id = ?");

            stmtIns.setString(1, cliente.getNombre());
            stmtIns.setObject(2, cliente.getEdad());
            stmtIns.setString(3, cliente.getDireccion());
            stmtIns.setTimestamp(4, cliente.getFechaNacimiento());
            stmtIns.setInt(5, id);

            if (stmtIns.executeUpdate() == 1) {

                // obtener datos
                stmtSel = conn.prepareStatement("SELECT id, nombre, edad, direccion, fecha_nacimiento, created_at, updated_at FROM clientes WHERE id = ?");

                stmtSel.setInt(1, id);
                rsSel = stmtSel.executeQuery();

                if (rsSel.next()) {
                    cliente = new Cliente(
                            DB.getLong(rsSel, "id"),
                            rsSel.getString("nombre"),
                            DB.getInt(rsSel, "edad"),
                            rsSel.getString("direccion"),
                            rsSel.getTimestamp("fecha_nacimiento"),
                            rsSel.getTimestamp("created_at"),
                            rsSel.getTimestamp("updated_at")
                    );

                    entity = cliente;
                }
            }

            if (entity == null) {
                responseBuilder.status(Response.Status.NOT_FOUND);
                entity = "not found";
            }

            conn.commit();

            // Thread.sleep(3000);
        } catch (SQLException /*| InterruptedException*/ ex) {
            DB.rollback(conn);
            responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR);
            entity = ex.getMessage();
            Logger.getLogger(ClientesResource.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DB.closeResultSet(rsSel);
            DB.closeStatement(stmtSel);

            DB.closeStatement(stmtIns);
            DB.closeConnection(conn);
        }

        return responseBuilder.entity(entity).build();
    }

    @DELETE
    @Path("/{id}")
    public Response destroy(@PathParam("id") int id) {

        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.OK);
        Object entity = null;

        Connection conn = null;

        PreparedStatement stmtDel = null;

        try {

            conn = DB.getConnection();

            conn.setAutoCommit(false);

            // borrar
            stmtDel = conn.prepareStatement("DELETE FROM clientes WHERE id = ?");

            stmtDel.setInt(1, id);

            if (stmtDel.executeUpdate() == 1) {
                responseBuilder.status(Response.Status.NO_CONTENT);
                entity = "";
            }

            if (entity == null) {
                responseBuilder.status(Response.Status.NOT_FOUND);
                entity = "not found";
            }

            conn.commit();

            // Thread.sleep(3000);
        } catch (SQLException /*| InterruptedException*/ ex) {
            DB.rollback(conn);
            responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR);
            entity = ex.getMessage();
            Logger.getLogger(ClientesResource.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DB.closeStatement(stmtDel);
            DB.closeConnection(conn);
        }

        return responseBuilder.entity(entity).build();

    }
}
