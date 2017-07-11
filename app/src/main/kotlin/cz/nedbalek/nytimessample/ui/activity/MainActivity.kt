package cz.nedbalek.nytimessample.ui.activity

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import cz.nedbalek.nytimessample.R
import cz.nedbalek.nytimessample.ui.adapter.ArticlesAdapter
import cz.nedbalek.nytimessample.ui.fragment.BaseContentFragment
import cz.nedbalek.nytimessample.ui.fragment.BaseContentFragment.ContentType.*
import cz.nedbalek.nytimessample.ui.helpers.show
import cz.nedbalek.nytimessample.viewobjects.Article
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
        BottomNavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemReselectedListener,
        ArticlesAdapter.ArticlesActionListener {

    private val mostMailedFragment by lazy { BaseContentFragment.create(MAILED) }
    private val mostSharedFragment by lazy { BaseContentFragment.create(SHARED) }
    private val mostViewedFragment by lazy { BaseContentFragment.create(VIEWED) }

    private var currentFragment: BaseContentFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(this)
        navigation.setOnNavigationItemReselectedListener(this)

        mostMailedFragment.show(R.id.content, this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean =
            when (item.itemId) {
                R.id.navigation_most_mailed -> {
                    currentFragment = mostMailedFragment
                    currentFragment?.show(R.id.content, this)
                    true
                }
                R.id.navigation_most_shared -> {
                    currentFragment = mostSharedFragment
                    currentFragment?.show(R.id.content, this)
                    true
                }
                R.id.navigation_most_viewed -> {
                    currentFragment = mostViewedFragment
                    currentFragment?.show(R.id.content, this)
                    true
                }
                else -> {
                    currentFragment = null
                    false
                }

            }


    override fun onNavigationItemReselected(item: MenuItem) {
        currentFragment?.reselected()
    }

    override fun onArticleClicked(article: Article) {
        DetailActivity.create(this, article)
    }
}