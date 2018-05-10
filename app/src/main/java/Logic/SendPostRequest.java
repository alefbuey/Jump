package Logic;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendPostRequest extends AsyncTask<String, Void, String> {

    Context context;
    String receiveUrl;
    JSONObject receiveJSON;

    String mensaje; //Mensaje de confirmacion, si desea ponerlo

    public SendPostRequest(Context context, String receiveUrl, JSONObject receiveJSON) {
        this.context = context;
        this.receiveUrl = receiveUrl;
        this.receiveJSON = receiveJSON;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    protected void onPreExecute(){}

    protected String doInBackground(String... arg0) {

        String response;
        try {

            URL url = new URL(this.receiveUrl); // here is your URL path
            String message = this.receiveJSON.toString();

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(10000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(message.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            conn.connect();

            OutputStream os = new BufferedOutputStream(conn.getOutputStream());;
            os.write(message.getBytes());
            os.close();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);

            conn.disconnect();


        } catch (IOException e) {
            response = "Exception: " + e.getMessage();

        }

        return response;
    }

    @Override
    protected void onPostExecute(String respuesta) {
        if(respuesta!=null){
            Toast.makeText(this.context, respuesta, Toast.LENGTH_LONG).show();
        }
    }



    @NonNull
    private String convertStreamToString(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
