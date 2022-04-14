package com.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.beans.Ville;
import com.service.Process;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ListServlet")
public class ListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ListServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int page;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (Exception e) {
            page = 1;
        }

        Map<String, Object> villePage = Process.getVillesPaginated(page);
        request.setAttribute("villes", villePage.get("villes"));
        request.setAttribute("page", page);
        request.setAttribute("nbPages", villePage.get("nbPages"));
        request.getRequestDispatcher("/WEB-INF/list.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("_method");
        if (method.equals("put")) {
            doPut(request, response);
        } else if (method.equals("delete")) {
            doDelete(request, response);
        } else {
            Ville newVille = new Ville(request.getParameter("codeCommune"),
                    request.getParameter("nomCommune"),
                    request.getParameter("codePostal"),
                    request.getParameter("libelle"),
                    request.getParameter("ligne"),
                    request.getParameter("latitude"),
                    request.getParameter("longitude"));
            int responseCode = Process.createVille(newVille);
            String message = "";
            boolean success = false;
            switch (responseCode) {
                case 200:
                    message = "La ville a été créée avec succès.";
                    success = true;
                    break;
                case 409:
                    message = "La ville existe déjà.";
                    break;
                case -1:
                case 500:
                    message = "Quelque chose s'est mal passé.";
                    break;
            }
            request.setAttribute("message", message);
            request.setAttribute("success", success);
            doGet(request, response);
        }
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Ville updatedVille = new Ville(request.getParameter("codeCommune"),
                request.getParameter("nomCommune"),
                request.getParameter("codePostal"),
                request.getParameter("libelle"),
                request.getParameter("ligne"),
                request.getParameter("latitude"),
                request.getParameter("longitude"));
        int responseCode = Process.updateVille(updatedVille);
        String message = "";
        boolean success = false;
        switch (responseCode) {
            case 200:
                message = "La ville a été modifiée avec succès.";
                success = true;
                break;
            case 404:
                message = "La ville n'a pas été trouvée.";
                break;
            case -1:
            case 500:
                message = "Quelque chose s'est mal passé.";
                break;
        }
        request.setAttribute("message", message);
        request.setAttribute("success", success);
        doGet(request, response);
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String codeCommune = request.getParameter("codeCommune");
        int responseCode = Process.deleteVille(codeCommune);
        String message = "";
        boolean success = false;
        switch (responseCode) {
            case 200:
                message = "La ville a été supprimée avec succès.";
                success = true;
                break;
            case 404:
                message = "La ville n'a pas été trouvée.";
                break;
            case -1:
            case 500:
                message = "Quelque chose s'est mal passé.";
                break;
        }
        request.setAttribute("message", message);
        request.setAttribute("success", success);
        doGet(request, response);
    }
}
