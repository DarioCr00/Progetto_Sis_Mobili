package it.unibo.demo.nearpharm

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.PendingResult
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode
import it.unibo.demo.nearpharm.BuildConfig.MAPS_API_KEY


class MapsActivity: AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geoApiContext: GeoApiContext

    companion object{
        private const val LOCATION_REQUEST_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        geoApiContext = GeoApiContext.Builder()
            .apiKey("${MAPS_API_KEY}")
            .build()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true

        mMap.setOnMarkerClickListener(this)

        setUpMap()
    }

    private fun setUpMap() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
            return
        }
        mMap.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->

            if(location !=null){
                lastLocation = location
                val currentLatLong = LatLng(location.latitude, location.longitude)
                placeMarkerOnMap(currentLatLong) //da modificare con il pallino
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 20f))
            }
        }

    }

    private fun placeMarkerOnMap(currentLatLong: LatLng) {
        val markerOptions = MarkerOptions().position(currentLatLong)
        markerOptions.title("$currentLatLong")
        mMap.addMarker((markerOptions))
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        val destination = marker?.position
        if (destination != null) {
            calculateDirections(destination)
        }
        return false
    }

    private fun calculateDirections(destination: LatLng) {
        val origin = "${lastLocation.latitude},${lastLocation.longitude}"
        val destinationStr = "${destination.latitude},${destination.longitude}"

        DirectionsApi.newRequest(geoApiContext)
            .mode(TravelMode.DRIVING) // Puoi cambiare il modo di viaggio (DRIVING, WALKING, TRANSIT, ecc.)
            .origin(origin)
            .destination(destinationStr)
            .setCallback(object : PendingResult.Callback<DirectionsResult> {
                override fun onResult(result: DirectionsResult?) {
                    // Processa il risultato delle indicazioni del percorso
                    if (result != null && result.routes.isNotEmpty()) {
                        val route = result.routes[0]
                        val distance = route.legs[0].distance
                        val duration = route.legs[0].duration

                        // Aggiungi qui la logica per visualizzare la distanza e la durata del percorso all'utente
                    }
                }

                override fun onFailure(e: Throwable?) {
                    // Gestisci l'errore di richiesta delle indicazioni del percorso
                }
            })
    }

}


