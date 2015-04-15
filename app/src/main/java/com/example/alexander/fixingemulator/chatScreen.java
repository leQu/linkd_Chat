package com.example.alexander.fixingemulator;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class chatScreen extends ActionBarActivity {

    Message [] messageArray = new Message[0];
    int messagearraySize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

  /**     if(!isMessages){
            int i = 0;
            while(i <= arraySize){
                messageArray[i] = new Message();
                messageArray[i].setMessage("");
                i++;
            }
        }
**/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        setTitle(MainActivity.getNameOfChat());
        createMessageListView();


        final EditText inputTextLine = (EditText) findViewById(R.id.inputTextLine);
        //final TextView mainText1 = (TextView) findViewById(R.id.mainText1);
        inputTextLine.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(final CharSequence textToPrint, int start, int before, int count) {
                    Button button1 = (Button) findViewById(R.id.go_button);
                    button1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String tempMessage = String.valueOf(textToPrint);
                            if (tempMessage.trim().length() > 0) {
                                inputTextLine.setText("");
                                createMessage(tempMessage);
                                // mainText1.setText(textToPrint);
                            }
                        }
                    });
                }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void createMessageListView() {
        ListView messageList = (ListView) findViewById(R.id.displayMessages);


            String [] messageStrings = new String[messagearraySize];
            int i = 0;
            while(i < messagearraySize){
                messageStrings[i] = messageArray[i].getMessage();
                i++;
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, messageStrings);
            messageList.setAdapter(adapter);
            messageList.setTextFilterEnabled(true);

    }

    private void createMessage(String s) {
            expandArray();
            Message newMessage = new Message();
            newMessage.setSelf(true);
            newMessage.setMessage(s);
            newMessage.setFromName("user");
            int i = 0;

            messageArray[(messagearraySize-1)] = newMessage;
            createMessageListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       getMenuInflater().inflate(R.menu.menu_chat_screen, menu);
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

    public void expandArray(){
        Message[] newArray = new Message[messageArray.length + 1];
        System.arraycopy(messageArray, 0, newArray, 0, messageArray.length);
        messagearraySize++;
        messageArray = newArray;
    }
}