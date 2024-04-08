package org.utours.ace.common;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import org.utours.ace.R;

public class WifiReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("test connexion . . . exception");
        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if(info != null && !info.isConnected()) {
            // Do your work.
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_missing_network);
            dialog.setCancelable(false);
            dialog.show();

            System.out.println("PAS DE CONNEXION !!!!!!!!!!!!!!!! exception");
        }
    }
}