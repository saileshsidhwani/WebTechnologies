package com.example.sailesh.hw9_ebay;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.InputStream;

import com.facebook.*;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;


public class itemActivity extends ActionBarActivity {

    String url = null;
    ShareDialog shareDialog;
    CallbackManager callbackManager;
    String Discription = null;
    String Title = null;
    String pictureURLSuperSize = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Button btnA = (Button)findViewById(R.id.BasicInfo);
        btnA.setBackgroundColor(Color.YELLOW);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog =new ShareDialog(this);

        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
// successful so call the share async task
                if(result.getPostId() != null) {
                    Toast.makeText(getBaseContext(),
                            "Success, Post ID:"+result.getPostId(),
                            Toast.LENGTH_SHORT).show();
                }

                else if(result.getPostId() == null)
                {
                    Toast.makeText(getBaseContext(),
                            "Post Cancelled",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancel() {
                Toast.makeText(getBaseContext(),
                        "Post Cancelled",
                        Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onError(FacebookException error) {

            }

        });


        TextView t;
        ImageView i;
        JSONObject itemDetails,basicInfo,sellerInfo,shippingInfo;
        String title = null;
        String price = null;
        String location = null;
        String topRated = null;
        String shipping;
        String store = null;
        String expeditedShipping=null;
        String oneDayShipping=null;
        String returnsExcepted = null;
        Double shippingCost = null;
        Intent intent = getIntent();
        ImageView image = (ImageView)findViewById(R.id.imageView);

        TableLayout A = (TableLayout)findViewById(R.id.TableA);
        try {
            itemDetails = new JSONObject(intent.getStringExtra("ItemDetails"));
            basicInfo = itemDetails.getJSONObject("basicInfo");
            sellerInfo = itemDetails.getJSONObject("sellerInfo");
            shippingInfo = itemDetails.getJSONObject("shippingInfo");

            pictureURLSuperSize = basicInfo.getString("pictureURLSuperSize");
            title = basicInfo.getString("title");
            Title = title;
            price = basicInfo.getString("convertedCurrentPrice");
            location = basicInfo.getString("location");
            shipping = basicInfo.getString("shippingServiceCost");

            if(shipping.trim().length()==0)
                shippingCost = 0.0;
            else
                shippingCost = Double.parseDouble(shipping);
            topRated = basicInfo.getString("topRatedListing");
            url = basicInfo.getString("viewItemURL");
            Discription = "Price: $"+price;
            if(shippingCost == 0.0 || shipping.trim().length() == 0)
                Discription += "(free shipping)";
            else
                Discription += " (+ $" + shippingCost + " for shipping)";
            Discription += ", Location: "+location;


            t = (TextView)findViewById(R.id.CategoryName);
            t.setText(basicInfo.getString("categoryName"));
            t = (TextView)findViewById(R.id.Condition);
            t.setText(basicInfo.getString("conditionDisplayName"));
            t = (TextView)findViewById(R.id.BuyingFormat);
            t.setText(basicInfo.getString("listingType"));

            t = (TextView)findViewById(R.id.UserName);
            t.setText(sellerInfo.getString("sellerUserName"));
            t = (TextView)findViewById(R.id.FeedbackScore);
            t.setText(sellerInfo.getString("feedbackScore"));
            t = (TextView)findViewById(R.id.PositiveFeedback);
            t.setText(sellerInfo.getString("positiveFeedbackPercent")+"%");
            t = (TextView)findViewById(R.id.FeedbackRating);
            t.setText(sellerInfo.getString("feedbackRatingStar"));
            t = (TextView)findViewById(R.id.Store);
            store = sellerInfo.getString("sellerStoreName");
            if(store.trim().length() == 0)
                t.setText("N/A");
            else
                t.setText(sellerInfo.getString("sellerStoreName"));

            t = (TextView)findViewById(R.id.ShippingType);
            t.setText(shippingInfo.getString("shippingType"));
            t = (TextView)findViewById(R.id.HandlingTime);
            t.setText(shippingInfo.getString("handlingTime")+"day(s)");
            t = (TextView)findViewById(R.id.ShippingLocations);
            t.setText(shippingInfo.getString("shipToLocations"));

            i = (ImageView)findViewById(R.id.ExpeditedShipping);
            expeditedShipping = shippingInfo.getString("expeditedShipping");
            if(expeditedShipping.equals("true"))
                i.setImageResource(R.drawable.right);
            else
                i.setImageResource(R.drawable.wrong);

            i = (ImageView)findViewById(R.id.OneDayShipping);
            oneDayShipping = shippingInfo.getString("oneDayShippingAvailable");
            if(oneDayShipping.equals("true"))
                i.setImageResource(R.drawable.right);
            else
                i.setImageResource(R.drawable.wrong);

            i = (ImageView)findViewById(R.id.ReturnsAccepted);
            returnsExcepted = shippingInfo.getString("returnsAccepted");
            if(returnsExcepted.equals("true"))
                i.setImageResource(R.drawable.right);
            else
                i.setImageResource(R.drawable.wrong);

            i = (ImageView)findViewById(R.id.TopRatedImage);
            if(topRated.equals("true"))
                i.setImageResource(R.drawable.right);
            else
                i.setImageResource(R.drawable.wrong);



        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        new DownloadFile(image).execute(pictureURLSuperSize);
        t = (TextView)findViewById(R.id.Title);
        t.setText(title);
        t = (TextView)findViewById(R.id.Price);
        if(shippingCost > 0.0)
           t.setText("Price: $"+price);
        else
            t.setText("Price: $"+price +"(free shipping)");
           t = (TextView)findViewById(R.id.Location);
        t.setText(location);
        if(topRated.equals("true"))
        {
            i =  (ImageView)findViewById(R.id.TopRated);
            i.setVisibility(View.VISIBLE);
        }







    }

    public void shareFB(View view)
    {

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setImageUrl(Uri.parse(pictureURLSuperSize))
                    .setContentTitle(Title)
                    .setContentDescription(Discription)
                    .setContentUrl(Uri.parse(url))
                    .build();

            shareDialog.show(linkContent, ShareDialog.Mode.FEED);

        }
    }

    public void displayTableA(View view) {
        TableLayout A = (TableLayout) findViewById(R.id.TableA);
        TableLayout B = (TableLayout) findViewById(R.id.TableB);
        TableLayout C = (TableLayout) findViewById(R.id.TableC);
        A.setVisibility(View.VISIBLE);
        B.setVisibility(View.INVISIBLE);
        C.setVisibility(View.INVISIBLE);

        Button btnA = (Button)findViewById(R.id.BasicInfo);
        Button btnB = (Button)findViewById(R.id.SellerInfo);
        Button btnC = (Button)findViewById(R.id.ShippingInfo);
        btnA.setBackgroundColor(Color.YELLOW);
        btnB.setBackgroundColor(Color.LTGRAY);
        btnC.setBackgroundColor(Color.LTGRAY);
    }

    public void displayTableB(View view) {
        TableLayout A = (TableLayout) findViewById(R.id.TableA);
        TableLayout B = (TableLayout) findViewById(R.id.TableB);
        TableLayout C = (TableLayout) findViewById(R.id.TableC);
        A.setVisibility(View.INVISIBLE);
        B.setVisibility(View.VISIBLE);
        C.setVisibility(View.INVISIBLE);

        Button btnA = (Button)findViewById(R.id.BasicInfo);
        Button btnB = (Button)findViewById(R.id.SellerInfo);
        Button btnC = (Button)findViewById(R.id.ShippingInfo);
        btnA.setBackgroundColor(Color.LTGRAY);
        btnB.setBackgroundColor(Color.YELLOW);
        btnC.setBackgroundColor(Color.LTGRAY);
    }

    public void displayTableC(View view) {
        TableLayout A = (TableLayout) findViewById(R.id.TableA);
        TableLayout B = (TableLayout) findViewById(R.id.TableB);
        TableLayout C = (TableLayout) findViewById(R.id.TableC);
        A.setVisibility(View.INVISIBLE);
        B.setVisibility(View.INVISIBLE);
        C.setVisibility(View.VISIBLE);

        Button btnA = (Button)findViewById(R.id.BasicInfo);
        Button btnB = (Button)findViewById(R.id.SellerInfo);
        Button btnC = (Button)findViewById(R.id.ShippingInfo);
        btnA.setBackgroundColor(Color.LTGRAY);
        btnB.setBackgroundColor(Color.LTGRAY);
        btnC.setBackgroundColor(Color.YELLOW);
    }

    public void openLink(View view)
    {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);
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
