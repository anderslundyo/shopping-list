package org.projects.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    ListView listView;
    ArrayList<String> bag;

    public ArrayAdapter getMyAdapter()
    {
        return adapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Needed to get the toolbar to work on older versions
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String name = MyPreferences.getName(this);
        updateUI(name);

        if (savedInstanceState != null)
        {
             bag = savedInstanceState.getStringArrayList("shoppingArray");
        }
        else{
            bag = new ArrayList<>();
        }

        //getting our listiew - you can check the ID in the xml to see that it
        //is indeed specified as "list"
        listView = (ListView) findViewById(R.id.list);
        //here we create a new adapter linking the bag and the
        //listview
        adapter =  new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_checked,bag );

        //setting the adapter on the listview
        listView.setAdapter(adapter);
        //here we set the choice mode - meaning in this case we can
        //only select one item at a time. - Edited to multiple
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);



        final EditText shoppingItem = (EditText)findViewById(R.id.shopping_input);
        final EditText shoppingItemQty = (EditText)findViewById(R.id.shopping_input_qty);


        Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shoppingItemValue = shoppingItem.getText().toString();
                String itemQty = shoppingItemQty.getText().toString();

                bag.add(itemQty + " - " + shoppingItemValue);

                shoppingItem.setText("");
                shoppingItemQty.setText("");
                //The next line is needed in order to say to the ListView
                //that the data has changed - we have added stuff now!
                getMyAdapter().notifyDataSetChanged();
            }
        });

        //add some stuff to the list so we have something
        // to show on app startup
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //ALWAYS CALL THE SUPER METHOD - To be nice!
        super.onSaveInstanceState(outState);
		/* Here we put code now to save the state */
        outState.putStringArrayList("shoppingArray", bag);
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


        if (id == R.id.clear_list){
            this.clearList();
            return true;
        }

        if (item.getItemId()==R.id.action_settings)
        {
            //Start our settingsactivity and listen to result - i.e.
            //when it is finished.
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivityForResult(intent,1);
            //notice the 1 here - this is the code we then listen for in the
            //onActivityResult

        }

        return super.onOptionsItemSelected(item);
    }

    public void deleteItem(View view){
        int checked = listView.getCheckedItemPosition();
        bag.remove(checked);
        getMyAdapter().notifyDataSetChanged();
    }
    public void clearList(){
        bag.clear();
        getMyAdapter().notifyDataSetChanged();
    }

    public void updateUI(String name){
        Toast.makeText(this, "Welcome back " + name, Toast.LENGTH_SHORT).show();
    }

}
