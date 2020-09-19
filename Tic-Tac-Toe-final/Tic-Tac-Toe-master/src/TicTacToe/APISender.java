package TicTacToe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.Gson;

import okhttp3.Response;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;

public class APISender {
	public static final String HOST = "http://www.notexponential.com/aip2pgaming/api/index.php";
    public static final String TEAMID = "1205";
    public static final String APIKEY = "9b392e42cf6813dafbed";
    public static final String USERID = "868";
    
    public static String GAMEID;
    
    protected final OkHttpClient httpClient = new OkHttpClient();
 
    
    public APISender(int GameId) {
    	this.GAMEID = Integer.toString(GameId);
    }
    
    public int[] getMove() throws IOException {
    	
        Request request = new Request.Builder()
                .url(String.format("%s?%s%s", HOST, "type=moves&count=1&gameId=", GAMEID))
                .addHeader("x-api-key", APIKEY)
                .addHeader("userid", USERID)
                .get()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            Gson gson = new Gson();
            Moves moves = gson.fromJson(response.body().string(), Moves.class);
            try {
            	JsonObject move = (JsonObject) moves.getMoves().get(0);
	            int x = Integer.parseInt(move.get("moveX").toString().replace("\"", ""));
	            int y = Integer.parseInt(move.get("moveY").toString().replace("\"", ""));
	            System.out.println("x: " + x);
	            System.out.println("y: " + y);
	            return  new Move(x, y).getMove();
            }catch (Exception e) {
            	return new int[] {-1, -1};
            }
        }
    }
    
	 public boolean sendMove(int x, int y) throws IOException {

	        RequestBody body = new FormBody.Builder()
	                .add("gameId", GAMEID)
	                .add("type", "move")
	                .add("teamId", TEAMID)
	                .add("move", x + "," + y)
	                .build();

	        Request request = new Request.Builder()
	                .url(HOST)
	                .addHeader("x-api-key", APIKEY)
	                .addHeader("userid", USERID)
	                .post(body)
	                .build();

	        try (Response response = httpClient.newCall(request).execute()) {

	            if (!response.isSuccessful()) {
//	            	throw new IOException("Unexpected code " + response);
	            	return false;
	            }

	            // Get response body
	            System.out.println(response.body().string());
	        }

	        return true;
    }
    
	public static void main(String[] args) {
		APISender sender = new APISender(235);
		int[] move;
		try {
			move = sender.getMove();
			System.out.println(move[0] + move[1]);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
}

class Moves{
	private JsonArray moves;
	public Moves() {
		
	}
	
	public JsonArray getMoves() {
		return this.moves;
	}
	
}

















