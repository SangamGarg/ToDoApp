package com.sangam.todoapp.OnBoard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.sangam.todoapp.OnBoard.Screens.FirstScreen
import com.sangam.todoapp.OnBoard.Screens.SecondScreen
import com.sangam.todoapp.R

class ViewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_pager, container, false)
        val fragmentlist:List<Fragment> = listOf(FirstScreen(), SecondScreen())

        val adapter =
            ViewPagerAdapter(fragmentlist, requireActivity().supportFragmentManager,lifecycle)

        view.findViewById<ViewPager2>(R.id.onBoardViewPager).adapter =adapter
        return view
    }
}