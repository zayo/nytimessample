package cz.nedbalek.nytimessample.ui.helpers

import androidx.fragment.app.FragmentActivity
import cz.nedbalek.nytimessample.ui.fragment.BaseContentFragment

/**
 * Created by prasniatko on 11/07/2017.
 */
fun BaseContentFragment.show(id: Int, activity: FragmentActivity) {
    activity.supportFragmentManager.beginTransaction().replace(id, this).commit()
}
