package cz.nedbalek.nytimessample.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import cz.nedbalek.nytimessample.R
import cz.nedbalek.nytimessample.lib.SortedRecyclerAdapter
import cz.nedbalek.nytimessample.ui.helpers.getColor
import cz.nedbalek.nytimessample.viewobjects.Article
import kotlinx.android.synthetic.main.item_article_simple.view.*

class ArticlesAdapter(val inflater: LayoutInflater, val listener: ArticlesActionListener?) :
        SortedRecyclerAdapter<Article, ArticlesAdapter.ArticleViewHolder>(Article::class.java, AdapterType.SORTED_DESCENDING_SELECTABLE) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(inflater.inflate(R.layout.item_article_simple, parent, false))
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = getItemAt(position)

        holder.title.text = item.title
        holder.category.text = item.section
        holder.category.setTextColor(getColor(item.section))

        Picasso.Builder(inflater.context).build().load(item.getImageUrl()).into(holder.image)
    }

    inner class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.article_title
        val category = view.article_category
        val image = view.article_image

        init {
            view.article_card_view.setOnClickListener {
                listener?.onArticleClicked(getItemAt(adapterPosition))
            }
        }
    }

    interface ArticlesActionListener {
        fun onArticleClicked(article: Article)
    }
}