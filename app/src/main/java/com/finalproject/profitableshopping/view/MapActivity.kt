package com.finalproject.profitableshopping.view

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.core.app.ActivityCompat
import com.finalproject.profitableshopping.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import java.util.*

class MapActivity : AppCompatActivity(),OnMapReadyCallback {
    private var mMap: GoogleMap? = null
    private lateinit var databaseReference: DatabaseReference
    lateinit var clickButton : Button


    //   private lateinit var locationListener: LocationListener
    private var locationManager: LocationManager? = null
    private val MIN_TIME: Long = 1000
    private val MIN_DIST: Long = 5

    private var editTextLatitude: EditText? = null
    private var editTextLongitude: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        clickButton = findViewById(R.id.button)
        clickButton.setOnClickListener {
            databaseReference.child("latitude").push().setValue(editTextLatitude?.getText().toString());
            databaseReference.child("longitude").push().setValue(editTextLongitude?.getText().toString());
        }
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            PackageManager.PERMISSION_GRANTED)
        editTextLatitude = findViewById(R.id.editText);
        editTextLongitude = findViewById(R.id.editText2);

        databaseReference = FirebaseDatabase.getInstance().getReference("Location")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //   val latitude= dataSnapshot.getValue()
                try {
                    val databaseLatitudeString =
                        dataSnapshot.child("latitude").value.toString()
                            .substring(
                                1, dataSnapshot.child("latitude")
                                    .value.toString().length - 1
                            )
                    val databaseLongitudedeString = dataSnapshot.child("longitude")
                        .value.toString().substring(
                            1, dataSnapshot.child("longitude")
                                .value.toString().length - 1
                        )
                    val stringLat = databaseLatitudeString.split(", ".toRegex()).toTypedArray()
                    Arrays.sort(stringLat)
                    val latitude = stringLat[stringLat.size - 1].split("=".toRegex()).toTypedArray()[1]
                    val stringLong = databaseLongitudedeString.split(", ".toRegex()).toTypedArray()
                    Arrays.sort(stringLong)
                    val longitude = stringLong[stringLong.size - 1].split("=".toRegex()).toTypedArray()[1]
                    val latLng = LatLng(latitude.toDouble(), longitude.toDouble())
                    mMap!!.addMarker(MarkerOptions().position(latLng).title("$latitude , $longitude"))
                    mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value

            }
        })

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        var locationListener: LocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                try {
                    editTextLatitude!!.setText(java.lang.Double.toString(location.latitude))
                    editTextLongitude!!.setText(java.lang.Double.toString(location.longitude))
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {

            }

            override fun onProviderEnabled(s: String) {

            }


            override fun onProviderDisabled(s: String) {

            }

        }

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        try {
            locationManager!!.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                MIN_TIME,
                MIN_DIST.toFloat(),
                locationListener
            )
            locationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_TIME,
                MIN_DIST.toFloat(),
                locationListener
            )
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}