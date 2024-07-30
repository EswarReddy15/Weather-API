package Mypackage;

import java.io.IOException;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@WebServlet("/Myservlet")
public class Myservlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Myservlet() {
        super();
        // TODO Auto-generated constructor stub
    }



	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//API setup
		String apikey="a66030a74d3efb1ed367423518349b89";
		String getcity=request.getParameter("city");
		//System.out.println(getcity);
		//create the url for openweatherapp
		String apiurl="https://api.openweathermap.org/data/2.5/weather?q="+getcity+"&appid="+apikey;
		// api integration
		try {
		URL url=new URL(apiurl);
		HttpURLConnection connection=(HttpURLConnection)url.openConnection();
		connection.setRequestMethod("GET");
		
		//Reading the data from network
		InputStream inputStream=connection.getInputStream();
		InputStreamReader reader=new InputStreamReader(inputStream);
		
		//want to store in String
		StringBuilder responsecontent=new StringBuilder();
		
		//for getting input from reader, will create scanner obj
		Scanner scanner=new Scanner(reader);
		
		while(scanner.hasNext()){
			responsecontent.append(scanner.nextLine());
		}
		scanner.close();
	//	System.out.println(responsecontent);
		Gson gson=new Gson();
		JsonObject jsonobj=gson.fromJson(responsecontent.toString(),JsonObject.class);
//		System.out.println(jsonobj);
		long dateTimestamp=jsonobj.get("dt").getAsLong()*1000;
		String date=new Date(dateTimestamp).toString();
		
		double temperaturekelvin=jsonobj.getAsJsonObject("main").get("temp").getAsDouble();
		int temperaturecelsius=(int)(temperaturekelvin-273.15);
		
		int humidity=jsonobj.getAsJsonObject("main").get("humidity").getAsInt();

		double windspeed=jsonobj.getAsJsonObject("wind").get("speed").getAsDouble();
		
		  //Weather Condition
        String weatherCondition = jsonobj.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").toString();
        
        // Set the data as request attributes (for sending to the jsp page)
        request.setAttribute("date", date);
        request.setAttribute("city", getcity);
        request.setAttribute("temperature", temperaturecelsius);
        request.setAttribute("weatherCondition", weatherCondition); 
        request.setAttribute("humidity", humidity);    
        request.setAttribute("windSpeed", windspeed);
        request.setAttribute("weatherData", responsecontent.toString());
        
        connection.disconnect();
} 
catch (IOException e) {
    e.printStackTrace();
}

// Forward the request to the weather.jsp page for rendering
request.getRequestDispatcher("index.jsp").forward(request, response);		
		}
	
	
	}




