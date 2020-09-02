package com.example.futureweather.ui.manage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.futureweather.R
import com.example.futureweather.extension.showToast
import com.example.futureweather.logic.model.PlaceResponse
import com.example.futureweather.ui.weather.WeatherActivity
import com.example.futureweather.utils.GlobalUtil
import com.example.futureweather.widget.DiyDialog
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
        initNoteSwitch(activity)
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

    private fun initNoteSwitch(activity: FragmentActivity?) {
        note_switch_btn.setOnClickListener {
            if (!note_switch_btn.isChecked) {
                switchIconSrc(false)
                "已关闭通知功能".showToast()
            } else {
                generateTipDialog(activity)
            }
        }
    }

    private fun generateTipDialog(activity: FragmentActivity?) {
        if (activity is WeatherActivity) {
            val tipDialogView =
                LayoutInflater.from(activity).inflate(R.layout.dialog_tip_layout, null)
            val confirmBtn = tipDialogView.findViewById<TextView>(R.id.confirm_button)
            val contentText = tipDialogView.findViewById<TextView>(R.id.content_text)
            val animationSwitch =
                tipDialogView.findViewById<LottieAnimationView>(R.id.animation_set_note_finish)
            confirmBtn.text = getString(R.string.note_confirm)
            contentText.text = getString(R.string.note_switch_content)
            val dialog = DiyDialog(activity, R.style.DiyDialog, tipDialogView)
            animationSwitch.addAnimatorUpdateListener {
                if (it.animatedFraction == 1F) {
                    switchIconSrc(true)
                    dialog.closeDiyDialog()
                }
            }
            confirmBtn.setOnClickListener {
                contentText.visibility = View.GONE
                animationSwitch.visibility = View.VISIBLE
                if (!animationSwitch.isAnimating) {
                    animationSwitch.playAnimation()
                }
            }
            dialog.setCanceledOnTouchOutside(false)
            dialog.showDiyDialog()
        }
    }

    private fun switchIconSrc(turnOn: Boolean) {
        if (turnOn) {
            ic_note.visibility = View.GONE
            animation_note.visibility = View.VISIBLE
            animation_note.playAnimation()
        } else {
            ic_note.visibility = View.VISIBLE
            animation_note.visibility = View.GONE
            animation_note.pauseAnimation()
        }
    }

    /**
     * 刷新当前地区
     */
    fun refreshNowPlaceName() {
        nowPlaceName.text = placeList[0].name
    }
}