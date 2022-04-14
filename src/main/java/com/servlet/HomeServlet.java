package com.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.beans.Ville;
import com.service.Process;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/HomeServlet")
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public HomeServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Ville> villes = Process.getVilles();
		HttpSession session = request.getSession();
		session.setAttribute("villes", villes);
		request.setAttribute("villes", villes);
		this.getServletContext().getRequestDispatcher("/WEB-INF/home.jsp").forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		List<Ville> villes = (List<Ville>) session.getAttribute("villes");
		if (villes == null) {
			villes = Process.getVilles();
			session.setAttribute("villes", villes);
		}
		Ville ville1 = Process.getVille(request.getParameter("ville1"));
		Ville ville2 = Process.getVille(request.getParameter("ville2"));
		int distanceDirect = Process.getDistanceDirect(ville1, ville2);
		int distanceRoad = Process.getDistanceRoad(ville1, ville2);
		HashMap<String, String> weatherVille1 = Process.getWeather(ville1);
		HashMap<String, String> weatherVille2 = Process.getWeather(ville2);
		
		request.setAttribute("villes", villes);
		request.setAttribute("displayDistance", true);
		request.setAttribute("ville1", ville1);
		request.setAttribute("ville2", ville2);
		request.setAttribute("distanceDirect", distanceDirect);
		request.setAttribute("distanceRoad", distanceRoad);
		request.setAttribute("weatherVille1", weatherVille1);
		request.setAttribute("weatherVille2", weatherVille2);
		this.getServletContext().getRequestDispatcher("/WEB-INF/home.jsp").forward(request, response);
	}
}
