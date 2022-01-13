package cz.nedbalek.nytimessample.ui.helpers

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Decorates items of recycler view.
 * Adds [margin] to every item's side.
 */
class CardViewDecorator(private val margin: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        if (parent.getChildAdapterPosition(view) == 0) {
            // Add top only to first item, otherwise bottom will be used from spacing.
            outRect.top = margin
        }
        outRect.left = margin
        outRect.right = margin
        outRect.bottom = margin
    }
}
