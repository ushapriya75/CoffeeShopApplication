package com.org.coffeeshop;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.org.coffeeshop.ApiCall.ZomatoApi;
import com.org.coffeeshop.ApiCall.ZomatoApiClient;
import com.org.coffeeshop.Utils.Constant;
import com.org.coffeeshop.Utils.DataFilterValues;
import com.org.coffeeshop.Utils.Preferences;
import com.org.coffeeshop.modal.Restaurant;
import com.org.coffeeshop.modal.ZometoResponse;
import com.org.coffeeshop.modal.nearbyResturants.NearbyResturants;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    int PERMISSION_ID = 44;
    private FusedLocationProviderClient mFusedLocationClient;
    private ArrayList<com.org.coffeeshop.modal.nearbyResturants.Restaurant> nearByResturantList = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private ShopAdapter mShopAdapter;
    ProgressDialog progressDoalog;
    List<Restaurant> restaurantList = new ArrayList<>();

    TextView mGreetingTextView;
    Button mRefreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

       // initializeData();
    }

    private void initializeData() {

        // check for one day cache
        if (Preferences.getIntPreferences(Constant.KEY_SAVED_DATE) != new Date(System.currentTimeMillis()).getDate() || Preferences.getStringPreferences(Constant.KEY_CACHED_DATA).equals("")) {
           // getRestorentData();
        } else {
            Type type = new TypeToken<List<Restaurant>>() {
            }.getType();

            List<Restaurant> restaurants = new Gson().fromJson(Preferences.getStringPreferences(Constant.KEY_CACHED_DATA), type);
            restaurantList.clear();
            restaurantList.addAll(restaurants);
        }
    }


    private void initView() {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        mRecyclerView = findViewById(R.id.shop_list_recycler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        /*mShopAdapter = new ShopAdapter(this, restaurantList, new ShopAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Restaurant item) {
                Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.RESTAURANT_INTENT, item.getRestaurant());
                detailIntent.putExtras(bundle);

                startActivity(detailIntent);
            }
        });
        mRecyclerView.setAdapter(mShopAdapter);*/

        mGreetingTextView = findViewById(R.id.greeting_text_view);
        mRefreshButton = findViewById(R.id.refresh_button);

        mGreetingTextView.setText(getString(R.string.hello_1_s, Preferences.getStringPreferences(Constant.KEY_SAVED_Name),
                Preferences.getStringPreferences(Constant.KEY_SAVED_Email)));

        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
            }
        });
    }

    private void getNearbyResturants(double longitude, double latitude) {

        ZomatoApi service = ZomatoApiClient.getInstance().create(ZomatoApi.class);
        Call<NearbyResturants> call = service.getNearByResturants(latitude, longitude);
        call.enqueue(new Callback<NearbyResturants>() {
            @Override
            public void onResponse(Call<NearbyResturants> call, Response<NearbyResturants> response) {
                NearbyResturants nearbyResturants = response.body();
                if (nearbyResturants != null){
                    Log.d("MYMSG", "result = " + response.body());
                    nearByResturantList.clear();
                    for (int i = 0 ; i < nearbyResturants.getNearbyRestaurants().size(); i ++){
                        nearByResturantList.add(nearbyResturants.getNearbyRestaurants().get(i).getRestaurant());
                    }
                    mShopAdapter = new ShopAdapter(MainActivity.this, nearByResturantList);
                    mRecyclerView.setAdapter(mShopAdapter);
                }else {
                    Toast.makeText(MainActivity.this, "No Resturant Found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<NearbyResturants> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
/*
    private void getRestorentData() {
        progressDoalog = new ProgressDialog(MainActivity.this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.setCancelable(false);
        progressDoalog.show();


        *//*Create handle for the RetrofitInstance interface*//*
        ZomatoApi service = ZomatoApiClient.getInstance().create(ZomatoApi.class);
        Call<ZometoResponse> call = service.getRestaurants(DataFilterValues.ENTITY_ID, DataFilterValues.ENTITY_TYPE, DataFilterValues.TOTAL_RECORD_COUNT, DataFilterValues.SORT_BY, DataFilterValues.SORT_ORDER, DataFilterValues.SEARCH_KEY);
        call.enqueue(new Callback<ZometoResponse>() {
            @Override
            public void onResponse(Call<ZometoResponse> call, Response<ZometoResponse> response) {

                progressDoalog.dismiss();
                ZometoResponse zometoResponse = (ZometoResponse) response.body();

                if (zometoResponse != null) {
                    restaurantList.clear();
                    restaurantList.addAll(zometoResponse.getRestaurants());

                    //save data in cache
                    Preferences.saveStringPreferencessync(Constant.KEY_CACHED_DATA, new Gson().toJson(zometoResponse.getRestaurants()));
                    Preferences.saveIntPreferencessync(Constant.KEY_SAVED_DATE, new Date(System.currentTimeMillis()).getDate());
                    mShopAdapter.notifyDataSetChanged();
                    mRecyclerView.smoothScrollToPosition(0);
                }
            }

            @Override
            public void onFailure(Call<ZometoResponse> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    private boolean checkPermissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }

    private void requestPermissions(){
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }
        }
    }

    private boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    getNearbyResturants(location.getLongitude(), location.getLatitude());
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            getNearbyResturants(mLastLocation.getLongitude(), mLastLocation.getLatitude());
        }
    };

    @Override
    public void onResume(){
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }

    }

}
