package cz.nedbalek.nytimessample.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationBarView
import cz.nedbalek.nytimessample.R
import cz.nedbalek.nytimessample.databinding.ActivityMainBinding
import cz.nedbalek.nytimessample.ui.fragment.BaseContentFragment
import cz.nedbalek.nytimessample.ui.fragment.BaseContentFragment.ContentType.MAILED
import cz.nedbalek.nytimessample.ui.fragment.BaseContentFragment.ContentType.SHARED
import cz.nedbalek.nytimessample.ui.fragment.BaseContentFragment.ContentType.VIEWED
import cz.nedbalek.nytimessample.ui.helpers.show

/**
 * Root activity responsible for displaying Fragments within bottom navigation.
 */
class MainActivity : AppCompatActivity(),
    NavigationBarView.OnItemSelectedListener,
    NavigationBarView.OnItemReselectedListener {

    private val mostMailedFragment by lazy { BaseContentFragment.create(MAILED) }
    private val mostSharedFragment by lazy { BaseContentFragment.create(SHARED) }
    private val mostViewedFragment by lazy { BaseContentFragment.create(VIEWED) }

    private var currentFragment: BaseContentFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
            navigation.setOnItemSelectedListener(this@MainActivity)
            navigation.setOnItemReselectedListener(this@MainActivity)
        }

        show(mostMailedFragment)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.navigation_most_mailed -> show(mostMailedFragment)
        R.id.navigation_most_shared -> show(mostSharedFragment)
        R.id.navigation_most_viewed -> show(mostViewedFragment)
        else -> error("Undefined item for BottomNavigation")
    }.let { true }

    override fun onNavigationItemReselected(item: MenuItem) {
        currentFragment?.reselected()
    }

    private fun show(fragment: BaseContentFragment) {
        currentFragment = fragment
        currentFragment?.show(R.id.content, this)
    }
}