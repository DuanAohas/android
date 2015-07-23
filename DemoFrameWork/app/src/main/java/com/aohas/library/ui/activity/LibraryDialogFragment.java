package com.aohas.library.ui.activity;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.aohas.library.Library;
import com.aohas.library.ui.util.RelayoutViewTool;
import com.aohas.library.util.ParcelUtil;

/**
 *
 */
public class LibraryDialogFragment extends DialogFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(ParcelUtil.getLayoutId("library_common_dialog_progressbar"), container, false);
        RelayoutViewTool.relayoutViewWithScale(view, Library.screenWidthScale);
        TextView titleTest = (TextView) view.findViewById(ParcelUtil.getItemId("library_common_dialog_loading_txt"));


        return view;
    }


}
