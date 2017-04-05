package com.app.feng.fixtablelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.app.feng.fixtablelayout.adapter.IDataAdapter;
import com.app.feng.fixtablelayout.adapter.TableAdapter;
import com.app.feng.fixtablelayout.widget.SingleLineItemDecoration;
import com.app.feng.fixtablelayout.widget.TableLayoutManager;

/**
 * Created by feng on 2017/4/2.
 */
public class FixTableLayout extends FrameLayout {

    RecyclerView recyclerView;
    HorizontalScrollView titleView;
    RecyclerView leftViews;
    TextView left_top_view;
    View leftViewShadow;

    int divider_height;
    int divider_color;
    int col_1_color;
    int col_2_color;
    int title_color;
    int item_width;
    int item_padding;
    int item_gravity;

    boolean show_left_shadow = false;
    private IDataAdapter dataAdapter;

    public FixTableLayout(Context context) {
        this(context,null);
    }

    public FixTableLayout(
            Context context,@Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public FixTableLayout(Context context,@Nullable AttributeSet attrs,int defStyleAttr) {
        super(context,attrs,defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.FixTableLayout);

        divider_height = array.getDimensionPixelOffset(
                R.styleable.FixTableLayout_fixtable_divider_height,4);
        divider_color = array.getColor(R.styleable.FixTableLayout_fixtable_divider_color,
                                       Color.BLACK);

        col_1_color = array.getColor(R.styleable.FixTableLayout_fixtable_column_1_color,Color.BLUE);
        col_2_color = array.getColor(R.styleable.FixTableLayout_fixtable_column_2_color,
                                     Color.WHITE);
        title_color = array.getColor(R.styleable.FixTableLayout_fixtable_title_color,Color.GRAY);
        item_width = array.getDimensionPixelOffset(R.styleable.FixTableLayout_fixtable_item_width,
                                                   400);
        item_padding = array.getDimensionPixelOffset(
                R.styleable.FixTableLayout_fixtable_item_top_bottom_padding,0);
        item_gravity = array.getInteger(R.styleable.FixTableLayout_fixtable_item_gravity,0);

        switch (item_gravity) {
            case 0:
                item_gravity = Gravity.CENTER;
                break;
            case 1:
                item_gravity = Gravity.START | Gravity.CENTER_VERTICAL;
                break;
            case 2:
                item_gravity = Gravity.END | Gravity.CENTER_VERTICAL;
                break;
        }

        show_left_shadow = array.getBoolean(
                R.styleable.FixTableLayout_fixtable_show_left_view_shadow,false);

        array.recycle();

        View view = inflate(context,R.layout.table_view,null);
        init(view);
        addView(view);
    }

    private void init(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        titleView = (HorizontalScrollView) view.findViewById(R.id.titleView);
        leftViews = (RecyclerView) view.findViewById(R.id.leftViews);
        left_top_view = (TextView) view.findViewById(R.id.left_top_view);
        leftViewShadow = view.findViewById(R.id.leftView_shadows);

        leftViews.setLayoutManager(new TableLayoutManager());
        leftViews.addItemDecoration(new SingleLineItemDecoration(divider_height,divider_color));
        leftViews.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v,MotionEvent event) {
                //将事件发送到RV
                recyclerView.onTouchEvent(event);
                return true;
            }
        });

        if (show_left_shadow) {
            leftViewShadow.setVisibility(VISIBLE);
        } else {
            leftViewShadow.setVisibility(GONE);
        }

        recyclerView.setLayoutManager(new TableLayoutManager());
        recyclerView.addItemDecoration(new SingleLineItemDecoration(divider_height,divider_color));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,int newState) {
                super.onScrollStateChanged(recyclerView,newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView,int dx,int dy) {
                super.onScrolled(recyclerView,dx,dy);
                titleView.scrollBy(dx,0);
                leftViews.scrollBy(0,dy);
            }
        });
    }

    public void setAdapter(
            IDataAdapter dataAdapter) {
        this.dataAdapter = dataAdapter;
        initAdapter();
    }

    private void initAdapter() {
        TableAdapter.Builder builder = new TableAdapter.Builder();
        TableAdapter tableAdapter = builder.setLeft_top_view(left_top_view)
                .setTitleView(titleView)
                .setParametersHolder(
                        new TableAdapter.ParametersHolder(col_1_color,col_2_color,title_color,
                                                          item_width,item_padding,item_gravity))
                .setLeftViews(leftViews)
                .setDataAdapter(dataAdapter)
                .create();
        recyclerView.setAdapter(tableAdapter);
    }
}
