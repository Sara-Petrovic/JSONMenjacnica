package rs.ac.bg.fon.ai.JSONMenjacnica.main;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import rs.ac.bg.fon.ai.JSONMenjacnica.transakcija.Transakcija;

public class Main1 {

	private static final String BASE_URL = "http://api.currencylayer.com";
	private static final String API_KEY = "812c74d90b7e8b8e1c9b663cc87995e2";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//broj indeksa 58/2017-------> 58 americkih dolara = ? kanadskih dolara
		
		try {
			Transakcija transakcija = new Transakcija();
			transakcija.setIzvornaValuta("USD");
			transakcija.setKrajnjaValuta("CAD");
			transakcija.setDatumTransakcije(new Date());
			transakcija.setPocetniIznos(58);
			
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
			URL url = new URL(BASE_URL + "/live?access_key="+API_KEY+
						"&source="+transakcija.getIzvornaValuta()+"&currencies="+transakcija.getKrajnjaValuta());
			
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			
			con.setRequestMethod("GET");
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			JsonObject rez = gson.fromJson(reader, JsonObject.class);
			System.out.println(rez);

			
			if(rez.get("success").getAsBoolean()) {
				transakcija.setKonvertovaniIznos( rez.get("quotes").getAsJsonObject().get("USDCAD").getAsDouble()*transakcija.getPocetniIznos());
				
			}
			
				try(FileWriter file = new FileWriter("prva_transakcija.json")){
					gson.toJson(transakcija, file);
				}catch(Exception e) {
					e.printStackTrace();
				}
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
	}

}
