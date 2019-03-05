package com.isi.pfe.bank_app.Fragments;

import android.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.isi.pfe.bank_app.Classes.GPSTracker;
import com.isi.pfe.bank_app.R;

import java.util.ArrayList;


public class Find_ATM extends Fragment implements OnMapReadyCallback{
    Toolbar toolbar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_find_atm, container, false);
        toolbar = (Toolbar) myInflatedView.findViewById(R.id.toolbaratm);
        return myInflatedView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.drawer_item_find_atm));
        FragmentManager fragment = getActivity().getFragmentManager();
        MapFragment mf = (MapFragment) fragment.findFragmentById(R.id.map);
        mf.getMapAsync(this);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MapFragment f = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);
        if (f != null)
            getActivity().getFragmentManager().beginTransaction().remove(f).commit();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        String[] mapItem1 = new String[]{"BFI","BFI Group","36.837386","10.234462","ATM"};
        String[] mapItem2 = new String[]{"ISI","Inst.Sup.Info","36.861381","10.188776","Bank"};
        ArrayList<String[]> listMap = new ArrayList<>(2);
        listMap.add(mapItem1);
        listMap.add(mapItem2);
        for (String[] mapItem : listMap) {
            LatLng pos = new LatLng(Double.parseDouble(mapItem[2]),Double.parseDouble(mapItem[3]));
            BitmapDescriptor icon;
            if(mapItem[4].equals("Bank"))
                icon = BitmapDescriptorFactory.fromResource(R.drawable.bank);
            else
                icon = BitmapDescriptorFactory.fromResource(R.drawable.atm);
            googleMap.addMarker(new MarkerOptions().title(mapItem[0]).snippet(mapItem[1]).position(pos).icon(icon));

        }
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setTrafficEnabled(true);
        googleMap.setMyLocationEnabled(true);
        GPSTracker gps = new GPSTracker(getContext());
        if(gps.canGetLocation()) {
            LatLng mypos = new LatLng(gps.getLatitude(),gps.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mypos, 16));
        }
        else{
            gps.showSettingsAlert();
        }
    }


}