package cz.nedbalek.nytimessample.ui.helpers

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by prasniatko on 11/07/2017.
 */
class CardViewDecorator(val dim: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = dim
        }
        outRect.left = dim
        outRect.right = dim
        outRect.bottom = dim
    }

}