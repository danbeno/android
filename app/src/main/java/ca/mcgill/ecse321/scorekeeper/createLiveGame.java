package ca.mcgill.ecse321.scorekeeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.List;

import ca.mcgill.ecse321.team5.controller.Team5Controller;
import ca.mcgill.ecse321.team5.model.Game;
import ca.mcgill.ecse321.team5.model.LeagueManager;
import ca.mcgill.ecse321.team5.model.Team;

public class createLiveGame extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {
    Spinner spinner5, spinner6;
    String team1, team2;
    Team t1, t2;
    Team5Controller tc;
    List<Team> teams;
    String[] teamNames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            tc = new Team5Controller();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            teams = tc.loadTeamData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        teamNames = new String[teams.size()];
        for (int i = 0; i < teams.size(); i++) {
            teamNames[i] = teams.get(i).getName();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_live_game);

        spinner5 = (Spinner) findViewById(R.id.spinner5);
        ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, teamNames);
        spinner5.setAdapter(adapter5);
        spinner5.setOnItemSelectedListener(this);

        spinner6 = (Spinner) findViewById(R.id.spinner6);
        ArrayAdapter<String> adapter6 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, teamNames);
        spinner6.setAdapter(adapter6);
        spinner6.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_live_game, menu);
        return true;
    }

    public void goToLiveMode (View v) throws SQLException {
        Button button = (Button) v;
        team1 = ((Spinner)findViewById(R.id.spinner5)).getSelectedItem().toString();
        team2 = ((Spinner)findViewById(R.id.spinner6)).getSelectedItem().toString();
        for (int i = 0; i < teams.size(); i++) {
            if (team1.equals(teams.get(i).getName())) {
                t1 = teams.get(i);
            }
        }
        for (int i = 0; i < teams.size(); i++) {
            if (team2.equals(teams.get(i).getName())) {
                t2 = teams.get(i);
            }
        }
        if (team1.equals(team2)) {
            Toast.makeText(this, "Please Choose Two Different Teams",
                    Toast.LENGTH_SHORT).show();

        } else {
            Intent intent = new Intent(getApplicationContext(), LiveMode.class);
            intent.putExtra("Team1", team1);
            intent.putExtra("Team2", team2);
            tc.createGame(t1, t2);
            List<Game> gs = tc.loadGamesData();
            int gameId = gs.get(gs.size()).getId();
            intent.putExtra("Gameid", gameId);
            intent.putExtra("T1ID", t1.getId());
            intent.putExtra("T2ID", t2.getId());
            startActivity(intent);
        }
    }

    public void goToMain(View v) {
        Button button = (Button) v;
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
