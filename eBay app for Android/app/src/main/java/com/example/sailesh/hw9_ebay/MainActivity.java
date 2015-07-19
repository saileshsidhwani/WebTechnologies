package com.example.sailesh.hw9_ebay;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.net.URLEncoder;


public class MainActivity extends ActionBarActivity {


    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buildURL(String key, Double priceFrom, Double priceTo) {
        String tempURL;
        String sortString = null;
        Spinner sort = (Spinner) findViewById(R.id.sortBy);
        key = key.trim();
        key = URLEncoder.encode(key);
        int i = sort.getSelectedItemPosition();
        tempURL = "http://csci571hw8-sidhwani.elasticbeanstalk.com/index.php?keywords=" + key;
        /*tempURL = "http://cs-server.usc.edu:29084/hw8php.php?keywords=" + key;*/
        if(priceFrom == null)
          tempURL += "&from=";
        else
            tempURL += "&from=" + priceFrom;

        if(priceTo == null)
            tempURL += "&to=";
        else
            tempURL += "&to=" + priceTo;

        if(i==0)
            sortString = "BestMatch";
        else if(i==1)
            sortString = "CurrentPriceHighest";
        else if(i==2)
            sortString = "PricePlusShippingHighest";
        else
            sortString = "PricePlusShippingLowest";

        tempURL += "&sortBy=" + sortString + "&resultsPerPage=5";
        Log.v("url", tempURL);
        url = tempURL;
    }

    public boolean validate(View view) {
        TextView err = (TextView) findViewById(R.id.errorMsg);
        err.setText("");
        EditText from = (EditText) findViewById(R.id.from);
        EditText to = (EditText) findViewById(R.id.to);
        EditText keywords = (EditText) findViewById(R.id.keywords);
        Double priceTo = null;
        Double priceFrom = null;
        String key;
        int flag = 0;

        if (String.valueOf(to.getText()).trim().length() == 0) {
            priceTo = null;
        } else {
            priceTo = Double.parseDouble(String.valueOf(to.getText()));
        }

        if (String.valueOf(from.getText()).trim().length() == 0) {
            priceFrom = null;
        } else {
            priceFrom = Double.parseDouble(String.valueOf(from.getText()));
        }
        key = String.valueOf(keywords.getText());
        if (key.trim().length() == 0) {
            err.setText(err.getText() + "Please enter a keyword\n");
            flag = 1;
        }
        if(priceFrom != null && priceTo != null)
        {
            if (priceFrom > priceTo) {
                err.setText(err.getText() + "Price From cannot be less than Price To\n");
                flag = 1;
                }
        }

        if (flag == 0) {
            buildURL(key, priceFrom, priceTo);
            Log.v("url is", url);
            new request().execute();
            return true;
        } else
            return false;
    }

    public void clearForm(View view)
    {
        TextView err = (TextView) findViewById(R.id.errorMsg);
        EditText from = (EditText) findViewById(R.id.from);
        EditText to = (EditText) findViewById(R.id.to);
        EditText keywords = (EditText) findViewById(R.id.keywords);
        Spinner sortby = (Spinner)findViewById(R.id.sortBy);

        sortby.setSelection(0);
        err.setText("");
        from.setText("");
        to.setText("");
        keywords.setText("");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

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


    public class request extends AsyncTask<String,Integer,Double> {
        String resultOutput;
        String keyword;

        @Override
        protected Double doInBackground(String... params) {



            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
                resultOutput = EntityUtils.toString(httpResponse.getEntity());
                Log.v("A",resultOutput);
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Double result) {
            try {
                JSONObject abc = new JSONObject(resultOutput);
                if(abc.getString("ack").equals("No results found"))
                {
                    TextView err = (TextView) findViewById(R.id.errorMsg);
                    err.setText("No results found");
                    return;
                }
            }
            catch(Exception e){e.printStackTrace();}
            EditText keywords = (EditText) findViewById(R.id.keywords);
            keyword = String.valueOf(keywords.getText());

            Intent intent = new Intent(MainActivity.this, resultActivity.class);
            intent.putExtra("Result", resultOutput);
            intent.putExtra("keyword",keyword);
            startActivity(intent);
        }
    }

}



