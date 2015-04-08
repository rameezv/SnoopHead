package com.rammyapps.snoophead;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;


public class MainActivity extends ActionBarActivity {
    private Switch switchActive;
    private ListView listSettings;
    SharedPreferences sharedpreferences;
    public static final String PREFS = "com.rammyapps.snoophead.prefs";
    public static final String cHEAD = "com.rammyapps.snoophead.prefs.head";
    public static final String cSOUND = "com.rammyapps.snoophead.prefs.sound";
    public static final String cTIME = "com.rammyapps.snoophead.prefs.time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedpreferences = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if (!sharedpreferences.contains(cHEAD)) {
            editor.putInt(cHEAD, R.drawable.head_snoop_default);
        }
        if (!sharedpreferences.contains(cSOUND)) {
            editor.putString(cSOUND, "swed_default.mp3");
        }
        if (!sharedpreferences.contains(cTIME)) {
            editor.putString(cTIME, "1620");
        }
        editor.commit();

        switchActive = (Switch)findViewById(R.id.switchActive);
        switchActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Intent intent = new Intent(MainActivity.this, SnoopHeadService.class);
                    startService(intent);
                } else {
                    stopService(new Intent(MainActivity.this, SnoopHeadService.class));
                }
            }
        });

        switchActive.setChecked(isSnoopAlive(SnoopHeadService.class));

        listSettings = (ListView)findViewById(R.id.listSettings);
        String[] settingsValues = new String[] {
                "Head",
                "Sound",
                "Time"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, settingsValues);
        listSettings.setAdapter(adapter);
        listSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch(position) {
                    case 0:
                        String heads[] = new String[] {"Snoop 1", "Snoop 2", "Seth"};

                        AlertDialog.Builder builderHeads = new AlertDialog.Builder(MainActivity.this);
                        builderHeads.setTitle("Choose a Head");
                        LayoutInflater factory = LayoutInflater.from(MainActivity.this);
                        final View headSelectorView = factory.inflate(R.layout.head_selector_item, null);
                        //builderHeads.setView(headSelectorView);
                        builderHeads.setItems(heads, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // the user clicked on heads[which]
                            }
                        });

                        builderHeads.show();
                        break;
                    case 1:
                        String sounds[] = new String[] {"SWED", "Seth's laugh"};

                        AlertDialog.Builder builderSounds = new AlertDialog.Builder(MainActivity.this);
                        builderSounds.setTitle("Choose a Sound");
                        builderSounds.setItems(sounds, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // the user clicked on sounds[which]
                            }
                        });
                        builderSounds.show();
                        break;
                    case 3:
                        String times[] = new String[] {"4:20", "Always"};

                        AlertDialog.Builder builderTimes = new AlertDialog.Builder(MainActivity.this);
                        builderTimes.setTitle("Choose a Sound");
                        builderTimes.setItems(times, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // the user clicked on time[which]
                            }
                        });
                        builderTimes.show();
                        break;
                    default:
                        // dance
                }
            }

        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_donate) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isSnoopAlive(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
