package com.example.futureweather.ui.manage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.futureweather.R
import com.example.futureweather.extension.showToast
import com.example.futureweather.logic.model.PlaceResponse
import com.example.futureweather.ui.weather.WeatherActivity
import com.example.futureweather.utils.GlobalUtil
import kotlinx.android.synthetic.main.fragment_manage_places.*

class ManageFragment : Fragment() {
    val viewModel by lazy { ViewModelProviders.of(this).get(ManageViewModel::class.java) }
    private lateinit var adapter: ManagePlaceAdapter
    private val placeList = ArrayList<PlaceResponse.Place>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_manage_places, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initNowLayout(activity)
    }

    override fun onResume() {
        super.onResume()
        placeList.clear()
        placeList.addAll(viewModel.getManagePlaceList())
        adapter.notifyDataSetChanged()
    }

    private fun initNowLayout(activity: FragmentActivity?) {
        if (activity is WeatherActivity && viewModel.hasPlace()) {
            placeList.addAll(viewModel.getManagePlaceList())
            nowPlaceName.text = placeList[0].name
            nowPlaceLayout.setOnClickListener {
               GlobalUtil.showTipDialog(
                   activity,
                   getString(R.string.note_now_content),
                   getString(R.string.note_confirm)
               )
            }
        }
        val layoutManager = LinearLayoutManager(activity)
        recyclerview_manage.layoutManager = layoutManager as RecyclerView.LayoutManager
        adapter = ManagePlaceAdapter(this, placeList)
        recyclerview_manage.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    /**
     * 刷新当前地区
     */
    fun refreshNowPlaceName(){
        nowPlaceName.text = placeList[0].name
    }
}