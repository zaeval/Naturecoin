package muzimuzi.jejuhackerton.com.muzimuzi.Fragment;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.util.List;

import muzimuzi.jejuhackerton.com.muzimuzi.LogActivity;
import muzimuzi.jejuhackerton.com.muzimuzi.MainActivity;
import muzimuzi.jejuhackerton.com.muzimuzi.R;
import muzimuzi.jejuhackerton.com.muzimuzi.util.SharedPreferencesManager;
import muzimuzi.jejuhackerton.com.muzimuzi.util.Util;

public class HomeFragment extends Fragment implements View.OnClickListener,OnMapReadyCallback{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static GoogleMap mMap;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RelativeLayout pointBanner;
    public static TextView ntc;
    static public Marker marker=null;
    private ScrollView scrollView;
    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        pointBanner = (RelativeLayout) rootView.findViewById(R.id.point_banner);
        pointBanner.setOnClickListener(this);
        ntc = (TextView) rootView.findViewById(R.id.ntc_tv);
        scrollView = (ScrollView) rootView.findViewById(R.id.scroll);
        scrollView.requestDisallowInterceptTouchEvent(true);
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager();
        float sum = sharedPreferencesManager.getFloat(LogActivity.CURRENT_NTC,getContext());
        ntc.setText(sum +" NTC");

        return rootView;
    }


    @Override
    public void onClick(View view) {
        if(view == pointBanner){
            ((AppCompatActivity)getActivity()).startActivity(new Intent(getContext(),LogActivity.class));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mMap != null){
            SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager();
            float lat = sharedPreferencesManager.getFloat(SendFragment.SEND_LAT,getContext());
            float lng = sharedPreferencesManager.getFloat(SendFragment.SEND_LNG,getContext());
            LatLng latLng = new LatLng(lat,lng);

            if(lat == 0 && lng == 0){
                if(MainActivity.nowLocation!=null)
                    latLng = MainActivity.nowLocation;
            }
            else {
                if(!(marker.getPosition().latitude == lat && marker.getPosition().longitude== lng)) {
                    if (marker != null)
                        marker.remove();
                    marker = mMap.addMarker(new MarkerOptions().position(latLng).title("recently transaction location"));
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager();
        float lat = sharedPreferencesManager.getFloat(SendFragment.SEND_LAT,getContext());
        float lng = sharedPreferencesManager.getFloat(SendFragment.SEND_LNG,getContext());
        LatLng latLng = new LatLng(lat,lng);

        if(lat == 0 && lng == 0){
            if(MainActivity.nowLocation!=null)
                latLng = MainActivity.nowLocation;
        }
        else {
            if(marker !=null)
                marker.remove();
            marker = mMap.addMarker(new MarkerOptions().position(latLng).title("recently transaction location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));
        }
        getAddress(latLng.latitude,latLng.longitude,getContext());

    }
    public List<Address> getAddress(double latitude, double longitude,Context context) {
        List<Address> addresses=null;
        if (latitude != 0 && longitude != 0) {
            try {
                Geocoder geocoder = new Geocoder(context);
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getAddressLine(1);
                String country = addresses.get(0).getAddressLine(2);
                Log.d("TAG", "address = " + address + ", city = " + city + ", country = " + country);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "latitude and longitude are null", Toast.LENGTH_LONG).show();
        }

        return addresses;
    }
}
