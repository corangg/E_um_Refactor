package com.app.ui.activity

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.app.BuildConfig
import com.app.databinding.ActivityMapBinding
import com.core.ui.BaseActivity
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.OnMapReadyCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapActivity : BaseActivity<ActivityMapBinding>(ActivityMapBinding::inflate), OnMapReadyCallback {
    private lateinit var mapView: MapView
    private var naverMap: NaverMap? = null

    override fun setUi() {
        bindingOnClick()
        setMap()
    }

    override fun setUpDate() {


    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {

    }

    private fun bindingOnClick() {
        binding.btnMyLocation.setOnClickListener {
        }

    }

    private fun setMap(){
        NaverMapSdk.getInstance(this).client = NaverMapSdk.NcpKeyClient(BuildConfig.NAVER_MAP_CLIENT_ID)
        mapView = binding.mapView
        mapView.onCreate(null)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(naverMap: NaverMap) {
        Log.d("MapActivity", "onMapReady() called")
        this.naverMap = naverMap

        // 서울 중심으로 이동
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(37.5665, 126.9780))
        naverMap.moveCamera(cameraUpdate)
        Log.d("MapActivity", "카메라 위치를 서울로 이동")
    }

    override fun onStart() {
        super.onStart()
        Log.d("MapActivity", "onStart()")
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        Log.d("MapActivity", "onResume()")
        mapView.onResume()
    }

    override fun onPause() {
        Log.d("MapActivity", "onPause()")
        mapView.onPause()
        super.onPause()
    }

    override fun onStop() {
        Log.d("MapActivity", "onStop()")
        mapView.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        Log.d("MapActivity", "onDestroy()")
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        Log.d("MapActivity", "onLowMemory()")
        mapView.onLowMemory()
        super.onLowMemory()
    }
}