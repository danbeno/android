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
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.List;

import ca.mcgill.ecse321.team5.controller.Team5Controller;
import ca.mcgill.ecse321.team5.model.Game;
import ca.mcgill.ecse321.team5.model.Team;

public class createBatchGame extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner spinner7, spinner8, spinner9;
    String[] teamNames;
    Team5Controller tc;
    List<Team> teams;
    Team team1, team2;
    List<Game> gs;

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

        spinner7 = (Spinner) findViewById(R.id.spinner7);
        ArrayAdapter<String> adapter7 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, teamNames);
        spinner7.setAdapter(adapter7);
        spinner7.setOnItemSelectedListener(this);

        spinner8 = (Spinner) findViewById(R.id.spinner8);
        ArrayAdapter<String> adapter8 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, teamNames);
        spinner8.setAdapter(adapter8);
        spinner8.setOnItemSelectedListener(this);

        spinner9 = (Spinner) findViewById(R.id.spinner9);
        ArrayAdapter<String> adapter9 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, teamNames);
        spinner9.setAdapter(adapter8);
        spinner9.setOnItemSelectedListener(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_batch_game);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_batch_game, menu);
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

    public void goToBatchMode (View v) {
        Button button = (Button) v;
        startActivity(new Intent(getApplicationContext(), BatchMode.class));
    }

    public void addTeam (View v) throws SQLException {
        Button button = (Button) v;
        String teamName = ((TextView)findViewById(R.id.textView8)).getText().toString();
        if (teamName.equals("")) {
            Toast.makeText(this, "Please enter a valid team name", Toast.LENGTH_SHORT);
            return;
        }
        for (String t : teamNames) {
            if (t.equals(teamName)) {
                Toast.makeText(this, "This team already exists", Toast.LENGTH_SHORT);
                return;
            }
        }
        tc.addTeam(teamName);
        Toast.makeText(this, teamName + " was created", Toast.LENGTH_SHORT);
    }

    public void addPlayer (View v) throws SQLException {
        Button button = (Button) v;
        String playerName = ((TextView)findViewById(R.id.textView9)).getText().toString();
        String position = ((TextView)findViewById(R.id.textView10)).getText().toString();
        String team = ((Spinner)findViewById(R.id.spinner7)).getSelectedItem().toString();
        int i;
        // i will store the index of the team that this player will be added to. We do not allow
        // for 2 teams with the same name.
        for (i = 0; i < teams.size(); i++) {
            if (teamNames[i].equals(team)) {
                break;
            }
        }
        Team t = teams.get(i);
        tc.createPlayer(playerName, position, t);
        Toast.makeText(this, playerName + " was created on team" + t.getName() + "!",
                Toast.LENGTH_SHORT);
    }

    public void createGame (View v) throws SQLException {
        Button button = (Button) v;
        String t1 = ((Spinner)findViewById(R.id.spinner8)).getSelectedItem().toString();
        String t2 = ((Spinner)findViewById(R.id.spinner9)).getSelectedItem().toString();
        // Getting team objects for team 1 and team 2
        for (int i = 0; i < teams.size(); i++) {
            if (t1.equals(teams.get(i).getName())) {
                team1 = teams.get(i);
            }
        }
        for (int i = 0; i < teams.size(); i++) {
            if (t1.equals(teams.get(i).getName())) {
                team2 = teams.get(i);
            }
        }
        if (t1.equals(t2)) {
            Toast.makeText(this, "Please Choose Two Different Teams",
                    Toast.LENGTH_SHORT).show();

        } else {
            // Passing some information to the next activiy.
            Intent intent = new Intent(getApplicationContext(), BatchMode.class);
            intent.putExtra("Team1", t1);
            intent.putExtra("Team2", t2);
            intent.putExtra("GameID", gs.get(gs.size()).getId());
            intent.putExtra("Team1ID", team1.getId());
            intent.putExtra("Team2ID", team2.getId());
            startActivity(intent);
            tc.createGame(team1, team2);
            gs = tc.loadGamesData();
        }
    }

    public void goToMain (View v) {
        Button button = (Button) v;
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
