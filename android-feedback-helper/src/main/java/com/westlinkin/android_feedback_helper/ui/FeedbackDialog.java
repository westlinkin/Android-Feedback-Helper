/*
 * Copyright 2015 Wesley Lin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.westlinkin.android_feedback_helper.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.westlinkin.android_feedback_helper.R;

/**
 * Created by Wesley Lin on 3/24/15.
 */
public class FeedbackDialog extends DialogFragment {
    private static final String TAG = "FeedbackDialog";

    public static FeedbackDialog getInstance() {
        return new FeedbackDialog();
    }

    public interface OnDialogButtonsClickListener {
        void onSendClicked(String feedbackMessage);
    }

    private OnDialogButtonsClickListener onDialogButtonsClickListener;

    public void setOnDialogButtonsClickListener(OnDialogButtonsClickListener onDialogButtonsClickListener) {
        this.onDialogButtonsClickListener = onDialogButtonsClickListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.afh_dialog_title));

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.afh_dialog_view, null);
        final EditText feedbackMsg = (EditText) view.findViewById(R.id.afh_feedback_edittext);
        final EditText emailEditText = (EditText) view.findViewById(R.id.afh_email_edittext);
        final Spinner emailSpinner = (Spinner) view.findViewById(R.id.afh_email_spinner);

        // todo: if permission allowed, use emailSpinner, hide emailEditText

        feedbackMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                setSendButtonEnable(!feedbackMsg.getText().toString().trim().isEmpty());
            }
        });

        builder.setPositiveButton(getString(R.string.afh_positive_btn), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (onDialogButtonsClickListener == null)
                    return;

                onDialogButtonsClickListener.onSendClicked(feedbackMsg.getText().toString());
            }
        });

        builder.setView(view);

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        setSendButtonEnable(false);
    }

    private void setSendButtonEnable(boolean enable) {
        AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog == null)
            return;
        Button sendButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
        if (sendButton == null)
            return;
        sendButton.setEnabled(enable);
    }
}
