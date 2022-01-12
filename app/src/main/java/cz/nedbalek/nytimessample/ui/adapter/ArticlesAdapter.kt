package cz.nedbalek.nytimessample.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import cz.nedbalek.nytimessample.R
import cz.nedbalek.nytimessample.ui.adapter.ArticlesAdapter.PositionComposer
import cz.nedbalek.nytimessample.ui.helpers.getColor
import cz.nedbalek.nytimessample.viewobjects.Article
import kotlinx.android.synthetic.main.item_article_simple.view.*

class ArticlesAdapter(context: Context, val listener: ArticlesActionListener) :
    ListAdapter<Article, ArticlesAdapter.ArticleViewHolder>(Differ()) {

    private val picasso: Picasso = Picasso.Builder(context).build()
    private val clickComposer = PositionComposer { position -> listener(getItem(position)) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder =
        ArticleViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_article_simple, parent, false),
            clickComposer,
            picasso
        )

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) =
        holder.bind(getItem(position))

    class ArticleViewHolder(view: View, provider: PositionComposer, private val picasso: Picasso) :
        RecyclerView.ViewHolder(view) {
        private val title = view.article_title
        private val category = view.article_category
        private val image = view.article_image

        init {
            view.article_card_view.setOnClickListener { provider(bindingAdapterPosition) }
        }

        fun bind(article: Article) {
            title.text = article.title
            category.text = article.section
            category.setTextColor(getColor(article.section))

            picasso.load(article.imageUrl).into(image)
        }
    }

    private class Differ : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article) =
            oldItem.title == newItem.title && oldItem.section == newItem.section

        override fun areContentsTheSame(oldItem: Article, newItem: Article) =
            oldItem == newItem
    }

    fun interface ArticlesActionListener {
        operator fun invoke(article: Article)
    }

    /**
     * Provides position of the clicked view to upper level for composition with the data.
     */
    // TODO move to some generic utils.
    fun interface PositionComposer {
        operator fun invoke(position: Int)
    }
}