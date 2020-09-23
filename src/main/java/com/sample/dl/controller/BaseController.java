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
import javax.ws.rs.core.StreamingOutput;
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
            String st = DataCache.getInstance().getStatus();
            System.out.println(st);
            if (st.equalsIgnoreCase("Caching")) {
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

        Set TCs = new TreeSet<String>();
        TCs.addAll(Arrays.asList(tcHeader.split(",")));
        data.put("tc", TCs);

        // Connect Mongo DB
        MongoDb mD = new MongoDb();
        DBCollection col = mD.getDB("FlashHatch").getCollection("TestCases");

        // Insert or Update
        DBObject cursorTCs = col.findOne(new BasicDBObject("tc", TCs));
        if (cursorTCs != null) {
            // Get already listof methods
            Set dbMethods = new TreeSet<String>();
            dbMethods.addAll((List) cursorTCs.get("coverName"));

            // Get latest caching list of methods
            Set cachedMethods = new TreeSet<String>();
            cachedMethods.addAll((TreeSet) data.get("coverName"));

            // Update for new
            if (!dbMethods.equals(cachedMethods)) {
                dbMethods.addAll(cachedMethods);
                data.put("coverName", dbMethods);
                col.update(new BasicDBObject("tc", TCs), new BasicDBObject(data));
                System.out.println("--------------------------- Update DB " + data);
            }
        } else {
            col.insert(new BasicDBObject(data));
            System.out.println("--------------------------- Insert DB " + data);
        }
    }
}

// https://www.mongodb.com/blog/post/quick-start-java-and-mongodb--update-operations?utm_campaign=updatejava&utm_source=twitter&utm_medium=organic_social