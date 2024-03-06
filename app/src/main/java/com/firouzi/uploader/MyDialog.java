package com.firouzi.uploader;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MyDialog extends BottomSheetDialogFragment {
    private MyDialogEventListener eventListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        eventListener= (MyDialogEventListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_layout,null,false);

        Button okBtn=view.findViewById(R.id.btn_main_save);
        Button cancelBtn=view.findViewById(R.id.btn_main_save2);



        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventListener.onOkButtonClicked();

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }


    public interface MyDialogEventListener{
        void onOkButtonClicked();
    }
}

