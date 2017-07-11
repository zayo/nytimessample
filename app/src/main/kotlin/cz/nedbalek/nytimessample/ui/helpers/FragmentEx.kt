package cz.nedbalek.nytimessample.ui.helpers

import android.app.Activity
import cz.nedbalek.nytimessample.ui.fragment.BaseContentFragment

/**
 * Created by prasniatko on 11/07/2017.
 */
 fun BaseContentFragment.show(id: Int, activity: Activity) {
    activity.fragmentManager.beginTransaction().replace(id, this).commit()
}
