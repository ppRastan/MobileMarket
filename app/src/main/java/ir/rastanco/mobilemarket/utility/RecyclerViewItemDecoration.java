package ir.rastanco.mobilemarket.utility;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ShaisteS on 1394/12/18.
 * this class is A Custom Item Decoration in recycler view when recycler view is grid and set space between column
 */
public class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration {

    private int verticalSpacing;
    private int horizontalSpacing;

    public RecyclerViewItemDecoration(int verticalSpacing,int horizontalSpacing) {
        this.verticalSpacing = verticalSpacing;
        this.horizontalSpacing=horizontalSpacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,RecyclerView parent, RecyclerView.State state) {
        outRect.left = horizontalSpacing;
        outRect.right = horizontalSpacing;
        outRect.bottom = verticalSpacing;
        outRect.top = verticalSpacing;
    }
}
