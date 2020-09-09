package com.org.coffeeshop;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.org.coffeeshop.Utils.Constant;
import com.org.coffeeshop.modal.Restaurant_;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    private Restaurant_ restaurant;

    TextView mShopName, mAddress, mLink;
    RatingBar mRating;
    ImageView mShopFeaturedImage;
    ImageButton mShareButton;

    private String name, address, featuredImage, link, rating, aggragateRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_screen);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        name = getIntent().getStringExtra("name");
        address = getIntent().getStringExtra("address");
        featuredImage = getIntent().getStringExtra("featuredImage");
        link = getIntent().getStringExtra("link");
        rating = getIntent().getStringExtra("rating");
        aggragateRating = getIntent().getStringExtra("aggregateRating");


        initVeiws();
    }

    private void initVeiws() {

        mShopName = findViewById(R.id.shop_name_text_view);
        mAddress = findViewById(R.id.address_text_view);
        mRating = findViewById(R.id.rating_bar);
        mLink = findViewById(R.id.see_more_textView);
        mShopFeaturedImage = findViewById(R.id.detail_screen_shop_featured_image_view);
        mShareButton = findViewById(R.id.share_image_button);


        mLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webview = new Intent(DetailActivity.this, WebViewActivity.class);

                webview.putExtra(Constant.WEB_LINK, link);

                startActivity(webview);
            }
        });

        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");

                    String shareMessage = "\nCheckout the Restaurant :-\n\n";
                    shareMessage = shareMessage + link;
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "Share with"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mShopName.setText(name);
        mAddress.setText(address);
        mRating.setRating(Float.parseFloat(aggragateRating));
        //mRating.setRating(Float.parseFloat(restaurant.getUserRating().getAggregateRating()));

        if (featuredImage != null && !featuredImage.equals(""))
            Picasso.with(this).load(featuredImage).
                    fit()
                    .into(mShopFeaturedImage);
    }
}
