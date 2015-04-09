package com.rammyapps.snoophead;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;


public class MainActivity extends ActionBarActivity {
    Switch switchActive;
    ListView listSettings;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public static final String PREFS = "com.rammyapps.snoophead.prefs";
    public static final String cHEAD = "com.rammyapps.snoophead.prefs.head";
    public static final String cSOUND = "com.rammyapps.snoophead.prefs.sound";
    public static final String cTIME = "com.rammyapps.snoophead.prefs.time";
    public static final String cENABLED = "com.rammyapps.snoophead.prefs.enabled";
    private SimpleAdapter sa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedpreferences = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        if (!sharedpreferences.contains(cHEAD)) {
            editor.putInt(cHEAD, R.drawable.head_snoop_default);
        }
        if (!sharedpreferences.contains(cSOUND)) {
            editor.putString(cSOUND, "swed_default.mp3");
        }
        if (!sharedpreferences.contains(cTIME)) {
            editor.putString(cTIME, "1620");
        }
        if (!sharedpreferences.contains(cENABLED)) {
            editor.putBoolean(cENABLED, false);
        }
        editor.apply();
        switchActive = (Switch)findViewById(R.id.switchActive);
        switchActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(cENABLED, isChecked);
                editor.commit();
                setServiceTime(sharedpreferences.getString(cTIME, "1620"));
            }
        });


        listSettings = (ListView)findViewById(R.id.listSettings);
        sa = setUpList();
        listSettings.setAdapter(sa);
        listSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch(position) {
                    case 0:
                        Head heads[] = new Head[] {
                                new Head("Snoop Dogg 1", R.drawable.head_snoop_default),
                                new Head("Snoop Dogg 2", R.drawable.head_snoop_toque),
                                new Head("Seth Rogen", R.drawable.head_seth)
                        };

                        AlertDialog.Builder builderHeads = new AlertDialog.Builder(MainActivity.this);
                        builderHeads.setTitle("Choose a Head");
                        ListAdapter adapter = new HeadAdapter(MainActivity.this, heads);

                        builderHeads.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        editor.putInt(cHEAD, R.drawable.head_snoop_default);
                                        break;
                                    case 1:
                                        editor.putInt(cHEAD, R.drawable.head_snoop_toque);
                                        break;
                                    case 2:
                                        editor.putInt(cHEAD, R.drawable.head_seth);
                                        break;
                                    default:
                                        editor.putInt(cHEAD, R.drawable.head_snoop_default);
                                        //dance
                                }
                                editor.apply();
                                sa = setUpList();
                                listSettings.setAdapter(sa);
                                dialog.dismiss();
                            }
                        });

                        builderHeads.show();
                        break;
                    case 1:
                        String sounds[] = new String[] {"Smoke Weed Every Day", "Seth Rogen's Laugh"};

                        AlertDialog.Builder builderSounds = new AlertDialog.Builder(MainActivity.this);
                        builderSounds.setTitle("Choose a Sound");
                        builderSounds.setItems(sounds, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch(which) {
                                    case 0:
                                        editor.putString(cSOUND, "swed_default.mp3");
                                        break;
                                    case 1:
                                        editor.putString(cSOUND, "seth_laugh.mp3");
                                        break;
                                    default:
                                        editor.putString(cSOUND, "swed_default.mp3");
                                        //dance
                                }
                                editor.apply();
                                sa = setUpList();
                                listSettings.setAdapter(sa);
                            }
                        });
                        builderSounds.show();
                        break;
                    case 2:
                        String times[] = new String[] {"4:20 pm", "Always"};

                        AlertDialog.Builder builderTimes = new AlertDialog.Builder(MainActivity.this);
                        builderTimes.setTitle("Choose a Time");
                        builderTimes.setItems(times, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch(which) {
                                    case 0:
                                        editor.putString(cTIME, "1620");
                                        break;
                                    case 1:
                                        editor.putString(cTIME, "NULL");
                                        break;
                                    default:
                                        editor.putString(cTIME, "1620");
                                        //dance
                                }
                                editor.apply();
                                sa = setUpList();
                                listSettings.setAdapter(sa);
                                setServiceTime(sharedpreferences.getString(cTIME, "1620"));
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
            Intent intent = new Intent(this, DonateActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void setServiceTime(String timeString) {
        if (!sharedpreferences.getBoolean(cENABLED, false)) {
            if (timeString.equals("NULL") || timeString.length() != 4) {
                stopService(new Intent(MainActivity.this, SnoopHeadService.class));
            } else {
                Intent intent = new Intent(MainActivity.this, SnoopHeadService.class);
                PendingIntent pintent = PendingIntent.getService(MainActivity.this, 0, intent, 0);
                AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                alarm.cancel(pintent);
            }
        } else {
            if (timeString.equals("NULL") || timeString.length() != 4) {
                Intent intent = new Intent(MainActivity.this, SnoopHeadService.class);
                startService(intent);
            } else {
                Calendar cur_cal = new GregorianCalendar();
                cur_cal.setTimeInMillis(System.currentTimeMillis());//set the current time and date for this calendar

                Calendar cal = new GregorianCalendar();
                cal.add(Calendar.DAY_OF_YEAR, cur_cal.get(Calendar.DAY_OF_YEAR));
                cal.set(Calendar.HOUR_OF_DAY, 16);
                cal.set(Calendar.MINUTE, 20);
                cal.set(Calendar.SECOND, cur_cal.get(Calendar.SECOND));
                cal.set(Calendar.MILLISECOND, cur_cal.get(Calendar.MILLISECOND));
                cal.set(Calendar.DATE, cur_cal.get(Calendar.DATE));
                cal.set(Calendar.MONTH, cur_cal.get(Calendar.MONTH));
                Intent intent = new Intent(MainActivity.this, SnoopHeadService.class);
                PendingIntent pintent = PendingIntent.getService(MainActivity.this, 0, intent, 0);
                AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 30*1000, pintent);
            }
        }

    }

      private SimpleAdapter setUpList() {
        switchActive.setChecked(sharedpreferences.getBoolean(cENABLED, false));
        String[][] settingsValues = {
                {"Head","Snoop 1"},
                {"Sound","Smoke Weed Every Day"},
                {"Time","4:20"}
        };
        switch(sharedpreferences.getInt(cHEAD, R.drawable.head_snoop_default)) {
            case R.drawable.head_snoop_toque:
                settingsValues[0][1] = "Snoop Dogg 2";
                break;
            case R.drawable.head_seth:
                settingsValues[0][1] = "Seth Rogen";
                break;
            default:
                settingsValues[0][1] = "Snoop Dogg 1";
        }
        switch(sharedpreferences.getString(cSOUND, "swed_default.mp3")) {
            case "seth_laugh.mp3":
                settingsValues[1][1] = "Seth Rogen's Laugh";
                break;
            default:
                settingsValues[1][1] = "Smoke Weed Every Day";
        }
        switch(sharedpreferences.getString(cTIME, "1620")) {
            case "NULL":
                settingsValues[2][1] = "Always";
                break;
            default:
                settingsValues[2][1] = "4:20 pm";
        }
        ArrayList<HashMap<String,String>> listItems = new ArrayList<>();
        HashMap<String,String> item;
        for(int i=0;i<settingsValues.length;i++){
            item = new HashMap<>();
            item.put( "line1", settingsValues[i][0]);
            item.put( "line2", settingsValues[i][1]);
            listItems.add( item );
        }
        return new SimpleAdapter(this, listItems,
                R.layout.settings_list_item,
                new String[] { "line1","line2" },
                new int[] {R.id.list_item_line_1, R.id.list_item_line_2});
    }

    class Head {
        String name;
        int img;

        public Head(String nm, int im) {
            this.name = nm;
            this.img = im;
        }

        public String getName() {
            return this.name;
        }

        public int getImg() {
            return this.img;
        }
    }

    static class HeadAdapter extends ArrayAdapter {

        private static final int RESOURCE = R.layout.head_selector_item;
        private LayoutInflater inflater;

        static class ViewHolder {
            TextView nameTxVw;
            ImageView imageImVw;
        }

        public HeadAdapter(Context context, Head[] objects)
        {
            super(context, RESOURCE, objects);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewHolder holder;

            if ( convertView == null ) {
                // inflate a new view and setup the view holder for future use
                convertView = inflater.inflate( RESOURCE, null );

                holder = new ViewHolder();
                holder.nameTxVw =
                        (TextView) convertView.findViewById(R.id.headSelectorText);
                holder.imageImVw =
                        (ImageView) convertView.findViewById(R.id.headSelectorImage);
                convertView.setTag( holder );
            }  else {
                // view already defined, retrieve view holder
                holder = (ViewHolder) convertView.getTag();
            }

            Head hd = (Head)getItem(position);
            if ( hd == null ) {
                return null;
            }
            holder.nameTxVw.setText( hd.getName() );
            holder.imageImVw.setImageResource( hd.getImg() );

            return convertView;
        }
    }
}
