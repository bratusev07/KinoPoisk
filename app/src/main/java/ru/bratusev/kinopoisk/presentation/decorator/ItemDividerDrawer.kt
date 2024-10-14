package ru.bratusev.kinopoisk.presentation.decorator

import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.surfstudio.android.recycler.decorator.Decorator


class ItemDividerDrawer(private val gap: Gap) : Decorator.ViewHolderDecor {

    private val dividerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val alpha = dividerPaint.alpha

    init {
        dividerPaint.color = gap.color
        dividerPaint.strokeWidth = gap.width.toFloat()
    }

    override fun draw(
        canvas: Canvas,
        view: View,
        recyclerView: RecyclerView,
        state: RecyclerView.State
    ) {
        val viewHolder = recyclerView.getChildViewHolder(view)
        val nextViewHolder =
            recyclerView.findViewHolderForAdapterPosition(viewHolder.bindingAdapterPosition + 1)

        val startX: Int
        val startY: Int
        val stopX: Int
        val stopY: Int

        if(gap.isVertical) {
            startX = recyclerView.paddingLeft + gap.paddingStart
            startY = (view.bottom + view.translationY).toInt()
            stopX = recyclerView.width - recyclerView.paddingRight - gap.paddingEnd
            stopY = startY
        } else {
            startX = (view.right + view.translationX).toInt()
            startY = recyclerView.paddingTop + gap.paddingStart
            stopX = startX
            stopY = recyclerView.height - recyclerView.paddingBottom - gap.paddingEnd
        }

        dividerPaint.alpha = (view.alpha * alpha).toInt()

        val areSameHolders = viewHolder.itemViewType == nextViewHolder?.itemViewType

        if (areSameHolders) {
            canvas.drawLine(startX.toFloat(), startY.toFloat(), stopX.toFloat(), stopY.toFloat(), dividerPaint)
        } else {
            canvas.drawLine(startX.toFloat(), startY.toFloat(), stopX.toFloat(), stopY.toFloat(), dividerPaint)
        }
    }

}