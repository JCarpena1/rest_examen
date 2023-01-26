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
        Object entity = null;
        
        var clientes = new ArrayList<Cliente>();

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {

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

            entity = clientes;
            
            // Thread.sleep(3000);

        } catch (SQLException /*| InterruptedException*/ ex) {
            responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR);
            entity = ex.getMessage();
            Logger.getLogger(ClientesResource.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(stmt);
            DB.closeConnection(conn);
        }

        return responseBuilder.entity(entity).build();

    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response show(@PathParam("id") int id) {

        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.OK);
        Object entity = null;

        Cliente cliente = null;

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

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

                entity = cliente;
            }

            if (entity == null) {
                responseBuilder.status(Response.Status.NOT_FOUND);
                entity = "not found";
            }
            
            // Thread.sleep(3000);

        } catch (SQLException /*| InterruptedException*/ ex) {
            responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR);
            entity = ex.getMessage();
            Logger.getLogger(ClientesResource.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(stmt);
            DB.closeConnection(conn);
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
                            DB.getInt(rsSel, "id"),
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

            // Thread.sleep(3000);
        } catch (SQLException /*| InterruptedException*/ ex) {
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
                            DB.getInt(rsSel, "id"),
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

            // Thread.sleep(3000);
        } catch (SQLException /*| InterruptedException*/ ex) {
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

            // Thread.sleep(3000);
        } catch (SQLException /*| InterruptedException*/ ex) {
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
