package com.gamebench.gbaclientlib;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.sun.prism.impl.Disposer;
import org.json.JSONArray;
import org.json.JSONObject;

public class GbaClient {
    private String baseUrl;

    public GbaClient(String baseUrl) {
       this.baseUrl = baseUrl;
    }

    public List<Device> listDevices() throws GbaClientException {
        String response = get("devices");
        JSONArray jsonArray =  new JSONArray(response);

        ArrayList apps = new ArrayList();

        jsonArray.iterator().forEachRemaining(element -> {
            JSONObject jsonObject = (JSONObject) element;
            Device device = new Device();
            device.setId(jsonObject.getString("id"));
            device.setName(jsonObject.getString("name"));
            apps.add(device);
        });

        return apps;
    }

    public List<App> listDeviceApps(String deviceId) throws GbaClientException {
        String response = get("devices/" + deviceId + "/apps");
        JSONArray jsonArray = new JSONArray(response);

        ArrayList apps = new ArrayList();

        jsonArray.iterator().forEachRemaining(element -> {
            JSONObject jsonObject = (JSONObject) element;
            App app = new App();
            app.setIdentifier(jsonObject.getString("identifier"));
            apps.add(app);
        });

        return apps;
    }

    public Device getDevice(String deviceId) throws GbaClientException {
        String response = get("devices/" + deviceId);
        JSONObject jsonObject = new JSONObject(response);

        Device device = new Device();
        device.setId(jsonObject.getString("id"));
        device.setName(jsonObject.getString("name"));
        return device;
    }

    public Session startSession(String deviceId, String appId, StartSessionOptions options) throws GbaClientException {
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("deviceId", deviceId);
        jsonBody.put("appId", appId);
        jsonBody.put("screenshots", options.isScreenshots());
        jsonBody.put("autoSync", options.isAutoSync());

        String response = post("sessions", jsonBody);
        JSONObject jsonObject = new JSONObject(response);

        Session session = new Session(jsonObject.getString("id"));
        return session;
    }

    public JSONObject stopSession(String sessionId) throws GbaClientException {
        String response = post("sessions/" + sessionId + "/stop", new JSONObject().put("includeSessionJsonInResponse", true));
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject;
    }

    public void sync() throws GbaClientException {
        post("sessions/sync", new JSONObject());
    }

    public List<Session> listSessions() throws GbaClientException {
       String response = get("sessions");
       JSONArray jsonArray = new JSONArray(response);
       ArrayList<Session> sessions = new ArrayList<>();
       jsonArray.iterator().forEachRemaining(element -> {
           JSONObject e = (JSONObject) element;
           Session session = new Session(e.getString("id"));
           sessions.add(session);
       });
       return sessions;
    }

    private String get(String path) throws GbaClientException {
        URL url = null;

        try {
            url = new URL(String.format("%s/%s", this.baseUrl, path));
        } catch (MalformedURLException e) {
            throw new GbaClientException(e);
        }

        HttpURLConnection conn;

        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            throw new GbaClientException(e);
        }

        try {
            conn.setRequestMethod("GET");
        } catch (ProtocolException e) {
            throw new GbaClientException(e);
        }

        try(BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            return response.toString();
        } catch (UnsupportedEncodingException e) {
            throw new GbaClientException(e);
        } catch (IOException e) {
            throw new GbaClientException(e);
        }
    }

    public String post(String path, JSONObject jsonBody) throws GbaClientException {
        URL url = null;

        try {
            url = new URL(String.format("%s/%s", this.baseUrl, path));
        } catch (MalformedURLException e) {
            throw new GbaClientException(e);
        }

        HttpURLConnection conn;

        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            throw new GbaClientException(e);
        }

        try {
            conn.setRequestMethod("POST");
        } catch (ProtocolException e) {
            throw new GbaClientException(e);
        }

        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonBody.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        } catch (IOException e) {
            throw new GbaClientException(e);
        }

        try(BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            return response.toString();
        } catch (UnsupportedEncodingException e) {
            throw new GbaClientException(e);
        } catch (IOException e) {
            throw new GbaClientException(e);
        }
    }
}