package com.example.futureweather.ui.manage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.futureweather.R
import com.example.futureweather.extension.showToast
import kotlinx.android.synthetic.main.fragment_manage_places.*

class ManageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_manage_places, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        managePlaceBtn.setOnClickListener {
            "管理位置功能正在开发中".showToast()
        }
    }
}