package com.imanancin.storyapp1.ui.maps

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.imanancin.storyapp1.R
import com.imanancin.storyapp1.ViewModelFactory
import com.imanancin.storyapp1.data.remote.Results
import com.imanancin.storyapp1.databinding.ActivityMapsBinding
import timber.log.Timber

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var boundsBuilder = LatLngBounds.Builder()
    private lateinit var binding: ActivityMapsBinding
    private val TAG = "MapsActivity"
    private val viewModel: MapsViewModel by viewModels {
        ViewModelFactory.getInstance(activity)
    }
    private lateinit var activity: MapsActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        activity = this

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setupViewModel()
        mapUiSettings()
//        setMapStyle()
        getMyLocation()


    }

    private fun setupViewModel() {
        viewModel.getData().observe(this) { result ->
            Timber.tag(TAG).e(result.toString())
            when (result) {
                is Results.Error -> {
                    Toast.makeText(activity, getString(R.string.error_load_data), Toast.LENGTH_SHORT).show()
                }
                is Results.Loading -> {

                }
                is Results.Success -> {
                    result.data?.listStory?.forEach { item ->
                        val latLng = LatLng(item.lat ?: 0.0, item.lon ?: 0.0)
                        mMap.addMarker(MarkerOptions()
                            .position(latLng)
                            .title(item.name)
                            .snippet(item.description))
                        boundsBuilder(latLng)
                    }
                }
            }
        }
    }

    private fun boundsBuilder(latLng: LatLng) {
        boundsBuilder.include(latLng)
        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )
    }

    private fun mapUiSettings() {
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true



    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Timber.tag(TAG).e("Map style json error load")
            }
        } catch (exception: Resources.NotFoundException) {
            Timber.tag(TAG).e("Map error: ${exception.message}")
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }
    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}