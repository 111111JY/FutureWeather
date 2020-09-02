package com.example.futureweather.ui.manage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.futureweather.R
import com.example.futureweather.extension.showToast
import com.example.futureweather.logic.model.PlaceResponse
import com.example.futureweather.ui.weather.WeatherActivity
import com.example.futureweather.widget.DiyDialog
import kotlinx.android.synthetic.main.activity_weather.*
import java.util.*

class ManagePlaceAdapter(
    private val fragment: ManageFragment,
    private val placeList: ArrayList<PlaceResponse.Place>
) :
    RecyclerView.Adapter<ManagePlaceAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeName: TextView = view.findViewById(R.id.placeName)
        val placeAddress: TextView = view.findViewById(R.id.placeAddress)
        val refreshWeatherBtn: ImageView = view.findViewById(R.id.refreshWeatherBtn)
        val manageDeleteBtn: TextView = view.findViewById(R.id.manageDeleteBtn)
        val setLocationBtn: TextView = view.findViewById(R.id.setLocationBtn)
        val placeInfoLayout: RelativeLayout = view.findViewById(R.id.placeInfoLayout)
        val operatingLayout: LinearLayout = view.findViewById(R.id.operatingLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.manage_place_item, parent, false)
        val holder = ViewHolder(view)
        holder.manageDeleteBtn.setOnClickListener {
            showDeleteDialog(parent.context, holder.adapterPosition)
            holder.operatingLayout.visibility = View.GONE
        }
        holder.setLocationBtn.setOnClickListener {
            showLocationDialog(parent.context, holder.adapterPosition)
            holder.operatingLayout.visibility = View.GONE
        }
        holder.refreshWeatherBtn.setOnClickListener {
            val position = holder.adapterPosition
            val place = placeList[position]
            val activity = fragment.activity
            if (activity is WeatherActivity) {
                activity.drawerLayout.closeDrawers()
                activity.viewModel.locationLng = place.location.lng
                activity.viewModel.locationLat = place.location.lat
                activity.viewModel.placeName = place.name
                activity.refreshWeather()
            }
        }
        holder.placeInfoLayout.setOnClickListener {
            if (holder.operatingLayout.visibility == View.GONE) {
                holder.operatingLayout.visibility = View.VISIBLE
            } else if (holder.operatingLayout.visibility == View.VISIBLE) {
                holder.operatingLayout.visibility = View.GONE
            }
        }
        return holder
    }

    override fun getItemCount() = placeList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val placeInfo = placeList[position]
        holder.placeName.text = placeInfo.name
        holder.placeAddress.text = placeInfo.address
    }

    private fun showDeleteDialog(context: Context, position: Int) {
        val diyDialogView =
            LayoutInflater.from(context).inflate(R.layout.dialog_diy_layout, null)
        val confirmBtn = diyDialogView.findViewById<TextView>(R.id.confirm_button)
        val cancelBtn = diyDialogView.findViewById<TextView>(R.id.cancel_button)
        val contentText = diyDialogView.findViewById<TextView>(R.id.content_text)
        val contentAnimation =
            diyDialogView.findViewById<LottieAnimationView>(R.id.content_animation)
        confirmBtn.text = fragment.getString(R.string.note_delete)
        cancelBtn.text = fragment.getString(R.string.note_cancel)
        contentText.text = fragment.getString(R.string.note_delete_content)
        val dialog = DiyDialog(context, R.style.DiyDialog, diyDialogView)

        cancelBtn.setOnClickListener {
            dialog.closeDiyDialog()
        }
        confirmBtn.setOnClickListener {
            if (placeList.size > 1){
                //移除指定位置的地区
                placeList.removeAt(position)
                //保存现在的地区列表
                fragment.viewModel.savePlace(placeList)
                contentText.visibility = View.GONE
                contentAnimation.setAnimation(R.raw.delete_finish)
                contentAnimation.visibility = View.VISIBLE
                contentAnimation.loop(false)
                if (!contentAnimation.isAnimating) {
                    contentAnimation.playAnimation()
                }

                contentAnimation.addAnimatorUpdateListener {
                    if (it.animatedFraction == 1F) {
                        dialog.closeDiyDialog()
                    }
                }
                //通知recyclerview刷新列表
                notifyDataSetChanged()
            } else {
                fragment.getString(R.string.note_delete_size1).showToast()
            }
        }
        dialog.setCanceledOnTouchOutside(false)
        dialog.showDiyDialog()
    }

    private fun showLocationDialog(context: Context, position: Int) {
        val diyDialogView =
            LayoutInflater.from(context).inflate(R.layout.dialog_diy_layout, null)
        val confirmBtn = diyDialogView.findViewById<TextView>(R.id.confirm_button)
        val cancelBtn = diyDialogView.findViewById<TextView>(R.id.cancel_button)
        val contentText = diyDialogView.findViewById<TextView>(R.id.content_text)
        val contentAnimation =
            diyDialogView.findViewById<LottieAnimationView>(R.id.content_animation)
        confirmBtn.text = fragment.getString(R.string.note_confirm)
        cancelBtn.text = fragment.getString(R.string.note_cancel)
        contentText.text = fragment.getString(R.string.note_location_content)
        val dialog = DiyDialog(context, R.style.DiyDialog, diyDialogView)

        cancelBtn.setOnClickListener {
            dialog.closeDiyDialog()
        }
        confirmBtn.setOnClickListener {
            if (placeList.size > 1) {
                //将原有置顶当前地区与现需置顶的地区位置对换
                Collections.swap(placeList, 0, position)
            }
            //刷新当前地区
            fragment.refreshNowPlaceName()
            //保存现在的地区列表
            fragment.viewModel.savePlace(placeList)
            //刷新当前地区的天气
            val place = placeList[0]
            val activity = fragment.activity
            if (activity is WeatherActivity) {
                activity.viewModel.locationLng = place.location.lng
                activity.viewModel.locationLat = place.location.lat
                activity.viewModel.placeName = place.name
                activity.refreshWeather()
            }

            contentAnimation.addAnimatorUpdateListener {
                if (it.animatedFraction == 1F) {
                    dialog.closeDiyDialog()
                    if (activity is WeatherActivity) {
                        activity.drawerLayout.closeDrawers()
                    }
                }
            }

            contentText.visibility = View.GONE
            contentAnimation.setAnimation(R.raw.set_done_finish)
            contentAnimation.visibility = View.VISIBLE
            contentAnimation.loop(false)
            if (!contentAnimation.isAnimating) {
                contentAnimation.playAnimation()
            }

            //通知recyclerview刷新列表
            notifyDataSetChanged()
        }
        dialog.setCanceledOnTouchOutside(false)
        dialog.showDiyDialog()
    }
}