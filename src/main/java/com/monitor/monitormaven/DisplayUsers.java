/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monitor.monitormaven;

import com.webservice.android.AndroidWebService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.java.dev.jaxb.array.StringArray;
import org.json.simple.JSONObject;

/**
 *
 * @author TomHardy
 */
@WebServlet(name = "DisplayUsers", urlPatterns = {"/DisplayUsers"})
public class DisplayUsers extends HttpServlet {

    public final static String AUTH_KEY_FCM = "AAAAgB2I4bs:APA91bFxM9j79sdSul5PUl8jxujpu0qDAJjTSZAREWomFdvLYxFs2I7t9RQcL8SgYp8Zvw7rhm814xQyihIEWrWx--UflVuTQovMMq5tLbCo4WQzqakhctq9Xfb9ffU4XHxzT8vpM7kg";
    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";

    private String sendNotification(String DeviceIdKey) {

        try {
            URL url = new URL(API_URL_FCM);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "key=" + AUTH_KEY_FCM);
            conn.setRequestProperty("Content-Type", "application/json");
            System.out.println(DeviceIdKey);

            JSONObject jSONObject = new JSONObject();
            jSONObject.put("to", DeviceIdKey.trim());

            JSONObject info = new JSONObject();

            info.put("title", "FCM Notification Title"); // Notification title
            info.put("body", "Hello First Test notification"); // Notification body
            jSONObject.put("notification", info); // <= changed from "data"

            System.out.println(jSONObject.toString());

            try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(conn.getOutputStream())) {
                outputStreamWriter.write(jSONObject.toString());
                outputStreamWriter.flush();
            } catch (Exception e) {
                Logger.getLogger(AndroidWebService.class.getName()).log(Level.SEVERE, null, e);
            }

            int responseCode = conn.getResponseCode();
            System.out.println("Response Code : " + responseCode);

            StringBuffer response = null;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String inputLine;
                response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            } catch (Exception e) {
                Logger.getLogger(AndroidWebService.class.getName()).log(Level.SEVERE, null, e);
            }

            System.out.println("Resonse: " + response);

            return "SUCCESS";
        } catch (Exception e) {
            System.out.println(e);
            Logger.getLogger(AndroidWebService.class.getName()).log(Level.SEVERE, null, e);
            return e.toString();
        }
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */

            if (request.getParameter("send request") != null) { // is submit for 'send request' is pressed
                String[] selectedUsers = request.getParameterValues("selected");

                int length = 0;
                if (selectedUsers != null) {
                    length = selectedUsers.length;
                }
                String results = "";
                for (int i = 0; i < length; i++) {
                    results += sendNotification(selectedUsers[i]) + " ,";
                }

                request.getSession().setAttribute("message", "sendNotification status : " + results);
                response.sendRedirect(request.getHeader("referer"));
            }

            out.println("<h1>Display Users : </h1>");

            // Call Web Service Operation
            com.webservice.android.AndroidWebService_Service service = new com.webservice.android.AndroidWebService_Service();
            com.webservice.android.AndroidWebService port = service.getAndroidWebServicePort();

            // TODO process result here
            List<StringArray> result = port.getUsers();
            Iterator<StringArray> iterator = result.iterator();

            out.print("<form action=\"DisplayUsers\" method=\"POST\">");
            out.print("<table>");
            out.print("<tr><th>select</th><th>username</th><th>longitude</th><th>latitude</th><th>status</th><th>proximity</th><th>light</th></tr>");

            while (iterator.hasNext()) {
                StringArray value = iterator.next();

                List<String> eachRow = value.getItem();

                Iterator<String> it = eachRow.iterator();

                String token = it.next();  // token
                out.print("<tr>");
                out.print("<td> <input type=\"checkbox\" name=\"selected\" value=\"" + token + "\"></td>");

                while (it.hasNext()) {
                    out.print("<td>" + it.next() + "</td>");
                }
                out.print("</tr>");

            }
            out.println("</table>");

            out.println("<input type=\"submit\" name=\"send request\" value=\"send request\" />\n");
            out.println("</form>");

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
