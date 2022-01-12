package cz.nedbalek.nytimessample.ui.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import cz.nedbalek.nytimessample.R
import cz.nedbalek.nytimessample.ui.helpers.ColorGenerator
import cz.nedbalek.nytimessample.viewobjects.Article
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    companion object {
        const val TAG = "DetailActivity"
        const val PARAM_ARTICLE = "article"

        fun create(context: Activity, article: Article) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(PARAM_ARTICLE, article)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        val article = intent.extras?.get(PARAM_ARTICLE) as? Article ?: return

        populate(article)
    }

    private fun populate(article: Article) {
        Picasso.Builder(this).build().load(article.getImageUrl()).into(image)

        article_title.text = article.title
        article_category.text = article.section
        article_category.setTextColor(ColorGenerator.getColor(article.section).toInt())
        article_author.text = article.byline
        article_abstract.text = article.abstract
        article_date.text = article.published_date

        action_read_full.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(article.url)
            startActivity(i)
        }
    }
}