package com.example.soonbox_findpw.data;

import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soonbox_findpw.Frag1;

public class MyItemDecoration extends RecyclerView.ItemDecoration {
    private int size10;
    private int size5;

    public MyItemDecoration(Frag1 context) {

        size10 = dpToPx(context, 10);  //10dp씩 여백을 주기위해 size10 변수를 생성
        size5 = dpToPx(context, 5);    //5dp씩 여백을 주기위해 size5 변수를 생성
    }

    // recyclerView 안에 있는 아이템에 여백을 설정
    @Override
    public void getItemOffsets
            (@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        //position : 각 아이템의 포지션을 받아옴. / itemCount : 전체 아이템 개수를 받아옴.
        int position = parent.getChildAdapterPosition(view);
        int itemCount = state.getItemCount();

        //상하 설정
        if(position == 0 || position == 1) {
            // 첫번 째 줄 아이템
            outRect.top = size10;
            outRect.bottom = size10;
        } else {
            outRect.bottom = size10;
        }

        GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) view.getLayoutParams();
        int spanIndex = lp.getSpanIndex();

        if(spanIndex == 0) { //왼쪽 아이템을 나타냄
            outRect.left = size10;
            outRect.right = size5;

        } else if(spanIndex == 1) { //오른쪽 아이템을 나타냄
            outRect.left = size5;
            outRect.right = size10;
        }

        //상(top), 우(right), 하(bottom), 좌(left)에  여백을 설정
        outRect.top = size10;
        outRect.right = size10;
        outRect.bottom = size10;
        outRect.left = size10;

    }

    // dp -> pixel단위로 변경 (코드를 통해 view 사이즈에 변화를 주거나 여백을 설정해 줄 때는 Pixel 단위로 변환해서 작업)
    private int dpToPx(Frag1 context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

}



