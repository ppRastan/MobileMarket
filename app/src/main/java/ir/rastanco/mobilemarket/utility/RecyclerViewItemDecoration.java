package ir.rastanco.mobilemarket.utility;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by shaisteS on 1394/12/18.
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
