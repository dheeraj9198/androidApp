package com.example.dheeraj.superprofs;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.apache.http.util.EncodingUtils;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;


public class PaymentActivity extends ActionBarActivity {

    private static final String TAG = PaymentActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_payment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_payment, container, false);
            WebView webView = (WebView) rootView.findViewById(R.id.payment_web_view);
            webView.setWebViewClient(new WebViewClient());
            webView.getSettings().setJavaScriptEnabled(true);

            String orderId = UUID.randomUUID().toString();

            String password = "gtKFFx|"+orderId+"|10.00|dheeraj|dheeraj|dheeraj.sachan@aurusnet.com|||||||||||eCwWELxi";
            String hash = "";
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-512");
                md.update(password.getBytes());
                byte byteData[] = md.digest();
                //convert the byte to hex format method 1
                StringBuffer hashCodeStringBuffer = new StringBuffer();
                for (int i = 0; i < byteData.length; i++) {
                    hashCodeStringBuffer.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
                }
                hash = hashCodeStringBuffer.toString();
            }catch (NoSuchAlgorithmException e){
                Log.e(TAG,"algo exception",e);
            }

            String postData = "firstname=dheeraj&" +
                    "lastname=&"+
                    "surl=http://52.0.182.111/api/logs&"+
                    "phone=7406628160&"+
                    "key=gtKFFx&"+
                    "hash="+hash+"&"+
                    "curl=http://52.0.182.111/api/logs&"+
                    "furl=http://52.0.182.111/api/logs&"+
                    "txnid="+orderId+"&"+
                    "productinfo=dheeraj&"+
                    "amount=10.00&"+
                    "email=dheeraj.sachan@aurusnet.com&";

            webView.postUrl("https://test.payu.in/_payment", EncodingUtils.getBytes(postData, "BASE64"));

            return rootView;
        }
    }
}
