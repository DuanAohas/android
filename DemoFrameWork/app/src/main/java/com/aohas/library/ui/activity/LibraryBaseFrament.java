package com.aohas.library.ui.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.aohas.library.Library;
import com.aohas.library.ui.util.RelayoutViewTool;
import com.aohas.library.util.CheckUtil;
import com.aohas.library.util.Lg;
import com.aohas.library.util.LibraryCfg;

/**
 * Created by liuyu on 14-5-6.
 */
public abstract class LibraryBaseFrament extends Fragment {

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = onCreateView();
        RelayoutViewTool.relayoutViewWithScale(view, Library.screenWidthScale);
        initWidget(view);
        addListener();
        initData();
        return view;
    }

    public abstract View onCreateView();
    public abstract void initWidget(View view);
    public abstract void addListener();
    public abstract void initData();

    @Override
    public void onDestroy() {
        super.onDestroy();
        try{
            getActivity().unregisterReceiver(receiver);
        }catch (IllegalArgumentException e){
            Lg.e(LibraryBaseFragmentActivity.class.getName(), e.getMessage());
        }
    }

    protected void setReceiver(String action){
        IntentFilter filter = new IntentFilter();
        filter.addAction(action);
        getActivity().registerReceiver(receiver, filter);
    }

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            String values = intent.getStringExtra(LibraryCfg.ACT_FGM_INTENT);
            if(CheckUtil.isEmpty(values)){
                getTransferMsg(intent);
            }else{
                getTransferMsg(values);
            }
        }
    };

    protected void getTransferMsg(String values){
    }

    protected void getTransferMsg(Intent intent){
    }

    protected void setTransferMsg(String action, Intent intent){
        intent.setAction(action);
        getActivity().sendBroadcast(intent);
    }

    protected void setTransferMsg(String action, String values){
        Intent intent = new Intent(action);
        intent.putExtra(LibraryCfg.ACT_FGM_INTENT, values);
        getActivity().sendBroadcast(intent);
    }

}
