package org.projects.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyDialogFragment.OnPositiveListener {

    MyDialogFragment dialog;
    FirebaseListAdapter<Product> adapter;
    ListView listView;
    ArrayList<Product> bag;
    public FirebaseListAdapter<Product> getMyAdapter()
    {
        return adapter;
    }
    View parent;
    DatabaseReference firebase;


    //This method is the one we need to implement from the
    //interface. It will be called when the user has clicked the
    //positive button (yes button):
    public void onPositiveClicked() {
        //Do your update stuff here to the listview
        //and the bag etc
        //just to show how to get arguments from the bag.
        Toast toast = Toast.makeText(this,
                "Cleared shoppinglist", Toast.LENGTH_LONG);
        toast.show();
        clearList(); //here you can do stuff with the bag and
        //adapter etc.
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Needed to get the toolbar to work on older versions
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        parent = findViewById(R.id.layout_root);

        firebase = FirebaseDatabase.getInstance().getReference().child("items");

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("items");

        FirebaseListOptions<Product> options = new FirebaseListOptions.Builder<Product>()
                .setQuery(query, Product.class)
                .setLayout(android.R.layout.simple_list_item_checked)
                .build();


        String name = MyPreferences.getName(this);
        updateUI(name);

        if (savedInstanceState != null)
        {
             bag = savedInstanceState.getParcelableArrayList("bag");
        }
        else{
            bag = new ArrayList<>();
        }
        Spinner spinner = (Spinner) findViewById(R.id.spinner_qty);

        //getting our listiew - you can check the ID in the xml to see that it
        //is indeed specified as "list"
        listView = (ListView) findViewById(R.id.list);

        //Adapters
        adapter = new FirebaseListAdapter<Product>(options) {
            @Override
            protected void populateView(View v, Product product, int position) {
                // Bind the Chat to the view
                // ...
                TextView textView = (TextView) v.findViewById(android.R.id.text1);
                textView.setTextSize(24);
                textView.setText(product.toString());
            }
        };

        //setting the adapter on the listview
        listView.setAdapter(adapter);
        //here we set the choice mode - meaning in this case we can
        //only select one item at a time. - Edited to multiple
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this, R.array.spinner_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter2);


        final Spinner mySpinner=(Spinner) findViewById(R.id.spinner_qty);

        final EditText shoppingItem = (EditText)findViewById(R.id.shopping_input);


        Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String spinqty = mySpinner.getSelectedItem().toString();
                String shoppingItemValue = shoppingItem.getText().toString();

                int qty = Integer.parseInt(spinqty);

                Product p = new Product(shoppingItemValue, qty);
                Log.d("product",p.toString());

               // bag.add(p);
                firebase.push().setValue(p); //see later for this reference


                shoppingItem.setText("");
                //The next line is needed in order to say to the ListView
                //that the data has changed - we have added stuff now!
                getMyAdapter().notifyDataSetChanged();
            }
        });

        //add some stuff to the list so we have something
        // to show on app startup
    }

    @Override protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //ALWAYS CALL THE SUPER METHOD - To be nice!
        super.onSaveInstanceState(outState);
		/* Here we put code now to save the state */
        outState.putParcelableArrayList("bag",bag);
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
            dialog = new MyDialogFragment.MyDialog();
            //Here we show the dialog
            //The tag "MyFragement" is not important for us.
            dialog.show(getFragmentManager(), "MyFragment");
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

        if (id == R.id.share){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, convertListToString());
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
        }

        return super.onOptionsItemSelected(item);
    }
    public String convertListToString()
    {
        String result = "";
        for (int i = 0; i<adapter.getCount();i++)
        {
            Product p = (Product) adapter.getItem(i);
            //TODO....add the product string to the result
            //to add the product and insert a new line you can do something like this : result = result + "\n";
            result = result + p.name + " - " + p.quantity + " \n";
        }
        return "Shopping list: \n" + result;
    }

    public void deleteItem(View view){

        final Product lastDeletedProduct;
        final int lastDeletedPosition;
            lastDeletedPosition = listView.getCheckedItemPosition();
            lastDeletedProduct = bag.get(lastDeletedPosition);

        bag.remove(lastDeletedPosition);

    Snackbar snackbar = Snackbar
            .make(parent, "Name saved", Snackbar.LENGTH_LONG)
            .setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //This code will ONLY be executed in case that
                    //the user has hit the UNDO button
                    bag.add(lastDeletedPosition, lastDeletedProduct);
                    Snackbar snackbar = Snackbar.make(parent, "Old name restored!", Snackbar.LENGTH_SHORT);

                    adapter.notifyDataSetChanged();
                    //Show the user we have restored the name - but here
                    //on this snackbar there is NO UNDO - so no SetAction method is called
                    //if you wanted, you could include a REDO on the second action button
                    //for instance.
                    snackbar.show();
                }
            });

        snackbar.show();

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
