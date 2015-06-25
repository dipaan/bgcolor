package com.dipaan.bgcolor;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BackgroundColorServlet extends HttpServlet {

    QueueManager queueManager = new QueueManager();

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Provides methods to change the background color of a page.";
    }

    /**
     * Handles the HTTP <code>GET</code> method. Fetches a tuple from the queue
     * and returns it to the client.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

        Tuple tuple = queueManager.fetchNew();
        String bgColorId = tuple.getId();
        String bgColor = Integer.toHexString(tuple.getValue());
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            out.println("{ \"bgColorId\" : \"" + bgColorId
                + "\", \"bgColor\" : \"" + bgColor + "\" }");
        } finally {
            out.close();
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method. Processes a Set notification
     * indicating that a tuple has been used by the client.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

        String bgColorId = request.getParameter("bgColorId");
        queueManager.markUsed(bgColorId);
    }

}
