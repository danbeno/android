package ca.mcgill.ecse321.scorekeeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class chooseSortPlayer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sort_player);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_sort_player, menu);
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

    // Choosing how the player data wants to be sorted. 1 will correspond to by goals, else
    // it will be by infractions.
    public void goToPlayerDataGoals (View v) {
        Button button = (Button) v;
        Intent intent = new Intent(getApplicationContext(), playerData.class);
        intent.putExtra("id", 1);
        startActivity(intent);
    }

    public void goToPlayerDataInfractions (View v) {
        Button button = (Button) v;
        Intent intent = new Intent(getApplicationContext(), playerData.class);
        intent.putExtra("id", 2);
        startActivity(intent);
    }
}
