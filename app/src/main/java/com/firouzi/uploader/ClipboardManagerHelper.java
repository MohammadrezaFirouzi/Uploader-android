package com.firouzi.uploader;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class ClipboardManagerHelper {

    private final Context context;

    public ClipboardManagerHelper(Context context) {
        this.context = context;
    }

    public void copyToClipboard(String text) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Copied Text", text);
        clipboardManager.setPrimaryClip(clipData);
    }
}
