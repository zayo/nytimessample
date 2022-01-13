package cz.nedbalek.nytimessample.ui.adapter

import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import cz.nedbalek.nytimessample.databinding.ItemArticleSimpleBinding
import cz.nedbalek.nytimessample.ui.adapter.ArticlesAdapter.PositionComposer
import cz.nedbalek.nytimessample.ui.helpers.getColor
import cz.nedbalek.nytimessample.viewobjects.Article

/**
 * Adapter for [Article].
 */
class ArticlesAdapter(context: ContextThemeWrapper, val listener: ArticlesActionListener) :
    ListAdapter<Article, ArticlesAdapter.ArticleViewHolder>(Differ()) {

    private val picasso: Picasso = Picasso.Builder(context).build()
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val clickComposer = PositionComposer { position -> listener(getItem(position)) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder =
        ArticleViewHolder(
            ItemArticleSimpleBinding.inflate(inflater, parent, false),
            clickComposer,
            picasso
        )

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) =
        holder.bind(getItem(position))

    class ArticleViewHolder(
        private val binding: ItemArticleSimpleBinding,
        provider: PositionComposer,
        private val picasso: Picasso
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.articleCardView.setOnClickListener { provider(bindingAdapterPosition) }
        }

        fun bind(article: Article) = with(binding) {
            articleTitle.text = article.title
            articleCategory.text = article.section
            articleCategory.setTextColor(getColor(article.section))

            picasso.load(article.imageUrl).into(articleImage)
        }
    }

    private class Differ : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article) =
            oldItem.title == newItem.title && oldItem.section == newItem.section

        override fun areContentsTheSame(oldItem: Article, newItem: Article) =
            oldItem == newItem
    }

    /**
     * Listener of [Article] clicked in this recycler.
     */
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