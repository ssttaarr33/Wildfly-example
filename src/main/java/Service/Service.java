/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import Obj.Light;
import Obj.State;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author adrian.stoicescu
 */
@Path("")
public class Service {

    HashMap<String, Boolean> lights = new HashMap<String, Boolean>();

    public void populateLights() {
        this.lights.put("kitchen", true);
        this.lights.put("living room", true);
        this.lights.put("bedroom 1", false);
        this.lights.put("bedroom 2", false);
        this.lights.put("bathroom 1", false);
        this.lights.put("bathroom 2", true);
        this.lights.put("balcony 1", true);
        this.lights.put("balcony 2", true);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("ping")
    public Response test() {
        String response = "Pong";
        return Response
                .status(200)
                .entity(response)
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("lightsState")
    public Response getLightsState() {
        JSONObject response = new JSONObject();
        JSONArray tasks = new JSONArray();
        populateLights();
        for (Map.Entry<String, Boolean> entry : lights.entrySet()) {
            JSONObject obj = new JSONObject();
            Light light = new Light(entry.getKey(), entry.getValue(), 1);
            obj.put("Light", ((Light) light).getLocation());
            obj.put("On", ((Light) light).getState());
            tasks.add(obj);
        }
        response.put("Lights", tasks);
        return Response
                .status(200)
                .entity(response)
                .build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("switchSingleLight")
    public Response switchSingleLight(@FormParam("light") String light, @FormParam("state") String state) {
        JSONObject response = new JSONObject();
        if (state.equals("on")) {
            this.lights.put(light, false);
            state = "off";
        } else {
            this.lights.put(light, true);
            state = "on";
        }
        response.put("light", light);
        response.put("state", state);
        return Response
                .status(200)
                .entity(response)
                .build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("switchLightsOff")
    public Response switchLight(@FormParam("lights") String lightsJson) throws ParseException, ExecutionException {
        JSONObject response = new JSONObject();
        JSONArray tasks = new JSONArray();
        JSONArray results = new JSONArray();
        JSONArray lightsList = (JSONArray) new JSONParser().parse(lightsJson);
        for (Object entry : lightsList) {
            JSONObject obj = (JSONObject) entry;
            if (obj.get("On").equals("on")) {
                this.lights.put((String) obj.get("Light"), true);
            } else {
                this.lights.put((String) obj.get("Light"), false);
            }
            System.out.println(obj.get("Light"));
            System.out.println(obj.get("On"));
        }
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        List<Future<State>> list = new ArrayList<Future<State>>();
        for (Map.Entry<String, Boolean> entry : lights.entrySet()) {
            JSONObject obj = new JSONObject();
            Callable<State> light = new Light(entry.getKey(), entry.getValue(), 1);
            obj.put("Light found", ((Light) light).getLocation());
            obj.put("On", ((Light) light).getState());
            Future<State> future = executor.submit(light);
            list.add(future);
            tasks.add(obj);
        }
        JSONArray newStates = new JSONArray();
        for (Future<State> event : list) {
            try {
                JSONObject obj = new JSONObject();
                JSONObject state = new JSONObject();
                obj.put("task", new Date() + " - " + event.get().getMessage());
                state.put("Light", event.get().getField());
                state.put("On", event.get().getState());
                results.add(obj);
                newStates.add(state);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        response.put("Lights", tasks);
        response.put("results", results);
        response.put("newStates", newStates);
        return Response
                .status(200)
                .entity(response)
                .build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("switchLightsOn")
    public Response switchLightsOn(@FormParam("lights") String lightsJson) throws ParseException, ExecutionException {
        JSONObject response = new JSONObject();
        JSONArray tasks = new JSONArray();
        JSONArray results = new JSONArray();
        JSONArray lightsList = (JSONArray) new JSONParser().parse(lightsJson);
        for (Object entry : lightsList) {
            JSONObject obj = (JSONObject) entry;
            if (obj.get("On").equals("on")) {
                this.lights.put((String) obj.get("Light"), true);
            } else {
                this.lights.put((String) obj.get("Light"), false);
            }
            System.out.println(obj.get("Light"));
            System.out.println(obj.get("On"));
        }
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        List<Future<State>> list = new ArrayList<Future<State>>();
        for (Map.Entry<String, Boolean> entry : lights.entrySet()) {
            JSONObject obj = new JSONObject();
            Callable<State> light = new Light(entry.getKey(), entry.getValue(), 2);
            obj.put("Light found", ((Light) light).getLocation());
            obj.put("On", ((Light) light).getState());
            Future<State> future = executor.submit(light);
            list.add(future);
            tasks.add(obj);
        }
        JSONArray newStates = new JSONArray();
        for (Future<State> event : list) {
            try {
                JSONObject obj = new JSONObject();
                JSONObject state = new JSONObject();
                obj.put("task", new Date() + " - " + event.get().getMessage());
                state.put("Light", event.get().getField());
                state.put("On", event.get().getState());
                results.add(obj);
                newStates.add(state);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        response.put("Lights", tasks);
        response.put("results", results);
        response.put("newStates", newStates);
        return Response
                .status(200)
                .entity(response)
                .build();
    }    
 
}
