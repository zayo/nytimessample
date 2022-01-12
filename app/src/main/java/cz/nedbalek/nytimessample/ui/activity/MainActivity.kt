package cz.nedbalek.nytimessample.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationBarView
import cz.nedbalek.nytimessample.R
import cz.nedbalek.nytimessample.ui.fragment.BaseContentFragment
import cz.nedbalek.nytimessample.ui.fragment.BaseContentFragment.ContentType.MAILED
import cz.nedbalek.nytimessample.ui.fragment.BaseContentFragment.ContentType.SHARED
import cz.nedbalek.nytimessample.ui.fragment.BaseContentFragment.ContentType.VIEWED
import cz.nedbalek.nytimessample.ui.helpers.show
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    NavigationBarView.OnItemSelectedListener,
    NavigationBarView.OnItemReselectedListener {

    private val mostMailedFragment by lazy { BaseContentFragment.create(MAILED) }
    private val mostSharedFragment by lazy { BaseContentFragment.create(SHARED) }
    private val mostViewedFragment by lazy { BaseContentFragment.create(VIEWED) }

    private var currentFragment: BaseContentFragment? = null
        set(value) {
            field = value
            value?.show(R.id.content, this)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnItemSelectedListener(this)
        navigation.setOnItemReselectedListener(this)

        currentFragment = mostMailedFragment
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        fun use(fragment: BaseContentFragment?): Boolean {
            currentFragment = fragment
            return fragment != null
        }

        return when (item.itemId) {
            R.id.navigation_most_mailed -> use(mostMailedFragment)
            R.id.navigation_most_shared -> use(mostSharedFragment)
            R.id.navigation_most_viewed -> use(mostViewedFragment)
            else -> use(null)
        }
    }

    override fun onNavigationItemReselected(item: MenuItem) {
        currentFragment?.reselected()
    }
}