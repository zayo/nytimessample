package cz.nedbalek.nytimessample.ui.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import cz.nedbalek.nytimessample.databinding.ActivityDetailBinding
import cz.nedbalek.nytimessample.ui.helpers.getColor
import cz.nedbalek.nytimessample.viewobjects.Article

/**
 * Detail activity that displays the [Article].
 */
class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val article = intent.extras?.get(PARAM_ARTICLE) as? Article
            ?: run { finish(); return@onCreate }

        ActivityDetailBinding.inflate(layoutInflater).apply {
            setContentView(root)
            setSupportActionBar(toolbar)
            populate(article)
        }

        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setDisplayShowTitleEnabled(true)
        }
    }

    private fun ActivityDetailBinding.populate(article: Article) {
        image.load(article.imageUrl)

        articleTitle.text = article.title
        articleCategory.text = article.section
        articleCategory.setTextColor(getColor(article.section))
        articleAuthor.text = article.byline
        articleAbstract.text = article.abstract
        articleDate.text = article.published_date
        actionReadFull.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(article.url)))
        }
    }

    companion object {
        const val PARAM_ARTICLE = "article"

        fun create(context: Activity, article: Article) {
            Intent(context, DetailActivity::class.java).run {
                putExtra(PARAM_ARTICLE, article)
                context.startActivity(this)
            }
        }
    }
}