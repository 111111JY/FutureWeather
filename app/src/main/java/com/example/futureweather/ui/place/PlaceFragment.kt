package com.example.futureweather.ui.place

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.futureweather.MainActivity
import com.example.futureweather.R
import com.example.futureweather.extension.showToast
import com.example.futureweather.ui.weather.WeatherActivity
import kotlinx.android.synthetic.main.fragment_place.*

class PlaceFragment : Fragment() {
    val viewModel by lazy { ViewModelProviders.of(this).get(PlaceViewModel::class.java) }

    private lateinit var adapter: PlaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_place, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity is MainActivity && viewModel.hasPlace()) {
            val place = viewModel.getSavedLiveData()
            val intent = Intent(context, WeatherActivity::class.java).apply {
                putExtra("location_lng", place.location.lng)
                putExtra("location_lat", place.location.lat)
                putExtra("place_name", place.name)
            }
            startActivity(intent)
            activity?.finish()
            return
        }
        val layoutManager = LinearLayoutManager(activity)
        recyclerview.layoutManager = layoutManager as RecyclerView.LayoutManager
        adapter = PlaceAdapter(this, viewModel.placeList)
        recyclerview.adapter = adapter
        searchPlaceEdit.addTextChangedListener { editable ->
            val content = editable.toString()
            if (content.isNotEmpty()) {
                viewModel.searchPlaces(content)
            } else {
                recyclerview.visibility = View.GONE
                animation_place_footer.visibility = View.VISIBLE
                animation_place_footer.resumeAnimation()
                tipsText.visibility = View.VISIBLE
                animation_place_header.visibility = View.VISIBLE
                animation_place_header.resumeAnimation()
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }

        viewModel.placeLiveData.observe(viewLifecycleOwner, Observer { result ->
            val places = result.getOrNull()
            if (places != null) {
                recyclerview.visibility = View.VISIBLE
                animation_place_footer.visibility = View.GONE
                animation_place_footer.pauseAnimation()
                tipsText.visibility = View.GONE
                animation_place_header.visibility = View.GONE
                animation_place_header.pauseAnimation()
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                adapter.notifyDataSetChanged()
            } else {
                "未能查询到任何地点".showToast()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }
}