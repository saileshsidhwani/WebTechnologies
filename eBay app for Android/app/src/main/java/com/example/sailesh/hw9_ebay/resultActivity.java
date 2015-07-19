package com.example.sailesh.hw9_ebay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;


public class resultActivity extends ActionBarActivity {

    ListView lv ;
    JSONArray itemarray;

    private Context context;
    ArrayList<HashMap<String, String>> jsonlist = new ArrayList<HashMap<String, String>>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        JSONObject abc;

        JSONObject item;
        JSONObject basicInfo;
        String a,b,c,shipping;
        Double cPrice,shippingCost;
        String j=intent.getStringExtra("Result");
        try {
            abc = new JSONObject(j);
            itemarray = abc.getJSONArray("item1");
            for(int i=0;i<5;i++) {
                item = (JSONObject) itemarray.get(i);
                basicInfo = item.getJSONObject("basicInfo");
                a = basicInfo.getString("galleryURL");
                b = basicInfo.getString("title");
                c = basicInfo.getString("convertedCurrentPrice");
                shipping=basicInfo.getString("shippingServiceCost");
                if(shipping.trim().length()==0)
                    shippingCost = 0.0;
                else
                    shippingCost = Double.parseDouble(shipping);
                if(shippingCost > 0.0)
                    c = "Price: $"+c + (" (+ $"+shippingCost + " for shipping)");
                else
                    c = "Price: $"+c +"(free shipping)";
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("image",a);
                map.put("title", b);
                map.put("price", c);
                jsonlist.add(map);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        String keyword = intent.getStringExtra("keyword");
        Log.v("keyword",keyword);
        TextView keywords = (TextView) findViewById(R.id.searchKeyword);
        keywords.setText("Results for '" + keyword + "'");

        String image = "image";
        String title = "title";
        String price = "price";


        ListAdapter adapter = new SimpleAdapter(this, jsonlist, R.layout.row, new String[] {image,title, price }, new int[] {R.id.itemImage,R.id.itemTitle,R.id.itemPrice }){
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = ((Activity)resultActivity.this).getLayoutInflater();
                convertView = inflater.inflate(R.layout.row,parent,false);

                ImageView image = (ImageView)convertView.findViewById(R.id.itemImage);
                TextView titleTextView = (TextView)convertView.findViewById(R.id.itemTitle);
                TextView priceTextView = (TextView)convertView.findViewById(R.id.itemPrice);
                titleTextView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v)
                    {
                        String itemDetails = null;
                        try {
                            itemDetails = itemarray.get(position).toString();
                        }
                        catch (Exception e){e.printStackTrace();}

                        Intent intent = new Intent(resultActivity.this, itemActivity.class);
                        intent.putExtra("ItemDetails", itemDetails);
                        startActivity(intent);
                    }
                });

                image.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        JSONObject item, basic;
                        String url=null;
                        try {
                            item = (JSONObject) itemarray.get(position);
                            basic = item.getJSONObject("basicInfo");
                            url = basic.getString("viewItemURL");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browserIntent);
                    }
                });



                HashMap<String,String> mapTemp = (HashMap)this.getItem(position);
                titleTextView.setText(mapTemp.get("title"));
                priceTextView.setText(mapTemp.get("price"));
                new DownloadFile(image).execute(mapTemp.get("image"));



                return convertView;
            }
        };
        lv= (ListView)findViewById(R.id.listView);
        lv.setAdapter(adapter);

        return;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
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


    private class DownloadFile extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadFile(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap thumbnail = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                thumbnail = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return thumbnail;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
