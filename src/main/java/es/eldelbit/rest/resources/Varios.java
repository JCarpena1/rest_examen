/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.eldelbit.rest.resources;

import es.eldelbit.rest.db.DB;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
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
}
