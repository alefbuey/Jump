package Logic;

import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public abstract class SendGetRequest extends AsyncTask<String, Void, String> {

    private static final String TAG = SendGetRequest.class.getSimpleName();

    private String receiveUrl;

    protected SendGetRequest(String receiveUrl) {
        this.receiveUrl = receiveUrl;
    }
    @Override
    protected String doInBackground(String... strings) {
        String response = null;
        int code = 0;
        try {
            URL url = new URL(this.receiveUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(7000 /* milliseconds */);
            conn.setConnectTimeout(7000 /* milliseconds */);
            code = conn.getResponseCode();
            if(code==HttpURLConnection.HTTP_OK){
                // read the response
                InputStream in = new BufferedInputStream(conn.getInputStream());
                response = convertStreamToString(in);
            }

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }


    @Override
    protected abstract void onPostExecute(String response);


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