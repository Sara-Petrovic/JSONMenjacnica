package rs.ac.bg.fon.ai.JSONMenjacnica.main;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import rs.ac.bg.fon.ai.JSONMenjacnica.transakcija.Transakcija;

public class Main2 {
	
	private static final String BASE_URL = "http://api.currencylayer.com";
	private static final String API_KEY = "812c74d90b7e8b8e1c9b663cc87995e2";
	private static final String DATE = "2020-07-17";

	public static void main(String[] args) {
		
		try {
			Date datum = new GregorianCalendar(2020, Calendar.JULY, 17).getTime();
			
			Transakcija transakcija1 = new Transakcija();
			transakcija1.setIzvornaValuta("USD");
			transakcija1.setKrajnjaValuta("EUR");
			transakcija1.setDatumTransakcije(datum);
			transakcija1.setPocetniIznos(100);
			
			Transakcija transakcija2 = new Transakcija();
			transakcija2.setIzvornaValuta("USD");
			transakcija2.setKrajnjaValuta("CHF");
			transakcija2.setDatumTransakcije(datum);
			transakcija2.setPocetniIznos(100);
			
			Transakcija transakcija3 = new Transakcija();
			transakcija3.setIzvornaValuta("USD");
			transakcija3.setKrajnjaValuta("CAD");
			transakcija3.setDatumTransakcije(datum);
			transakcija3.setPocetniIznos(100);
			
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
			URL url = new URL(BASE_URL + "/historical?access_key="+API_KEY+"&date="+DATE+
						"&source="+transakcija1.getIzvornaValuta()+"&currencies="+transakcija1.getKrajnjaValuta() + "," + transakcija2.getKrajnjaValuta() + "," + transakcija3.getKrajnjaValuta());
			
			System.out.println(url.toString());
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			
			con.setRequestMethod("GET");
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			JsonObject rez = gson.fromJson(reader, JsonObject.class);
			System.out.println(rez);

			
			if(rez.get("success").getAsBoolean()) {
				transakcija1.setKonvertovaniIznos( rez.get("quotes").getAsJsonObject().get("USDEUR").getAsDouble()*transakcija1.getPocetniIznos());
				transakcija2.setKonvertovaniIznos( rez.get("quotes").getAsJsonObject().get("USDCHF").getAsDouble()*transakcija2.getPocetniIznos());
				transakcija3.setKonvertovaniIznos( rez.get("quotes").getAsJsonObject().get("USDCAD").getAsDouble()*transakcija3.getPocetniIznos());
			}
			
			
			List<Transakcija> transakcije = new LinkedList<Transakcija>();
			transakcije.add(transakcija1);
			transakcije.add(transakcija2);
			transakcije.add(transakcija3);
						
			try(FileWriter file = new FileWriter("ostale_transakcije.json")){
				
				gson.toJson(transakcije, file);
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
	}

}
