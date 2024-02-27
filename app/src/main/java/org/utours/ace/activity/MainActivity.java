package org.utours.ace.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import org.utours.ace.activity.auth.ConnectionActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var cls = determineActivityClass();
        var intent = new Intent(this, cls);

        startActivity(intent);
        finish();
    }

    private Class<?> determineActivityClass() {
        return ConnectionActivity.class;
    }

}