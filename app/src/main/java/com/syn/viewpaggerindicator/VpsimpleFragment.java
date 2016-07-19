package com.syn.viewpaggerindicator;

import android.app.Fragment;
import android.os.Bundle;
//import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by 孙亚楠 on 2016/7/11.
 */
public class VpsimpleFragment extends Fragment {
    private String mTitle;
    public  static final String BUNDLE_TITLE="title";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle=getArguments();
        if(bundle!=null){
            mTitle=bundle.getString(BUNDLE_TITLE);
        }
        TextView tv=new TextView(getActivity());
        tv.setText(mTitle);
        tv.setGravity(Gravity.CENTER);
        return tv;
    }
    public static  VpsimpleFragment newInstance(String title){
        Bundle bundle=new Bundle();
        bundle.putString(BUNDLE_TITLE,title);
        VpsimpleFragment fragment=new VpsimpleFragment();
        fragment.setArguments(bundle);
        return fragment;
    }



}
