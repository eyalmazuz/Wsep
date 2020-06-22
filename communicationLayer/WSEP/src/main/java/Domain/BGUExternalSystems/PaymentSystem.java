package Domain.BGUExternalSystems;

import DTOs.ActionResultDTO;
import DTOs.IntActionResultDto;
import DTOs.ResultCode;
import Domain.IPaymentSystem;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;

import java.util.ArrayList;
import java.util.List;

public class PaymentSystem implements IPaymentSystem {

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
    public IntActionResultDto attemptPurchase(String cardNumber, String expirationMonth, String expirationYear, String holder, String ccv, String cardId) {
        if (loseContact) return new IntActionResultDto(ResultCode.ERROR_PAYMENT_SYSTEM_UNAVAILABLE, "Could not contact payment system. Please try again later.", -1);

        List<NameValuePair> params = new ArrayList<>(7);
        params.add(new BasicNameValuePair("action_type", "pay"));
        params.add(new BasicNameValuePair("card_number", cardNumber));
        params.add(new BasicNameValuePair("month", expirationMonth));
        params.add(new BasicNameValuePair("year", expirationYear));
        params.add(new BasicNameValuePair("holder", holder));
        params.add(new BasicNameValuePair("ccv", ccv));
        params.add(new BasicNameValuePair("id", cardId));

        String response = send(params);
        int statusCode = 0;
        try{
            statusCode = Integer.parseInt(response);
        }catch(NumberFormatException e){
            statusCode = -1;
        }
        if (response == null) return new IntActionResultDto(ResultCode.ERROR_PAYMENT_SYSTEM_UNAVAILABLE, "Could not contact payment system. Please try again later.", -1);
        return statusCode == -1 ?
                new IntActionResultDto(ResultCode.ERROR_PAYMENT_DENIED, "Payment system denied payment.", -1) :
                new IntActionResultDto(ResultCode.SUCCESS, null, statusCode);
    }

    @Override
    public ActionResultDTO requestRefund(int transactionId) {
        if (loseContact) return new IntActionResultDto(ResultCode.ERROR_PAYMENT_SYSTEM_UNAVAILABLE, "Could not contact payment system. Please try again later.", -1);

        List<NameValuePair> params = new ArrayList<>(2);
        params.add(new BasicNameValuePair("action_type", "cancel_pay"));
        params.add(new BasicNameValuePair("transaction_id", Integer.toString(transactionId)));

        String response = send(params);
        if (response == null) return new IntActionResultDto(ResultCode.ERROR_PAYMENT_SYSTEM_UNAVAILABLE, "Could not contact payment system. Please try again later.", -1);
        return response.equals("1") ? new ActionResultDTO(ResultCode.SUCCESS, null) : new ActionResultDTO(ResultCode.ERROR_PAYMENT_DENIED, "Payment system denied refund.");
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
