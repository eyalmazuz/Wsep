package Domain.BGUExternalSystems;

import DTOs.ActionResultDTO;
import DTOs.IntActionResultDto;
import DTOs.ResultCode;
import Domain.ISupplySystem;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class SupplySystem implements ISupplySystem {

    private final String urlStr = "https://cs-bgu-wsep.herokuapp.com/";
    DefaultHttpClient httpClient = new DefaultHttpClient();
    HttpPost httpPost = new HttpPost(urlStr);

    public static boolean loseContact = false;

    @Override
    public boolean handshake() {
        if (loseContact) return false;

        List<NameValuePair> params = new ArrayList<>(1);
        params.add(new BasicNameValuePair("action_type", "handshake"));
        String response = send(params);
        return response != null && response.equals("OK");
    }

    @Override
    public IntActionResultDto requestSupply(String name, String address, String city, String country, String zip) {
        if (loseContact) return new IntActionResultDto(ResultCode.ERROR_SUPPLY_SYSTEM_UNAVAILABLE, "Could not contact supply system. Please try again later.", -1);

        List<NameValuePair> params = new ArrayList<>(6);
        params.add(new BasicNameValuePair("action_type", "supply"));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("address", address));
        params.add(new BasicNameValuePair("city", city));
        params.add(new BasicNameValuePair("country", country));
        params.add(new BasicNameValuePair("zip", zip));

        String response = send(params);
        if (response == null) return new IntActionResultDto(ResultCode.ERROR_SUPPLY_SYSTEM_UNAVAILABLE, "Could not contact supply system. Please try again later.", -1);
        return response.equals("-1") ? new IntActionResultDto(ResultCode.ERROR_SUPPLY_DENIED, "Supply system denied supply.", -1) :
                new IntActionResultDto(ResultCode.SUCCESS, null, Integer.parseInt(response));
    }

    @Override
    public ActionResultDTO cancelSupply(int transactionId) {
        if (loseContact) return new IntActionResultDto(ResultCode.ERROR_SUPPLY_SYSTEM_UNAVAILABLE, "Could not contact supply system. Please try again later.", -1);

        List<NameValuePair> params = new ArrayList<>(2);
        params.add(new BasicNameValuePair("action_type", "cancel_supply"));
        params.add(new BasicNameValuePair("transaction_id", Integer.toString(transactionId)));

        String response = send(params);
        if (response == null) return new IntActionResultDto(ResultCode.ERROR_SUPPLY_SYSTEM_UNAVAILABLE, "Could not contact supply system. Please try again later.", -1);
        return response.equals("1") ? new ActionResultDTO(ResultCode.SUCCESS, null) : new ActionResultDTO(ResultCode.ERROR_SUPPLY_DENIED, "Supply system denied cancellation.");
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response == null) return null;

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
