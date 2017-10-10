package net.ariflaksito.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class HttpConnect {

    private HttpClient httpclient = new DefaultHttpClient();
    private HttpPost httppost;
    private HttpGet httpget;
    private List<NameValuePair> var;
    private String out;

    public HttpConnect(String url, int len) {
        httppost = new HttpPost(url);
        var = new ArrayList<NameValuePair>(len);
    }

    public HttpConnect(String url) {
        httpget = new HttpGet(url);
    }

    public void addVar(String varName, String varData) {
        var.add(new BasicNameValuePair(varName, varData));
    }

    public List<NameValuePair> getVar() {
        return var;
    }

    // digunakan untuk method POST
    public boolean postData() {

        StringBuilder builder = new StringBuilder();

        try {
            httppost.setEntity(new UrlEncodedFormEntity(var));
            HttpResponse response = httpclient.execute(httppost);

            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            }

            out = builder.toString();
            return true;

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    // digunakan untuk method GET
    public boolean getData() {
        HttpClient Client = new DefaultHttpClient();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try {
            out = Client.execute(httpget, responseHandler);
            return true;

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public String getOutput() {
        return out;
    }

}