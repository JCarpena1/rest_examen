/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.eldelbit.rest.resources;

import es.eldelbit.rest.db.DB;
import es.eldelbit.rest.models.Cliente;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author virtualbox
 */
@Path("varios")
public class Varios {

    @GET
    @Path("call_procedure/{search}")
    public Response callProcedure(@PathParam("search") String search) {

        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.OK);
        Object entity = null;

        Integer total;

        Connection conn = null;
        CallableStatement stmt = null;

        try {

            conn = DB.getConnection();

            stmt = conn.prepareCall("{CALL ContarClientesP(?, ?)}");

            stmt.setString(1, search);

            stmt.registerOutParameter(2, Types.NUMERIC);

            stmt.executeUpdate();

            total = stmt.getInt(2);

            entity = total;

        } catch (SQLException ex) {
            responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR);
            entity = ex.getMessage();
            Logger.getLogger(Varios.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DB.closeStatement(stmt);
            DB.closeConnection(conn);
        }

        return responseBuilder.entity(entity).build();

    }

    @GET
    @Path("call_function/{search}")
    public Response callFunction(@PathParam("search") String search) {

        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.OK);
        Object entity = null;

        Integer total;

        Connection conn = null;
        CallableStatement stmt = null;

        try {

            conn = DB.getConnection();

            stmt = conn.prepareCall("{? = CALL ContarClientesF(?)}");

            stmt.setString(2, search);

            stmt.registerOutParameter(1, Types.NUMERIC);

            stmt.execute();

            total = stmt.getInt(1);

            entity = total;

        } catch (SQLException ex) {
            responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR);
            entity = ex.getMessage();
            Logger.getLogger(Varios.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DB.closeStatement(stmt);
            DB.closeConnection(conn);
        }

        return responseBuilder.entity(entity).build();

    }

    private File getResourceFile(final String fileName) {
        URL url = this.getClass()
                .getClassLoader()
                .getResource(fileName);

        if (url == null) {
            throw new IllegalArgumentException(fileName + " is not found 1");
        }

        File file = new File(url.getFile());

        return file;
    }

    @GET
    @Path("script")
    public Response script() {

        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.OK);
        Object entity = null;

        Connection conn = null;
        Statement stmt = null;

        try {

            // PARA PROBAR ESTO HAY QUE HACERLO DESPLEGANDO EL WAR EN EL SERVIDOR
            File file = getResourceFile("esquema.sql");
            String sql = new String(Files.readAllBytes(file.toPath()));

            conn = DB.getConnection();
            stmt = conn.createStatement();

            stmt.executeUpdate(sql);

            entity = "";

        } catch (SQLException | IOException ex) {
            responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR);
            entity = ex.getMessage();
            Logger.getLogger(Varios.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DB.closeStatement(stmt);
            DB.closeConnection(conn);
        }

        return responseBuilder.entity(entity).build();

    }

    @GET
    @Path("resultset_actualizable")
    @Produces(MediaType.APPLICATION_JSON)
    public Response resultSetActualizable() {

        Response.ResponseBuilder responseBuilder = Response.status(Response.Status.OK);
        Object entity = null;

        var clientes = new ArrayList<Cliente>();

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {

            conn = DB.getConnection();

            // Muestra en el log qué opciones están soportadas
            var dbmd = conn.getMetaData();

            Logger.getLogger("resultSetActualizable").log(Level.INFO, "TYPE_FORWARD_ONLY: {0}", dbmd.supportsResultSetType(ResultSet.TYPE_FORWARD_ONLY));
            Logger.getLogger("resultSetActualizable").log(Level.INFO, "TYPE_FORWARD_ONLY+CONCUR_READ_ONLY: {0}", dbmd.supportsResultSetConcurrency(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY));
            Logger.getLogger("resultSetActualizable").log(Level.INFO, "TYPE_FORWARD_ONLY+CONCUR_UPDATABLE: {0}", dbmd.supportsResultSetConcurrency(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE));

            Logger.getLogger("resultSetActualizable").log(Level.INFO, "TYPE_SCROLL_INSENSITIVE: {0}", dbmd.supportsResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE));
            Logger.getLogger("resultSetActualizable").log(Level.INFO, "TYPE_SCROLL_INSENSITIVE+CONCUR_READ_ONLY: {0}", dbmd.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY));
            Logger.getLogger("resultSetActualizable").log(Level.INFO, "TYPE_SCROLL_INSENSITIVE+CONCUR_UPDATABLE: {0}", dbmd.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE));

            Logger.getLogger("resultSetActualizable").log(Level.INFO, "TYPE_SCROLL_SENSITIVE: {0}", dbmd.supportsResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE));
            Logger.getLogger("resultSetActualizable").log(Level.INFO, "TYPE_SCROLL_SENSITIVE+CONCUR_READ_ONLY: {0}", dbmd.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY));
            Logger.getLogger("resultSetActualizable").log(Level.INFO, "TYPE_SCROLL_SENSITIVE+CONCUR_UPDATABLE: {0}", dbmd.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE));

            Logger.getLogger("resultSetActualizable").log(Level.INFO, "HOLD_CURSORS_OVER_COMMIT: {0}", dbmd.supportsResultSetHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT));
            Logger.getLogger("resultSetActualizable").log(Level.INFO, "CLOSE_CURSORS_AT_COMMIT: {0}", dbmd.supportsResultSetHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT));

            // ResultSet.TYPE_FORWARD_ONLY
            // ResultSet.TYPE_SCROLL_INSENSITIVE
            // ResultSet.TYPE_SCROLL_SENSITIVE
            int resultSetType = ResultSet.TYPE_SCROLL_INSENSITIVE;

            // ResultSet.CONCUR_READ_ONLY
            // ResultSet.CONCUR_UPDATABLE
            int resultSetConcurrency = ResultSet.CONCUR_UPDATABLE;

            // ResultSet.HOLD_CURSORS_OVER_COMMIT
            // ResultSet.CLOSE_CURSORS_AT_COMMIT
            int resultSetHoldability = ResultSet.HOLD_CURSORS_OVER_COMMIT;

            stmt = conn.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);

            String sql = "SELECT id, nombre, edad, direccion, fecha_nacimiento, created_at, updated_at FROM clientes";
            rs = stmt.executeQuery(sql);

            // mover el cursor
            rs.next(); // ir al siguiente
            rs.previous(); // ir al anterior
            rs.first(); // ir al primero
            rs.last(); // ir al último

            rs.beforeFirst(); // ir a antes del primero
            rs.afterLast(); // ir a después del último

            rs.relative(-1); // avanzar x posiciones
            rs.absolute(1); // ir a la posición x

            // recorrer datos
            rs.beforeFirst();
            while (rs.next()) {
                var id = DB.getInt(rs, "id");

                // modificación
                if (id % 2 == 0) {
                    rs.updateString("nombre", rs.getString("nombre") + '_' + id);
                    rs.updateRow();
                }

                // borrado               
                if (id > 4) {
                    rs.deleteRow();
                    continue;
                }

                var cliente = new Cliente(
                        id,
                        rs.getString("nombre"),
                        DB.getInt(rs, "edad"),
                        rs.getString("direccion"),
                        rs.getTimestamp("fecha_nacimiento"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );

                clientes.add(cliente);
            }
            
            // insercción
            rs.moveToInsertRow();
            rs.updateString("nombre", "nuevo nombre");           
            rs.updateInt("edad", 18);           
            rs.insertRow();

            entity = clientes;
            
        } catch (SQLException ex) {
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
}
