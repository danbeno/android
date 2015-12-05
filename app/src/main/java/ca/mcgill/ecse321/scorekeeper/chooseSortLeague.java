package ca.mcgill.ecse321.scorekeeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.sql.SQLException;
import java.util.List;

import ca.mcgill.ecse321.team5.controller.Team5Controller;
import ca.mcgill.ecse321.team5.model.LeagueManager;
import ca.mcgill.ecse321.team5.model.Team;

public class chooseSortLeague extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sort_league);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_sort_league, menu);
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

    // id for how the league data will be sorted. 1 will correspond to sorting by goals, 2 will be
    // points, else sort by infractions
    public void goToLeagueDataGoals (View v) {
        Button button = (Button) v;
        Intent intent = new Intent(getApplicationContext(), leagueData.class);
        intent.putExtra("id", 1);
        startActivity(intent);
    }

    public void gotToLeagueDataPoints (View v) {
        Button button = (Button) v;
        Intent intent = new Intent(getApplicationContext(), leagueData.class);
        intent.putExtra("id", 2);
        startActivity(intent);
    }

    public void goToLeagueDataInfractions (View v) {
        Button button = (Button) v;
        Intent intent = new Intent(getApplicationContext(), leagueData.class);
        intent.putExtra("id", 3);
        startActivity(intent);
    }
}
