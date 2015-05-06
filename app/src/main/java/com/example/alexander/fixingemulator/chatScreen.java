package com.example.alexander.fixingemulator;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class chatScreen extends ActionBarActivity {

    static Message [] messageArray = new Message[0];
    static int messagearraySize = 0;
    String tempUserName = "Alex";

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
        messageArray = new Message[0];
        messagearraySize = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        setTitle(MainActivity.getNameOfChat());

        new getTask(this).execute();

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
                                createMessage(tempMessage, tempUserName, true);
                                new postTask(chatScreen.this, tempMessage, tempUserName).execute();
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
                messageStrings[i] = messageArray[i].getFromName() + ": " + messageArray[i].getMessage();
                i++;
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, messageStrings);
            messageList.setAdapter(adapter);
            messageList.setTextFilterEnabled(true);

    }

    protected void createMessage(String s, String d, boolean f) {
            expandArray();
            Message newMessage = new Message();
            newMessage.setSelf(f);
            newMessage.setMessage(s);
            newMessage.setFromName(d);
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

    public static void expandArray(){
        Message[] newArray = new Message[messageArray.length + 1];
        System.arraycopy(messageArray, 0, newArray, 0, messageArray.length);
        messagearraySize++;
        messageArray = newArray;
    }
}

class postTask extends AsyncTask<Void, Void, Void>{
    private final chatScreen mainChat;
    String message;
    String user;
    public postTask(chatScreen aChatScreen, String message, String user){
        this.mainChat = aChatScreen;
        this.message = message;
        this.user = user;
    }
    @Override
    protected Void doInBackground(Void... params) {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://188.166.120.241:8080/");
        try {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>(3);
            pairs.add(new BasicNameValuePair("message", message));
            pairs.add(new BasicNameValuePair("user_name", user));
            pairs.add(new BasicNameValuePair("chat_name", String.valueOf(MainActivity.getNameOfChat())));
            post.setEntity(new UrlEncodedFormEntity(pairs));
            HttpResponse response = client.execute(post);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

class getTask extends AsyncTask<Void, Void, Void> {
    String jsonStringTemp;
    private final chatScreen chatty;
    public getTask(chatScreen aChatty){
        chatty = aChatty;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            URL url = new URL("http://188.166.120.241:8080/"+String.valueOf(MainActivity.getNameOfChat()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

            jsonStringTemp = reader.readLine();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        try {
            JSONArray jsonArr = new JSONArray(jsonStringTemp);
            for (int i = 0; i < jsonArr.length(); i++) {
                JSONArray tmp = new JSONArray(jsonArr.get(i).toString());
                chatty.createMessage(tmp.get(1).toString(), tmp.get(0).toString(), false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}