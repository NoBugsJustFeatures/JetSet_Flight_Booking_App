package edu.birzeit.jetset.Fragments;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.birzeit.jetset.R;
import edu.birzeit.jetset.database.DataBaseHelper;
import edu.birzeit.jetset.database.SharedPrefManager;

public class HomeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DataBaseHelper dataBaseHelper;
    SharedPrefManager sharedPrefManager;
    LinearLayout cardContainer;
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPrefManager = SharedPrefManager.getInstance(getContext());
        sharedPrefManager.writeToolbarTitle("Home");


        dataBaseHelper = new DataBaseHelper(getContext());
        sharedPrefManager = SharedPrefManager.getInstance(getContext());
        cardContainer = view.findViewById(R.id.cardContainer);

        fillFlights();
    }

    @SuppressLint("SetTextI18n")
    public void fillFlights() {
        Cursor cursor = dataBaseHelper.getClosestFiveFlights();

        if (cursor != null && cursor.moveToFirst()) {
            int i = 0;
            do {
//                String flightId = cursor.getString(cursor.getColumnIndexOrThrow("FLIGHT_ID"));
                String flightNumber = cursor.getString(cursor.getColumnIndexOrThrow("FLIGHT_NUMBER"));
                String departureCity = cursor.getString(cursor.getColumnIndexOrThrow("DEPARTURE_CITY"));
                String destinationCity = cursor.getString(cursor.getColumnIndexOrThrow("DESTINATION_CITY"));
                String departureDateTime = cursor.getString(cursor.getColumnIndexOrThrow("DEPARTURE_DATETIME"));
                String arrivalDateTime = cursor.getString(cursor.getColumnIndexOrThrow("ARRIVAL_DATETIME"));

                View cardView = getLayoutInflater().inflate(R.layout.flight_card, cardContainer, false);

                TextView textViewFlightNumber = cardView.findViewById(R.id.textViewFlightNumber);
                TextView textViewFrom = cardView.findViewById(R.id.textViewFrom);
                TextView textViewDeparture = cardView.findViewById(R.id.textViewDeparture);
                TextView textViewTo = cardView.findViewById(R.id.textViewTo);
                TextView textViewArrival = cardView.findViewById(R.id.textViewArrival);

                textViewFlightNumber.setText(flightNumber);
                textViewFrom.setText(departureCity);
                textViewDeparture.setText("Departure on " + departureDateTime);
                textViewTo.setText(destinationCity);
                textViewArrival.setText("Arrival on " + arrivalDateTime);

                cardContainer.addView(cardView);

                i++;

            } while (cursor.moveToNext() && i < 5);

            cursor.close();
        }
    }
}