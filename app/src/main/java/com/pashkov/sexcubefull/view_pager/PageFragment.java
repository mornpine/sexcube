package com.pashkov.sexcubefull.view_pager;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pashkov.sexcubefull.R;
import com.pashkov.sexcubefull.MyPageItem;
import com.pashkov.sexcubefull.sexrule;
import com.pashkov.sexcubefull.MyPageFragment;
//import com.pashkov.sexcubefull.LoadLevels;
//import com.pashkov.sexcubefull.MyPrefs;

public abstract class PageFragment<T extends Parcelable> extends Fragment {


    int heigth = (int)(MyPageFragment.pxHeight);
    int width = (int)(MyPageFragment.pxWeigth);


    
    int imgHeigth = (int) (width/9.3f);
    int otstupLeft = (int)(imgHeigth*3f);
    int otstupMegduImg = (int)(imgHeigth/30);
    int otstupTopImg = (int) (imgHeigth/30);

    static ImageView levelTimer, levelBlock;

    MyPageItem item;
    View pageContent;
    PageLayout pageLayout;

    static Context contectApp;
    OvershootInterpolator inter2;
    LinearLayout line0;

    CheckBox checkBox0, checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6, checkBox7, checkBox8, checkBox9, checkBox10, checkBox11, checkBox12, checkBox13, checkBox14, checkBox15, checkBox16, checkBox17, checkBox18, checkBox19, checkBox20, checkBox21, checkBox22, checkBox23, checkBox24, checkBox25, checkBox26, checkBox27, checkBox28, checkBox29, checkBox30;
    CheckBox checkBox31, checkBox32, checkBox33, checkBox34, checkBox35, checkBox36, checkBox37, checkBox38, checkBox39, checkBox40, checkBox41, checkBox42, checkBox43;
    /**
     * @param pageLayout Layout of page.
     * @param pageItem Item for customize page content view.
     * @return View of page content.
     */
    public abstract View setupPage(PageLayout pageLayout, MyPageItem pageItem);

    public static Bundle createArgs(int pageLayoutId, Parcelable item) {
        Bundle args = new Bundle();
        args.putInt("page_layout_id", pageLayoutId);
        args.putParcelable("item", item);


        return args;
    }

    boolean mAdded = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
            int pageLayoutId = getArguments().getInt("page_layout_id");
            pageLayout = (PageLayout) inflater.inflate(pageLayoutId, container, false);

        CarouselConfig config = CarouselConfig.getInstance();
        float scale;
        if (config.scrollScalingMode == CarouselConfig.SCROLL_MODE_BIG_CURRENT) {
            scale = config.smallScale;
        } else {
            scale = config.bigScale;
        }
        pageLayout.setScaleBoth(scale);

//TODO PageFragment change item from code
        item = getArguments().getParcelable("item");
        pageContent = setupPage(pageLayout, item);
        if (pageContent != null) {
        	pageContent.setOnTouchListener((CarouselViewPager) container);
            pageContent.setTag(item);

            //Here I can change visualization of my viewpager blocks
            //pageContent.setBackgroundColor(34);
//TODO setParamsViewsLevels ()
            setParamsViewsLevels();

        }
        return pageLayout;
    }
    
    private static Drawable getDrawableFromUrl(final String url, Context context) throws IOException {
            return Drawable.createFromPath(Environment.getExternalStoragePublicDirectory(url).toString());
    }

       
    public void setParamsViewsLevels () {
    	TextView textPoza = (TextView) pageContent.findViewById(R.id.textPoza);
    	ImageView imgPoza = (ImageView) pageContent.findViewById(R.id.imgPozaView);
    	LinearLayout layPozaOk = (LinearLayout) pageContent.findViewById(R.id.linearPozaOk); 
    	line0 = (LinearLayout) pageContent.findViewById(R.id.LinerPolosa0);
        //System.gc();
        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) imgPoza.getLayoutParams();
        params.height = (int)(width);
        params.width = (int)(width);
        imgPoza.setLayoutParams(params);
        
    	ViewGroup.MarginLayoutParams paramsPolosa = (ViewGroup.MarginLayoutParams) layPozaOk.getLayoutParams();    	
        paramsPolosa.height = (int)(width*0.15f);
        //paramsPolosa.width = (int)(width);
        layPozaOk.setLayoutParams(paramsPolosa);
        
       
       // levelTimer = (ImageView) pageContent.findViewById(R.id.img_timer);
       // levelBlock = (ImageView) pageContent.findViewById(R.id.img_block);
   
        switch (item.getmLevelTitle()){
            case 0: {
            	imgPoza.setBackgroundResource(R.drawable.k0);
            	textPoza.setText(R.string.pos_w0);
            	checkBox0 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox0, 0);
                break;}     
            case 1: {
            	imgPoza.setBackgroundResource(R.drawable.k1);
            	textPoza.setText(R.string.pos_w1);
            	checkBox1 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox1, 1);
                break;}      
            case 2: {
            	imgPoza.setBackgroundResource(R.drawable.k2);
            	textPoza.setText(R.string.pos_w2);
            	checkBox2 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox2, 2);
                break;}      
            case 3: {
            	imgPoza.setBackgroundResource(R.drawable.k3);
            	textPoza.setText(R.string.pos_w3);
            	checkBox3 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox3, 3);
                break;}      
            case 4: {
            	imgPoza.setBackgroundResource(R.drawable.k4);
            	textPoza.setText(R.string.pos_w4);
            	checkBox4 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox4, 4);
                break;}      
            case 5: {
            	imgPoza.setBackgroundResource(R.drawable.k5);
            	textPoza.setText(R.string.pos_w5);
            	checkBox5 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox5, 5);
                break;}      
            case 6: {
            	imgPoza.setBackgroundResource(R.drawable.k6);
            	textPoza.setText(R.string.pos_w6);
            	checkBox6 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox6, 6);
                break;}      
            case 7: {
            	imgPoza.setBackgroundResource(R.drawable.k7);
            	textPoza.setText(R.string.pos_w7);     
            	checkBox7 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox7, 7);
                break;} 
            case 8: {
            	imgPoza.setBackgroundResource(R.drawable.k8);
            	textPoza.setText(R.string.pos_w8);
            	checkBox8 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox8, 8);
                break;} 
            case 9: {
            	imgPoza.setBackgroundResource(R.drawable.k9);
            	textPoza.setText(R.string.pos_w9);
            	checkBox9 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox9, 9);
                break;}      
            case 10: {
            	imgPoza.setBackgroundResource(R.drawable.k10);
            	textPoza.setText(R.string.pos_w10);
            	checkBox10 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox10, 10);
            	System.gc();
                break;}   
            case 11: {
            	imgPoza.setBackgroundResource(R.drawable.k11);
            	textPoza.setText(R.string.pos_w11);
            	checkBox11 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox11, 11);
                break;}      
            case 12: {
            	imgPoza.setBackgroundResource(R.drawable.k12);
            	textPoza.setText(R.string.pos_w12);
            	checkBox12 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox12, 12);
                break;}      
            case 13: {
            	imgPoza.setBackgroundResource(R.drawable.k13);
            	textPoza.setText(R.string.pos_w13);
            	checkBox13 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox13, 13);
                break;}      
            case 14: {
            	imgPoza.setBackgroundResource(R.drawable.k14);
            	textPoza.setText(R.string.pos_w14);
            	checkBox14 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox14, 14);
                break;}      
            case 15: {
            	imgPoza.setBackgroundResource(R.drawable.k15);
            	textPoza.setText(R.string.pos_w15);
            	checkBox15 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox15, 15);
                break;}      
            case 16: {
            	imgPoza.setBackgroundResource(R.drawable.k16);
            	textPoza.setText(R.string.pos_w16);
            	checkBox16 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox16, 16);
                break;}      
            case 17: {
            	imgPoza.setBackgroundResource(R.drawable.k17);
            	textPoza.setText(R.string.pos_w17);
            	checkBox17 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox17, 17);
                break;} 
            case 18: {
            	imgPoza.setBackgroundResource(R.drawable.k18);
            	textPoza.setText(R.string.pos_w18);
            	checkBox18 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox18, 18);
                break;} 
            case 19: {
            	imgPoza.setBackgroundResource(R.drawable.k19);
            	textPoza.setText(R.string.pos_w19);
            	checkBox19 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox19, 19);
                break;}      
            case 20: {
            	imgPoza.setBackgroundResource(R.drawable.k20);
            	textPoza.setText(R.string.pos_w20);
            	checkBox20 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox20, 20);
            	System.gc();
                break;}      
            case 21: {
            	imgPoza.setBackgroundResource(R.drawable.k21);
            	textPoza.setText(R.string.pos_w21);
            	checkBox21 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox21, 21);
                break;}      
            case 22: {
            	imgPoza.setBackgroundResource(R.drawable.k22);
            	textPoza.setText(R.string.pos_w22);
            	checkBox22 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox22, 22);
                break;}      
            case 23: {
            	imgPoza.setBackgroundResource(R.drawable.k23);
            	textPoza.setText(R.string.pos_w23);
            	checkBox23 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox23, 23);
                break;}      
            case 24: {
            	imgPoza.setBackgroundResource(R.drawable.k24);
            	textPoza.setText(R.string.pos_w24);
            	checkBox24 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox24, 24);
                break;}      
            case 25: {
            	imgPoza.setBackgroundResource(R.drawable.k25);
            	textPoza.setText(R.string.pos_w25);
            	checkBox25 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox25, 25);
                break;}      
            case 26: {
            	imgPoza.setBackgroundResource(R.drawable.k26);
            	textPoza.setText(R.string.pos_w26);
            	checkBox26 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox26, 26);
                break;}      
            case 27: {
            	imgPoza.setBackgroundResource(R.drawable.k27);
            	textPoza.setText(R.string.pos_w27);
            	checkBox27 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox27, 27);
                break;} 
            case 28: {
            	imgPoza.setBackgroundResource(R.drawable.k28);
            	textPoza.setText(R.string.pos_w28);
            	checkBox28 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox28, 28);
                break;} 
            case 29: {
            	imgPoza.setBackgroundResource(R.drawable.k29);
            	textPoza.setText(R.string.pos_w29);
            	checkBox29 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox29, 29);
                break;}      
            case 30: {
            	imgPoza.setBackgroundResource(R.drawable.k30);
            	textPoza.setText(R.string.pos_w30);
            	checkBox30 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox30, 30);
            	System.gc();
                break;}      
            case 31: {
            	imgPoza.setBackgroundResource(R.drawable.k31);
            	textPoza.setText(R.string.pos_w31);
            	checkBox31 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox31, 31);
                break;}      
            case 32: {
            	imgPoza.setBackgroundResource(R.drawable.k32);
            	textPoza.setText(R.string.pos_w32);
            	checkBox32 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox32, 32);
                break;}      
            case 33: {
            	imgPoza.setBackgroundResource(R.drawable.k33);
            	textPoza.setText(R.string.pos_w33);
            	checkBox33 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox33, 33);
                break;}      
            case 34: {
            	imgPoza.setBackgroundResource(R.drawable.k34);
            	textPoza.setText(R.string.pos_w34);
            	checkBox34 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox34, 34);
                break;}      
            case 35: {
            	imgPoza.setBackgroundResource(R.drawable.k35);
            	textPoza.setText(R.string.pos_w35);
            	checkBox35 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox35, 35);
                break;}      
            case 36: {
            	imgPoza.setBackgroundResource(R.drawable.k36);
            	textPoza.setText(R.string.pos_w36);
            	checkBox36 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox36, 36);
                break;}      
            case 37: {
            	imgPoza.setBackgroundResource(R.drawable.k37);
            	textPoza.setText(R.string.pos_w37);
            	checkBox37 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox37, 37);
                break;} 
            case 38: {
            	imgPoza.setBackgroundResource(R.drawable.k38);
            	textPoza.setText(R.string.pos_w38);
            	checkBox38 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox38, 38);
                break;} 
            case 39: {
            	imgPoza.setBackgroundResource(R.drawable.k39);
            	textPoza.setText(R.string.pos_w39);
            	checkBox39 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox39, 39);
                break;}      
            case 40: {
            	imgPoza.setBackgroundResource(R.drawable.k40);
            	textPoza.setText(R.string.pos_w40);
            	checkBox40 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox40, 40);
            	System.gc();
                break;}      
            case 41: {
            	imgPoza.setBackgroundResource(R.drawable.k41);
            	textPoza.setText(R.string.pos_w41);
            	checkBox41 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox41, 41);
                break;}      
            case 42: {
            	imgPoza.setBackgroundResource(R.drawable.k42);
            	textPoza.setText(R.string.pos_w42);
            	checkBox42 = (CheckBox) pageContent.findViewById(R.id.checkBoxPozaOk); 
            	checkBoxControl(checkBox42, 42);
                break;}      
            default: { break; }
        }

        //System.gc();


        int levelNomer = item.getmLevelTitle();
        
    }
    
    public void checkBoxControl (final CheckBox checkbox, final Integer nomer) {    	
    	ViewGroup.MarginLayoutParams paramsPolosa = (ViewGroup.MarginLayoutParams) checkbox.getLayoutParams();     
        paramsPolosa.height = (int)(width*0.15f);
        paramsPolosa.width = (int)(width*0.15f);
        checkbox.setLayoutParams(paramsPolosa);
        
    	final SharedPreferences sPref = sexrule.contextApp.getSharedPreferences("pozes",android.content.Context.MODE_PRIVATE);
		
		Boolean isChecks = sPref.getBoolean("checkP"+nomer, true);
		if (isChecks == null) {
			sPref.edit().putBoolean("checkP"+nomer, true).commit();
			checkbox.setChecked(true);
		} else if (isChecks == true){
			checkbox.setChecked(true);
		} else if (isChecks == false){
			checkbox.setChecked(false);
		}		
        
        checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				inter2 = new OvershootInterpolator();
				sexrule.imgOkBut.animate().scaleX(1f).scaleY(1f).setStartDelay(0).setDuration(600).setInterpolator(inter2);
			
				if (checkbox.isChecked()) {
					//sPref.edit().putBoolean("checkP"+nomer, true).commit();
					Toast.makeText(sexrule.contextApp, getResources().getText(R.string.clickcheckNo), Toast.LENGTH_SHORT).show(); 
				} else {
					// sPref.edit().putBoolean("checkP"+nomer, false).commit();
					 Toast.makeText(sexrule.contextApp, getResources().getText(R.string.clickcheckOk), Toast.LENGTH_SHORT).show();
				}
			     			    
                Boolean premiumOk = sPref.getBoolean("allOk", true);
                if (premiumOk) {
					if (checkbox.isChecked()) {
						sPref.edit().putBoolean("checkP"+nomer, true).commit();
						//Toast.makeText(sexrule.contextApp, getResources().getText(R.string.clickcheckNo), Toast.LENGTH_SHORT).show(); 
					} else {
						 sPref.edit().putBoolean("checkP"+nomer, false).commit();
						 //Toast.makeText(sexrule.contextApp, getResources().getText(R.string.clickcheckOk), Toast.LENGTH_SHORT).show();
					}
                }
			}
		});
    }
  
}
