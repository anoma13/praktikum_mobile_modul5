package com.wahyuli.mymaps.kotlin

import android.content.Context
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import com.wahyuli.mymaps.R
import com.wahyuli.mymaps.kotlin.model.DirectionResponses
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var rumah: LatLng
    private lateinit var spbu: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rumah = LatLng(-2.512289, 112.932005)
        spbu = LatLng(-2.522409, 112.944217)

        val mapFragment = maps_view as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val markerRumah = MarkerOptions()
                .position(rumah)
                .title("Rumah Saya")
        val markerSPBU = MarkerOptions()
                .position(spbu)
                .title("SPBU SAMPIT")

        map.addMarker(markerRumah)
        map.addMarker(markerSPBU)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(spbu, 15f))

        val fromRumah = rumah.latitude.toString() + "," + rumah.longitude.toString()
        val toSPBU = spbu.latitude.toString() + "," + spbu.longitude.toString()

        val apiServices = RetrofitClient.apiServices(this)
        apiServices.getDirection(fromRumah, toSPBU, getString(R.string.api_key))
                .enqueue(object : Callback<DirectionResponses> {
                    override fun onResponse(call: Call<DirectionResponses>, response: Response<DirectionResponses>) {
                        drawPolyline(response)
                        Log.d("done!!!", response.message())
                    }

                    override fun onFailure(call: Call<DirectionResponses>, t: Throwable) {
                        Log.e("error!!!", t.localizedMessage)
                    }
                })

    }

    private fun drawPolyline(response: Response<DirectionResponses>) {
        val shape = response.body()?.routes?.get(0)?.overviewPolyline?.points
        val polyline = PolylineOptions()
                .addAll(PolyUtil.decode(shape))
                .width(8f)
                .color(Color.RED)
        map.addPolyline(polyline)
    }

    private interface ApiServices {
        @GET("maps/api/directions/json")
        fun getDirection(@Query("origin") origin: String,
                         @Query("destination") destination: String,
                         @Query("key") apiKey: String): Call<DirectionResponses>
    }

    private object RetrofitClient {
        fun apiServices(context: Context): ApiServices {
            val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(context.resources.getString(R.string.base_url))
                    .build()

            return retrofit.create<ApiServices>(ApiServices::class.java)
        }
    }
}
