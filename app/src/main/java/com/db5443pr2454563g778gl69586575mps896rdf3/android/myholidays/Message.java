package com.db5443pr2454563g778gl69586575mps896rdf3.android.myholidays;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

public class Message {

    static class ErrorDialog {

        public ErrorDialog() {
        }

        public static AlertDialog getErrorMessageDialog(Context context, Result result){

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);

            LayoutInflater inf = LayoutInflater.from(context);
            View DialogExtraLayout = inf.inflate(R.layout.error_dialog, null);

            TextView tvMessage = DialogExtraLayout.findViewById(R.id.txtViewErrorMessage);
            TextView tvCaption = DialogExtraLayout.findViewById(R.id.txtViewErrorHeader);

            builder.setView(DialogExtraLayout);

            tvMessage.setText(result.getMessage());
            tvCaption.setText(result.getCaption());

            AlertDialog dlg = builder.create();

            dlg.setCancelable(false);
            dlg.setCanceledOnTouchOutside(true);

            return dlg;
        }

    }

    static class ProgressDialog {

        public ProgressDialog() {
        }

        public static AlertDialog getProgressDialog(Context context, Result result){

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);

            LayoutInflater inf = LayoutInflater.from(context);
            View DialogExtraLayout = inf.inflate(R.layout.sync_dialog_animated, null);

            TextView tvCaption = DialogExtraLayout.findViewById(R.id.txtViewProgressHeader);

            builder.setView(DialogExtraLayout);
            tvCaption.setText(result.getCaption());

            AlertDialog dlg = builder.create();

            dlg.setCanceledOnTouchOutside(false);

            return dlg;

        }

    }

}
