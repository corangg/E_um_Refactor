package com.app.ui.activity

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.BuildConfig
import com.app.R
import com.app.databinding.ActivityMapBinding
import com.app.ui.adapter.SearchListAdapter
import com.core.ui.BaseActivity
import com.core.util.changeCoordinate
import com.core.util.hideKeyboard
import com.domain.model.PlaceItem
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.presentation.MapActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapActivity : BaseActivity<ActivityMapBinding>(ActivityMapBinding::inflate),
    OnMapReadyCallback {
    private lateinit var mapView: MapView
    private lateinit var locationSource: FusedLocationSource
    private var naverMap: NaverMap? = null
    private lateinit var marker: Marker

    private val viewModel: MapActivityViewModel by viewModels()
    private val adapter by lazy { SearchListAdapter() }

    override fun setUi() {
        binding.viewModel = viewModel
        bindingOnClick()
        bindingRecyclerView()
        setMap()
    }

    override fun setUpDate() {}

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
        viewModel.coordinateLiveData.observe(lifecycleOwner, ::moveCoordinate)
        viewModel.getSelectPlace.observe(lifecycleOwner, ::showDetailView)
        viewModel.getSearchPlaceList.observe(lifecycleOwner, ::updateSearchList)
    }

    private fun initLocation() {
        val startAddress = intent.getStringExtra(getString(R.string.map_extra_start_address_key))
        if (startAddress != null) {
            viewModel.getCoordinateToAddress(startAddress)
        } else {
            enableMyLocation()
        }
    }

    private fun bindingOnClick() {
        binding.btnBackActivity.setOnClickListener { finish() }
        binding.btnSearch.setOnClickListener {
            binding.recyclerSearchList.visibility = View.VISIBLE
            binding.viewDetail.visibility = View.GONE
            val location = getCurrentLocation() ?: return@setOnClickListener
            viewModel.searchKeyword(location)
            hideKeyboard(binding.editSearch)
        }
        binding.btnMyLocation.setOnClickListener {
            enableMyLocation()
        }
        binding.btnCancel.setOnClickListener {
            binding.viewDetail.visibility = View.GONE
        }
        binding.btnOk.setOnClickListener {
            val selectAddress = binding.textAddress.text.toString()
            val resultIntent = Intent()
            resultIntent.putExtra(getString(R.string.map_search_address_key), selectAddress)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun enableMyLocation() {
        naverMap?.locationSource = locationSource
        naverMap?.locationTrackingMode = LocationTrackingMode.Follow
    }

    private fun setMap() {
        NaverMapSdk.getInstance(this).client = NaverMapSdk.NcpKeyClient(BuildConfig.NAVER_MAP_CLIENT_ID)

        mapView = binding.mapView
        mapView.onCreate(null)
        mapView.getMapAsync(this)
        locationSource = FusedLocationSource(this, 1024)
    }

    private fun moveCoordinate(coordinate: Pair<Double, Double>?) {
        coordinate ?: return
        val location = LatLng(coordinate.second, coordinate.first)
        val cameraUpdate = CameraUpdate.scrollTo(location)
        naverMap?.moveCamera(cameraUpdate)

        setMarker(location)
    }

    private fun setMarker(location:LatLng){
        if (::marker.isInitialized) {
            marker.position = location
        } else {
            marker = Marker().apply {
                position = location
                map = naverMap
            }
        }
    }

    private fun onMapClick(naverMap: NaverMap) {
        naverMap.setOnMapClickListener { pointF, latLng ->
            val latitude = latLng.latitude
            val longitude = latLng.longitude
            binding.viewDetail.visibility = View.GONE
            binding.recyclerSearchList.visibility = View.GONE

            setMarker(latLng)
            val currentLocation = getCurrentLocation() ?: return@setOnMapClickListener
            viewModel.getAddressToCoordinate(
                latitude.toString(),
                longitude.toString(),
                currentLocation
            )
        }
    }

    private fun showDetailView(placeItem: PlaceItem) {
        if (placeItem.title != "") {
            binding.viewDetail.visibility = View.VISIBLE
            binding.textTitle.text = placeItem.title
            binding.textAddress.text = placeItem.address

            if (placeItem.distance >= 1000) {
                val km = "${(placeItem.distance / 1000).toInt()}km"
                binding.textDistance.text = km
            } else {
                val m = "${placeItem.distance.toInt()}m"
                binding.textDistance.text = m
            }
        }
    }

    private fun bindingRecyclerView() {
        binding.recyclerSearchList.apply {
            layoutManager = LinearLayoutManager(this@MapActivity, LinearLayoutManager.VERTICAL, false)
            adapter = this@MapActivity.adapter
        }
        adapter.setOnItemClickListener { item, position ->
            binding.recyclerSearchList.visibility = View.GONE
            showDetailView(item)
            val coordinate = Pair(changeCoordinate(item.x), changeCoordinate(item.y))
            moveCoordinate(coordinate)
        }
    }

    private fun updateSearchList(list: List<PlaceItem>) {
        adapter.submitList(list)
    }

    private fun getCurrentLocation(): Pair<Double, Double>? {
        if (naverMap?.locationTrackingMode == LocationTrackingMode.None) {
            naverMap?.locationTrackingMode = LocationTrackingMode.NoFollow
        }
        val location = locationSource.lastLocation
        return location?.let { Pair(it.latitude, it.longitude) }
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        naverMap.locationSource = locationSource
        initLocation()
        onMapClick(naverMap)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onStop() {
        mapView.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        mapView.onLowMemory()
        super.onLowMemory()
    }
}