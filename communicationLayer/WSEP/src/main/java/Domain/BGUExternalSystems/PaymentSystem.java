package Domain.BGUExternalSystems;

import Domain.IPaymentSystem;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import sun.net.www.http.HttpClient;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PaymentSystem implements IPaymentSystem {

    private final String urlStr = "https://cs-bgu-wsep.herokuapp.com/";
    DefaultHttpClient httpClient = new DefaultHttpClient();
    HttpPost httpPost = new HttpPost(urlStr);


    @Override
    public boolean handshake() {
        List<NameValuePair> params = new ArrayList<>(1);
        params.add(new BasicNameValuePair("action_type", "handshake"));
        String response = send(params);
        return response.equals("OK");
    }

    @Override
    public int attemptPurchase(String cardNumber, String expirationMonth, String expirationYear, String holder, String ccv, String cardId) {
        List<NameValuePair> params = new ArrayList<>(7);
        params.add(new BasicNameValuePair("action_type", "pay"));
        params.add(new BasicNameValuePair("card_number", cardNumber));
        params.add(new BasicNameValuePair("month", expirationMonth));
        params.add(new BasicNameValuePair("year", expirationYear));
        params.add(new BasicNameValuePair("holder", holder));
        params.add(new BasicNameValuePair("ccv", ccv));
        params.add(new BasicNameValuePair("id", cardId));

        String response = send(params);
        return Integer.parseInt(response);
    }

    @Override
    public boolean requestRefund(int transactionId) {
        List<NameValuePair> params = new ArrayList<>(2);
        params.add(new BasicNameValuePair("action_type", "cancel_pay"));
        params.add(new BasicNameValuePair("transaction_id", Integer.toString(transactionId)));

        String response = send(params);
        return response.equals("1");
    }

    private String send(List<NameValuePair> parameters) {
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            try (InputStream instream = entity.getContent()) {
                StringBuilder sb = new StringBuilder();
                int c;
                while((c = instream.read()) >= 0) {
                    sb.append((char) c);
                }
                return sb.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
