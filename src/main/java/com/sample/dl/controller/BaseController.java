package com.sample.dl.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sample.agent.utils.DataCache;
import com.sample.agent.utils.MongoDb;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.*;

@Component
@Path("/")
public class BaseController {

    @GET
    @Produces("application/json")
    @Path("/info")
    public Response appInfo(@HeaderParam("testcase") String testCase) {
        String greet = "WARNING: This ApP is HACKED!!!";
        System.out.println(greet);
        String output = "{'greet': '" + greet + "', 'status': 'hacked'}";
        return Response.status(200).entity(output).build();
    }

    @GET
    @Produces("application/json")
    @Path("/register")
    public Response registerTestcase(@HeaderParam("testcase") String testCase) {
        DataCache.getInstance().updateStatus("Caching");
        DataCache.getInstance().addData("tc", testCase);

        String greet = "App is OK for caching data";
        System.out.println(greet);
        String output = "{'greet': '" + greet + "', 'status': 'registered'}";
        return Response.status(200).entity(output).build();
    }


    @GET
    @Produces("application/json")
    @Path("/logout")
    public Response logOut(@HeaderParam("testcase") String testCase) {
        try {
            if (DataCache.getInstance().getStatus().equalsIgnoreCase("Caching")) {
                // Write data to MongoDB
                DataCache.getInstance().addData("coverName", DataCache.getInstance().getMethods());
                writeDataToMongoDB(DataCache.getInstance().getData());
                System.out.println("Wrote to MongoDB");
            }
        } finally {
            // Clear data cache
            DataCache.getInstance().reset();

            // Println
            String greet = "Data clearing!!!";
            // Return Response
            String output = "{'greet': '" + greet + "', 'status': 'clean'}";
            return Response.status(200).entity(output).build();
        }
    }


    private void writeDataToMongoDB(Map data) {

        // Handle TC-ID before writing to DB
        String tcHeader = data.get("tc").toString().trim();

        System.out.println(tcHeader);

        Set TCs = new TreeSet<String>();
        TCs.addAll(Arrays.asList(tcHeader.split(",")));
        data.put("tc", TCs);

        // Connect Mongo DB
        MongoDb mD = new MongoDb();
        DBCollection col = mD.getDB("FlashHatch").getCollection("TestCases");

        // Insert or Update
        DBObject cursorTCs = col.findOne(new BasicDBObject("tc", TCs));
        if (cursorTCs != null) {
            // Get already list
            Set dbTCs = new TreeSet<String>();
            dbTCs.addAll((List) cursorTCs.get("coverName"));

            // Get latest caching data
            Set cachedTCs = new TreeSet<String>();
            cachedTCs.addAll((List) data.get("coverName"));

            // Update fo new
            if (!dbTCs.equals(cachedTCs)) {
                dbTCs.addAll(cachedTCs);
                data.put("coverName", dbTCs);
            } else {
                return;
            }
        }
        col.insert(new BasicDBObject(data));
    }
}