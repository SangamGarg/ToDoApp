package com.sangam.todoapp.OnBoard.Screens

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.sangam.todoapp.R

class FirstScreen : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_first_screen, container, false)
        val viewPager = activity?.findViewById<ViewPager2>(R.id.onBoardViewPager)
//        if (onBoardFinished()) {
//            activity?.finishAffinity()
//        }else{
            view.findViewById<Button>(R.id.btn_next).setOnClickListener {
                viewPager?.currentItem = 1
            }

            view.findViewById<Button>(R.id.btn_skip).setOnClickListener {
                findNavController().navigate(R.id.action_viewPagerFragment_to_mainActivity)
            }
//        }

        return view
    }

    private fun onBoardFinished(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }
}




