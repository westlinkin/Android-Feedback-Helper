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

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.westlinkin.android_feedback_helper.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Wesley Lin on 3/24/15.
 */
public class FeedbackDialog extends DialogFragment {
    private static final String TAG = "FeedbackDialog";
    private static final String ACCOUNT_PERMISSION_NAME = "android.permission.GET_ACCOUNTS";
    private static final String EMAIL_DOMAINS = "email_domains";

    public static FeedbackDialog getInstance(String[] emailDomains) {
        FeedbackDialog feedbackDialog = new FeedbackDialog();
        Bundle args = new Bundle();
        args.putStringArray(EMAIL_DOMAINS, emailDomains);
        feedbackDialog.setArguments(args);
        return feedbackDialog;
    }

    public interface OnDialogButtonsClickListener {
        void onSendClicked(String feedbackMessage, String userEmailAddress);
    }

    private OnDialogButtonsClickListener onDialogButtonsClickListener;

    public void setOnDialogButtonsClickListener(OnDialogButtonsClickListener onDialogButtonsClickListener) {
        this.onDialogButtonsClickListener = onDialogButtonsClickListener;
    }

    private String[] emailDomains;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        emailDomains = getArguments().getStringArray(EMAIL_DOMAINS);
    }

    private boolean useEditText = true;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.afh_dialog_title));

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.afh_dialog_view, null);
        final EditText feedbackMsg = (EditText) view.findViewById(R.id.afh_feedback_edittext);
        final AutoCompleteTextView emailEditText = (AutoCompleteTextView) view.findViewById(R.id.afh_email_edittext);
        final Spinner emailSpinner = (Spinner) view.findViewById(R.id.afh_email_spinner);

        // permission check, use Spinner or EditText
        PackageManager pm = getActivity().getPackageManager();
        boolean accountPermissionGranted = (PackageManager.PERMISSION_GRANTED == pm.checkPermission(ACCOUNT_PERMISSION_NAME, getActivity().getPackageName()));
        Account[] googleAccounts = null;
        Log.d(TAG, "accountPermissionGranted: " + accountPermissionGranted);
        if (accountPermissionGranted) {
            googleAccounts = getAccounts(getActivity());
        }

        if (googleAccounts != null && googleAccounts.length > 0) {
            useEditText = false;
        }

        // set ui based on {@link useEditText}
        if (useEditText) {
            emailEditText.setVisibility(View.VISIBLE);
            emailSpinner.setVisibility(View.GONE);

            emailEditText.setAdapter(new EmailFilterAdapter(getActivity(), android.R.layout.simple_list_item_1,
                    new ArrayList<String>(Arrays.asList(emailDomains))));

        } else {
            emailEditText.setVisibility(View.GONE);
            emailSpinner.setVisibility(View.VISIBLE);


        }

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
                String userEmailAddress = "";
                if (useEditText) {
                    userEmailAddress = emailEditText.getText().toString().trim();
                } else {
                    //todo: get email address from spinner
//                    userEmailAddress = emailSpinner.getSelectedItem()
                }
                onDialogButtonsClickListener.onSendClicked(feedbackMsg.getText().toString().trim(), userEmailAddress);
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

    private static Account[] getAccounts(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        return accountManager.getAccountsByType("com.google");
    }
}
