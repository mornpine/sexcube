package com.pashkov.sexcubefull.view_pager;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.*;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.FrameLayout;
import android.view.View.OnTouchListener;

import com.pashkov.sexcubefull.R;

public class CarouselViewPager extends ViewPager implements OnTouchListener {
    public static final float DEFAULT_SIDE_PAGES_VISIBLE_PART = 0.5f;
    private static final String TAG = CarouselViewPager.class.getSimpleName();
    private static final boolean DEBUG = false;

    private int mViewPagerWidth;
    private int mViewPagerHeight;

    // Distance between pages always will be greater than this value (even when scaling)
    private int mMinPagesOffset;
    private float mSidePagesVisiblePart;
    private int mWrapPadding;

    private boolean mSizeChanged;
    private float gravityOffset;

    private Resources mResources;
    private CarouselConfig mConfig;
    
    private GestureDetector mGestureDetector;
    private OnGestureListener mGestureListener;
    private View mTouchedView;
    private Parcelable mTouchedItem;
    
    private final String mPackageName;
    private int mPageContentWidthId;
    private int mPageContentHeightId;

    public static int heigth_frag, width_frag;

    public CarouselViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CarouselViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);

        mConfig = CarouselConfig.getInstance();
        mConfig.pagerId = getId();
        mResources = context.getResources();
        mPackageName = context.getPackageName();

        mPageContentWidthId = mResources.getIdentifier("page_content_width", "dimen",
                mPackageName);
        mPageContentHeightId = mResources.getIdentifier("page_content_height", "dimen",
                mPackageName);
        
        DisplayMetrics dm = mResources.getDisplayMetrics();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CarouselViewPager,
                defStyle, 0);
        try {
            if (a != null) {
                mConfig.orientation = a.getInt(R.styleable.CarouselViewPager_android_orientation,
                        CarouselConfig.HORIZONTAL);
                mConfig.infinite =
                        a.getBoolean(R.styleable.CarouselViewPager_infinite, true);
                mConfig.scrollScalingMode =
                        a.getInt(R.styleable.CarouselViewPager_scrollScalingMode,
                                CarouselConfig.SCROLL_MODE_BIG_CURRENT);

                float bigScale = a.getFloat(R.styleable.CarouselViewPager_bigScale,
                        CarouselConfig.DEFAULT_BIG_SCALE);
                if (bigScale > 1.0f || bigScale < 0.0f) {
                    bigScale = CarouselConfig.DEFAULT_BIG_SCALE;
                    Log.w(TAG, "Invalid bigScale attribute. Default value " +
                            CarouselConfig.DEFAULT_BIG_SCALE + " will be used.");
                }
                mConfig.bigScale = bigScale;

                float smallScale = a.getFloat(R.styleable.CarouselViewPager_smallScale,
                        CarouselConfig.DEFAULT_SMALL_SCALE);
                if (smallScale > 1.0f || smallScale < 0.0f) {
                    smallScale = CarouselConfig.DEFAULT_SMALL_SCALE;
                    Log.w(TAG, "Invalid smallScale attribute. Default value " +
                            CarouselConfig.DEFAULT_SMALL_SCALE + " will be used.");
                } else if (smallScale > bigScale) {
                    smallScale = bigScale;
                    Log.w(TAG, "Invalid smallScale attribute. Value " + bigScale +
                            " will be used.");
                }
                mConfig.smallScale = smallScale;

                mMinPagesOffset = (int) a.getDimension(R.styleable.CarouselViewPager_minPagesOffset,
                        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, dm));
                mSidePagesVisiblePart =
                        a.getFloat(R.styleable.CarouselViewPager_sidePagesVisiblePart,
                                DEFAULT_SIDE_PAGES_VISIBLE_PART);
                mWrapPadding = (int) a.getDimension(R.styleable.CarouselViewPager_wrapPadding,
                        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, dm));
            }
        } finally {
            if (a != null) {
                a.recycle();
            }
        }
        
        mGestureListener = new SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (getCarouselAdapter() != null) {
                    getCarouselAdapter().sendSingleTap(mTouchedView, mTouchedItem);
                }
                mTouchedView = null;
                mTouchedItem = null;
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (getCarouselAdapter() != null) {
                    getCarouselAdapter().sendDoubleTap(mTouchedView, mTouchedItem);
                }
                mTouchedView = null;
                mTouchedItem = null;
                return true;
            }
        };

        mGestureDetector = new GestureDetector(context, mGestureListener);
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
        if (adapter == null) {
            return;
        }

        if (adapter.getClass() != CarouselPagerAdapter.class) {
            throw new ClassCastException("Adapter must be instance of CarouselPagerAdapter class.");
        }

        setOnPageChangeListener((OnPageChangeListener) adapter);
//установка текущего левела
        //setCurrentItem(((CarouselPagerAdapter) adapter).getItemPosition(mItem));
        setCurrentItem(((CarouselPagerAdapter) adapter).getFirstPosition());
    }

    private void setCurrentItem(Fragment item) {
    }

    private CarouselPagerAdapter getCarouselAdapter() {
        PagerAdapter adapter = getAdapter();
        if (adapter == null) {
            return null;
        }
        return (CarouselPagerAdapter) adapter;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        CarouselState ss = new CarouselState(super.onSaveInstanceState());
        ss.position = getCurrentItem();

        PagerAdapter adapter = getAdapter();
        if (adapter == null) {
            ss.itemsCount = 0;
        } else {
            ss.itemsCount = adapter.getCount();
        }
        ss.infinite = mConfig.infinite;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof CarouselState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        CarouselState ss = (CarouselState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        if (getAdapter() == null) {
            return;
        }

        if (ss.infinite && !mConfig.infinite) {
            int itemsCount = getAdapter().getCount();
            if (itemsCount == 0) {
                return;
            }

            int offset = (ss.position - ss.itemsCount / 2) % itemsCount;
            if (offset >= 0) {
                setCurrentItem(offset);
            } else {
                setCurrentItem(ss.itemsCount / CarouselConfig.LOOPS + offset);
            }
        } else if (!ss.infinite && mConfig.infinite) {
            setCurrentItem(ss.itemsCount * CarouselConfig.LOOPS / 2 + ss.position);
        } else {
            setCurrentItem(ss.position);
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mSizeChanged = true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
         final int height = heigth_frag;

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

            int pageContentHeight = (int)(width_frag);
            if (heightMode == MeasureSpec.AT_MOST ||
                    pageContentHeight + 2*mWrapPadding > height) {//1111
                        heightMeasureSpec = MeasureSpec.makeMeasureSpec(heigth_frag, heightMode);
                    }

            // FIXME Supported only FrameLayout as parent
            if (!(getLayoutParams() instanceof FrameLayout.LayoutParams)) {
                throw new UnsupportedOperationException("Parent layout should be instance of " +
                        "FrameLayout.");
            } else {
                if (!parentHasExactDimensions()) {
                    throw new UnsupportedOperationException("Parent layout should have exact " +
                            "dimensions.");
                }

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        mViewPagerWidth = getMeasuredWidth();
        mViewPagerHeight = getMeasuredHeight();

        if (calculatePageLimitAndMargin()) {
            setOffscreenPageLimit(mConfig.pageLimit);
            setPageMargin(mConfig.pageMargin);
        }

        if (DEBUG) {
            Log.d(TAG, mConfig.toString());
        }
    }

    private boolean parentHasExactDimensions() {
        ViewGroup.LayoutParams params = ((ViewGroup) getParent()).getLayoutParams();
        return !(params.width == ViewGroup.LayoutParams.WRAP_CONTENT ||
                params.height == ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private String getModeDescription(int mode) {
        if (mode == MeasureSpec.AT_MOST) {
            return "AT_MOST";
        }

        if (mode == MeasureSpec.EXACTLY) {
            return "EXACTLY";
        }

        if (mode == MeasureSpec.UNSPECIFIED) {
            return  "UNSPECIFIED";
        }

        return "UNKNOWN";
    }

    private int getPageContentWidth() {
        return mResources.getDimensionPixelSize(mPageContentWidthId);
    }
    
    private int getPageContentHeight() {
        return mResources.getDimensionPixelSize(mPageContentHeightId);
    }
    
     /**
     * @return True, if config was successfully updated.
     */
    private boolean calculatePageLimitAndMargin() {
        if (mViewPagerWidth == 0 || mViewPagerHeight == 0) {
            return false;
        }
//2222
        int contentSize;
            contentSize = (int)(width_frag*0.985f);

        int viewSize = mViewPagerWidth;
        int minOffset = 0;
        switch (mConfig.scrollScalingMode) {
            case CarouselConfig.SCROLL_MODE_BIG_CURRENT: {
                minOffset = (int) (mConfig.getDiffScale() * contentSize / 2) + mMinPagesOffset;
                contentSize *= mConfig.smallScale;
                break;
            }
        }

        if (contentSize + 2*minOffset > viewSize) {
            if (DEBUG) {
                Log.d(TAG, "Page content is too large.");
            }
            return false;
        }

        while (true) {
            if (mSidePagesVisiblePart < 0.0f) {
                mSidePagesVisiblePart = 0.0f;
                break;
            }
            if (contentSize + 2*contentSize*(mSidePagesVisiblePart) + 2*minOffset <= viewSize) {
                break;
            }
            mSidePagesVisiblePart -= 0.07f;
        }

        int fullPages = 1;
        final int s = viewSize - (int) (2*contentSize* mSidePagesVisiblePart);

        while (minOffset + (fullPages+1)*(contentSize + minOffset) <= s) {
            fullPages++;
        }

        if (fullPages != 0 && fullPages % 2 == 0) {
            fullPages--;
        }

        int offset = (s - fullPages * contentSize) / (fullPages + 1);
        int pageLimit;
        if (Math.abs(mSidePagesVisiblePart) > 1e-6) {
            pageLimit = (fullPages + 2) - 1;
        } else {
            if (fullPages > 1) {
                pageLimit = fullPages - 1;
            } else {
                pageLimit = 1;
            }
        }
        // Reserve pages for correct scrolling
        pageLimit = 2*pageLimit + pageLimit / 2;
        mConfig.pageLimit = pageLimit;

        mConfig.pageMargin = -(viewSize - contentSize - offset);
        return true;
    }

    public static class CarouselState extends BaseSavedState {
        int position;
        int itemsCount;
        boolean infinite;

        public CarouselState(Parcelable superState) {
            super(superState);
        }

        private CarouselState(Parcel in) {
            super(in);
            position = in.readInt();
            itemsCount = in.readInt();
            infinite = in.readInt() == 0;
        }

        public static final Parcelable.Creator<CarouselState> CREATOR =
                new Parcelable.Creator<CarouselState>() {
                    @Override
                    public CarouselState createFromParcel(Parcel in) {
                        return new CarouselState(in);
                    }

                    @Override
                    public CarouselState[] newArray(int size) {
                        return new CarouselState[0];
                    }
                };

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(position);
            out.writeInt(itemsCount);
            out.writeInt(infinite ? 1 : 0);
        }
    }

	@Override
	public boolean onTouch(View view, MotionEvent e) {
		mTouchedView = view;
        mTouchedItem = (Parcelable) view.getTag();
        return mGestureDetector.onTouchEvent(e);
	}
}
