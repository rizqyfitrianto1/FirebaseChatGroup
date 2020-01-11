package com.tugasbesarkotlin5.firechatgo2.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class MyPagerAdapter(fm: FragmentManager?, titles: Array<CharSequence>, numboftabs: Int) : FragmentPagerAdapter(fm!!) {

    private val title = titles
    private val numboftab = numboftabs

    override fun getItem(p: Int): Fragment {
        if (p == 0) {
            return UserListFragment()
        }
        if (p == 1) {
            return GroupListFragment()
        }
        return UserListFragment()
    }

    override fun getCount(): Int {
        return numboftab
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return title[position]
    }
}