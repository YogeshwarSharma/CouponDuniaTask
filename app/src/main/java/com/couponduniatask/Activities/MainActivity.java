package com.couponduniatask.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.couponduniatask.R;
import com.couponduniatask.adapters.RecyclerAdapter;
import com.couponduniatask.network.ApiConfig;
import com.couponduniatask.network.Response.GetResponse;
import com.couponduniatask.utils.ItemDecoratorRecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener {
    private RecyclerView recyclerView;
    private RecyclerAdapter mRecyclerAdapter;
    private ProgressDialog mProgressDialog;
    private Location location;
    private double latitude = 21;//India
    private double longitude = 78;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        fab.setVisibility(View.GONE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getData();
    }

    private void getData() {
        showProgressDialog();
        RestAdapter restAdapter = buildAdapter();
        ApiConfig login = restAdapter.create(ApiConfig.class);
        login.getdata(new Callback<GetResponse.ApiResponse>() {
            @Override
            public void success(GetResponse.ApiResponse apiResponse, Response response2) {
                hideProgressDialog();
                setView(apiResponse.data);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                hideProgressDialog();
            }
        });
    }

    private void setView(List<GetResponse.Data> data) {


        if (data == null || data.size() == 0) {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isGPSTrackingEnabled = getLocation(this);

        if (!isGPSTrackingEnabled) {
            Toast.makeText(this, "Current Location Not Found", Toast.LENGTH_SHORT).show();
        }

        // if Current Lat Long are not set then sorted would be done with India Lat Long.
        sortData(data, latitude, longitude);

        mRecyclerAdapter = new RecyclerAdapter(data, latitude, longitude);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        final ItemDecoratorRecyclerView itemDecoratorRecyclerView = new ItemDecoratorRecyclerView(30, 0, 0, 0);

        recyclerView.addItemDecoration(itemDecoratorRecyclerView);

        recyclerView.setAdapter(mRecyclerAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void showProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();
    }

    protected void hideProgressDialog() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        } catch (Exception e) {

        }
    }

    public static final String API_BASE_URL = "http://staging.couponapitest.com";

    public RestAdapter buildAdapter() {
        return new RestAdapter.Builder()
                .setEndpoint(API_BASE_URL)
                .build();

    }

    public static List<GetResponse.Data> sortData(List<GetResponse.Data> data, final double myLatitude, final double myLongitude) {
        Comparator comp = new Comparator<GetResponse.Data>() {
            @Override
            public int compare(GetResponse.Data comparator, GetResponse.Data comparator2) {
                float[] result1 = new float[3];
                double lat_1 = Double.parseDouble(comparator.Latitude);
                double long_1 = Double.parseDouble(comparator.Longitude);
                android.location.Location.distanceBetween(myLatitude, myLongitude, lat_1, long_1, result1);
                Float distance1 = result1[0];

                float[] result2 = new float[3];
                double lat_2 = Double.parseDouble(comparator2.Latitude);
                double long_2 = Double.parseDouble(comparator2.Longitude);
                android.location.Location.distanceBetween(myLatitude, myLongitude, lat_2, long_2, result2);
                Float distance2 = result2[0];

                return distance1.compareTo(distance2);
            }
        };


        Collections.sort(data, comp);
        return data;
    }

    public boolean getLocation(Context mContext) {

        String provider_info = null;
        try {
            LocationManager locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (isGPSEnabled) {
                provider_info = LocationManager.GPS_PROVIDER;
            } else if (isNetworkEnabled) {
                provider_info = LocationManager.NETWORK_PROVIDER;
            }

            if (provider_info == null) {
                return false;
            }
            if (!provider_info.isEmpty()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        return false;
                    }
                }
                locationManager.requestLocationUpdates(
                        provider_info,
                        (1000 * 60 * 1),
                        10,
                        this
                );

                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(provider_info);
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Log.d("MainActivity", "Hello MainActivity.getLocation    " + latitude + "  " + longitude);
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
        }

        return false;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
