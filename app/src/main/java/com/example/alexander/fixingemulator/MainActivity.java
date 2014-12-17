package com.example.alexander.fixingemulator;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.content.Context;
import android.content.Intent;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    static CharSequence nameOfChat;
    String [] recCharArray = {"","","",""};
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        createListView();
        setTitle("Linkd");


        final EditText searchField = (EditText) findViewById(R.id.searchField);
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                Button button2 = (Button) findViewById(R.id.go_button2);

                button2.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        nameOfChat = s;
                        Intent intent = new Intent(context, chatScreen.class);
                        addChatToRec(String.valueOf(s));
                        searchField.setText("");
                        startActivity(intent);

                    }
                });
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void createListView() {
        ListView recenlist = (ListView) findViewById(R.id.recentChatList);

       if(recCharArray[0] != null) {
           ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recCharArray);
           recenlist.setAdapter(adapter);
           recenlist.setTextFilterEnabled(true);
       }


        recenlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "myPos " + position, Toast.LENGTH_LONG).show();
                nameOfChat = recCharArray[position];
                Intent intent = new Intent(context, chatScreen.class);
                addChatToRec(recCharArray[position]);
                startActivity(intent);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static CharSequence getNameOfChat() {
        return nameOfChat;
    }

    public void addChatToRec(String recChat) {
            int tempInt = 0;
            int i = 0;

        while (i<4){
            if(recCharArray[i].equals(recChat)){
                tempInt = i;
                break;
            }
            else{
                i ++;
                tempInt = i;
            }
        }

            if (tempInt == 1){
                recCharArray[1] = recCharArray[0];
                recCharArray[0] = recChat;
            }
            if (tempInt == 2){
                recCharArray[2] = recCharArray[1];
                recCharArray[1] = recCharArray[0];
                recCharArray[0] = recChat;
            }
            if (tempInt == 3 || tempInt == 4){
                recCharArray[3] = recCharArray[2];
                recCharArray[2] = recCharArray[1];
                recCharArray[1] = recCharArray[0];
                recCharArray[0] = recChat;
            }

        createListView();
        //funktion för att spara undan arrayen i databasen.
        /**
         * Fulkodat men pallar inte, arrayen flyttar om alla chatter och lägger den senaste på toppen, därefter ritar den om listan och uppdaterar databasen.
         */
    }


}
