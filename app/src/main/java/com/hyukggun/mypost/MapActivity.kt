package com.hyukggun.mypost

import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.PermissionChecker
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.naver.maps.map.OnMapReadyCallback
import com.hyukggun.mypost.databinding.ActivityMapBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap

class MapActivity : AppCompatActivity(), LocationListener, OnMapReadyCallback {

    private lateinit var binding: ActivityMapBinding

    private var naverMap: NaverMap? = null

    private var lastLocation: Location? = null

    private var locationManger: LocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tagsRecyclerView = binding.rvMap
        val tags = listOf("전체", "서촌", "북촌")
        tagsRecyclerView.adapter = MapTagAdapter(tags)
        tagsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        locationManger = getSystemService(LOCATION_SERVICE) as LocationManager?
        val fragment = binding.mapView.getFragment<MapFragment>()
        fragment.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    override fun onStart() {
        super.onStart()
        if (hasPermission()) {
            Log.d("MapActivity", "hasPermission")
            locationManger?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1f, this)
            locationManger?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1f, this)
            lastLocation = locationManger?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            lastLocation?.let {
                Log.d("MapActivity", "lastLocation $it")
            }
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        naverMap = null
    }

    @SuppressLint("missingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (hasPermission()) {
                Log.d("MapActivity", "hasPermssion")
                locationManger?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1f, this)
            }
           return
       }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onLocationChanged(location: Location) {
        Log.d("MapActivity", "onLocationChanged : $location")
        naverMap?.let {
            var coord = LatLng(location)

            val locationOverlay = it.locationOverlay
            locationOverlay.isVisible = true
            locationOverlay.position = coord
            locationOverlay.bearing = location.bearing

            it.moveCamera(CameraUpdate.scrollTo(coord))
        }
    }

    private inline fun hasPermission(): Boolean {
        return PermissionChecker.checkSelfPermission(this, PERMISSIONS[0]) ==
                PermissionChecker.PERMISSION_GRANTED &&
                PermissionChecker.checkSelfPermission(this, PERMISSIONS[1]) ==
                PermissionChecker.PERMISSION_GRANTED
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
        private val PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        if (lastLocation == null) { return }
        naverMap?.let {
            val coord = LatLng(lastLocation!!)
            val locationOverlay = it.locationOverlay
            locationOverlay.isVisible = true
            locationOverlay.position = coord
            locationOverlay.bearing = lastLocation!!.bearing

            it.moveCamera(CameraUpdate.scrollTo(coord))
        }
    }
}