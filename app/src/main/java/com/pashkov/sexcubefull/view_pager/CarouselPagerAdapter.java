package com.pashkov.sexcubefull.view_pager;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;

import com.pashkov.sexcubefull.MyPageItem;
import com.pashkov.sexcubefull.sexrule;

import java.util.ArrayList;
import java.util.List;

public class CarouselPagerAdapter<T extends Parcelable> extends FragmentPagerAdapter
        implements OnPageChangeListener {
    private static final String TAG = CarouselViewPager.class.getSimpleName();
    private CarouselConfig mConfig;
    private int mPagesCount;
    private int mFirstPosition;

    private FragmentManager mFragmentManager;
    private List<MyPageItem> mItems;
    public static int mCurrentPosition;

    private Class mPageFragmentClass;
    private int mPageLayoutId;

    private Context mContext;

    private PageFragment pf;
    
    private OnPageClickListener<T> mCallback;

    public CarouselPagerAdapter(FragmentManager fragmentManager,
                                Class pageFragmentClass,
                                int pageLayoutId,
                                List<T> items,
                                Context context) {
        super(fragmentManager);
        mConfig = CarouselConfig.getInstance();
        mFragmentManager = fragmentManager;
        mPageFragmentClass = pageFragmentClass;
        mPageLayoutId = pageLayoutId;
        mContext = context;
        if (items == null) {
            mItems = new ArrayList<MyPageItem>(0);
        } else {
            mItems = (List<MyPageItem>) items;
        }
        mPagesCount = mItems.size();
        if (mConfig.infinite) {
            mFirstPosition = mPagesCount * CarouselConfig.LOOPS / 2;
        }
    }
    
    public void setOnPageClickListener(OnPageClickListener<T> listener) {
        mCallback = listener;
    }

    public void sendSingleTap(View view, T item) {
        if (mCallback != null) {
            mCallback.onSingleTap(view, item);
        }
    }

    public void sendDoubleTap(View view, T item) {
        if (mCallback != null) {
            mCallback.onDoubleTap(view, item);
        }
    }


    @Override
    public Fragment getItem(int position) {
        if (mConfig.infinite) {
            position = position % mPagesCount;
        }

        try {
            pf = (PageFragment) mPageFragmentClass.newInstance();
            pf.setArguments(PageFragment.createArgs(mPageLayoutId, mItems.get(position)));
            return pf;
        } catch (IllegalAccessException e) {
            Log.w(TAG, e.getMessage());
        } catch (InstantiationException e) {
            Log.w(TAG, e.getMessage());
        }
        return null;
    }
  
    @Override
    public int getCount() {
        if (mConfig.infinite) {
//TODO Looping te viewpager
            return mPagesCount * CarouselConfig.LOOPS;
        }
        return mPagesCount;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        switch (mConfig.scrollScalingMode) {
            case CarouselConfig.SCROLL_MODE_BIG_CURRENT: {
//TODO curent and next LinerLayout-PageLayout while scroled
            	//position = sexrule.mViewPager.getCurrentItem();
                PageLayout current = getPageView(position);
                PageLayout next = getPageView(position + 1);

                if (current != null) {
                    current.setScaleBoth(mConfig.bigScale
                            - mConfig.getDiffScale() * positionOffset);
                }

                if (next != null) {
                    next.setScaleBoth(mConfig.smallScale
                            + mConfig.getDiffScale() * positionOffset);
                }

                break;
            }
        }
    }

    @Override
    public void onPageSelected(int position) {

        mCurrentPosition = position;
        int scalingPages = CarouselConfig.getInstance().pageLimit;

        // Fix fast scroll scaling bug
        if (mConfig.scrollScalingMode == CarouselConfig.SCROLL_MODE_BIG_CURRENT) {
            scaleAdjacentPages(position, scalingPages, mConfig.smallScale);
        }
    }

    /**
     * @param position Position of the current page.
     * @param scalingPages The number of pages on both sides of the current page,
     *                     which must be scaled.
     * @param scale Scale value.
     */
    private void scaleAdjacentPages(int position, int scalingPages, float scale) {
        if (scalingPages == 0) {
            return;
        }
        for (int i = 0; i < scalingPages / 2; i++) {
            PageLayout prevSidePage = getPageView(position - (i + 1));
            if (prevSidePage != null) {
                prevSidePage.setScaleBoth(scale);
                mFragmentManager.getFragments().clear();
            }
            PageLayout nextSidePage = getPageView(position + (i + 1));
            if (nextSidePage != null) {
                nextSidePage.setScaleBoth(scale);
               mFragmentManager.getFragments().clear();
            }
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == CarouselViewPager.SCROLL_STATE_IDLE) {
            int scalingPages = CarouselConfig.getInstance().pageLimit;
            if (scalingPages == 0) {
                return;
            }
        }
    }

    public int getFirstPosition() {
        return mFirstPosition;
    }

    private PageLayout getPageView(int position) {
    	//position = sexrule.mViewPager.getCurrentItem();
        String tag = mConfig.getPageFragmentTag(position);
        Fragment f = mFragmentManager.findFragmentByTag(tag);

        if (f != null && f.getView() != null) {
//            NoSaveStateFrameLayout wrapper = (NoSaveStateFrameLayout)f.getView();
//            return (PageLayout)((wrapper != null) ? wrapper.getChildAt(0) : null);
            return (PageLayout) f.getView();
        }
        return null;
    }
    
    /*
    @Override
    public void destroyItem(View collection, int position, Object o) {
        Log.d("DESTROY", "destroying view at position " + position);
        View view = (View) o;
        ((ViewManager) collection).removeView(view);
        view = null;
    }
    */
}
