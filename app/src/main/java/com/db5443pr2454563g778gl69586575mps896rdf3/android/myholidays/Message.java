package com.db5443pr2454563g778gl69586575mps896rdf3.android.myholidays;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

public class Message {

    static class Error {

        public Error() {
        }

        public static void displayErrorMessage(Context context, Result result){

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inf = LayoutInflater.from(context);
            View errorDialogExtraLayout;

            TextView tvMessage;
            TextView tvCaption;

            errorDialogExtraLayout = inf.inflate(R.layout.cloud_sync_error, null);
            tvMessage = errorDialogExtraLayout.findViewById(R.id.txtViewErrorMessage);
            tvCaption = errorDialogExtraLayout.findViewById(R.id.txtViewErrorHeader);

            builder.setView(errorDialogExtraLayout);

            tvMessage.setText(result.getMessage());
            tvCaption.setText(result.getCaption());

            AlertDialog dlg = builder.create();

            dlg.setCancelable(false);
            dlg.setCanceledOnTouchOutside(true);

            dlg.show();

        }

    }

}
