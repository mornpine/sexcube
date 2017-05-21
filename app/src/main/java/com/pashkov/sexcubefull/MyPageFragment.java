package com.pashkov.sexcubefull;

import android.view.View;
import android.widget.TextView;
import com.pashkov.sexcubefull.view_pager.PageFragment;
import com.pashkov.sexcubefull.view_pager.PageLayout;

public class MyPageFragment extends PageFragment<MyPageItem> {
    public static int pxHeight, pxWeigth;

    @Override
    public View setupPage(PageLayout pageLayout, MyPageItem pageItem) {

        View pageContent = pageLayout.findViewById(R.id.page_content);
        pageContent.getLayoutParams().height = pxHeight;
        pageContent.getLayoutParams().width = pxWeigth;


        TextView title = (TextView) pageContent.findViewById(R.id.title);
        title.setText(pageItem.getTitle());
        return pageContent;
    }
    public void setPxHeightWeight (int height, int wigth) {
        pxHeight = height;
        pxWeigth = wigth;
    }
}

