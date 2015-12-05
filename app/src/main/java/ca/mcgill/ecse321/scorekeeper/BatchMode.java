package ca.mcgill.ecse321.scorekeeper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.List;

import ca.mcgill.ecse321.team5.controller.Team5Controller;
import ca.mcgill.ecse321.team5.model.Game;
import ca.mcgill.ecse321.team5.model.Player;
import ca.mcgill.ecse321.team5.model.Team;

public class BatchMode extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    boolean isGoal;
    int score1 = 0, score2 = 0;
    Bundle extras;
    Team5Controller tc;
    List<Team> teams;
    List<Game> gs;
    List<Player> t1, t2;
    String[] infractions = {"Yellow", "Red", "Penalty"};
    Team team1, team2;
    Game g;
    String[] team1Names, team2Names;
    Spinner spinner10, spinner11, spinner12, spinner13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        extras = getIntent().getExtras();
        try {
            tc = new Team5Controller();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Loading all of the data into the league manager
        try {
            teams = tc.loadTeamData();
            tc.loadGamesData();
            tc.loadInfData();
            tc.loadShotData();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            tc.loadPlayerData();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            gs = tc.loadGamesData();
        } catch (SQLException e){
            e.printStackTrace();
        }
        // Getting the names of both team1 and team2 and gameid
        team1 = teams.get(extras.getInt("Team1ID"));
        team2 = teams.get(extras.getInt("Team2ID"));
        t1 = team1.getPlayers();
        t2 = team2.getPlayers();
        g = gs.get(extras.getInt("GameID"));

        team1Names = new String[t1.size()];
        team2Names = new String[t2.size()];
        // Storing the names of the players of each team
        for (int i = 0; i < t1.size(); i++) {
            team1Names[i] = t1.get(i).getName();
        }

        for (int i = 0; i < t2.size(); i++) {
            team2Names[i] = t2.get(i).getName();
        }

        spinner10 = (Spinner) findViewById(R.id.spinner10);
        ArrayAdapter<String> adapter10 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, team1Names);
        spinner10.setAdapter(adapter10);
        spinner10.setOnItemSelectedListener(this);

        spinner11 = (Spinner) findViewById(R.id.spinner11);
        ArrayAdapter<String> adapter11 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, team2Names);
        spinner11.setAdapter(adapter11);
        spinner11.setOnItemSelectedListener(this);

        spinner12 = (Spinner) findViewById(R.id.spinner12);
        ArrayAdapter<String> adapter12 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, infractions);
        spinner12.setAdapter(adapter12);
        spinner12.setOnItemSelectedListener(this);

        spinner13 = (Spinner) findViewById(R.id.spinner13);
        ArrayAdapter<String> adapter13 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, infractions);
        spinner13.setAdapter(adapter13);
        spinner13.setOnItemSelectedListener(this);
        // Placing the name of the team in the respective text box
        TextView textView12 = (TextView)findViewById(R.id.textView12);
        textView12.setText(extras.getString("Team1").toString());

        TextView textView13 = (TextView)findViewById(R.id.textView13);
        textView13.setText(extras.getString("Team2").toString());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_mode);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_batch_mode, menu);
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

    public void goToStats (View v) {
        Button button = (Button) v;
        Intent intent =new Intent(getApplicationContext(), gameStats.class);
        intent.putExtra("GameID", g.getId());
    }

    public void addShot1 (View v) throws SQLException {
        // Getting the player selected in the spinner
        String p1 = ((Spinner)findViewById(R.id.spinner10)).getSelectedItem().toString();
        CheckBox checkbox = (CheckBox) findViewById(R.id.checkBox3);
        isGoal = checkbox.isChecked();

        Button button = (Button) v;
        if (isGoal) {
            TextView score = (TextView) findViewById(R.id.textView14);
            score1 += 1;
            String scoreString = "" + score1 + "";
            score.setText(scoreString);
            Toast.makeText(this, p1 + " scored a goal!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, p1 + " was stopped!", Toast.LENGTH_SHORT).show();
        }
        // Updating database. The player will be in the t1 list at the same position as it is
        // in the dropdown spinner.
        Player p = t1.get(((Spinner)findViewById(R.id.spinner10)).getSelectedItemPosition());
        tc.addShot(isGoal, p, g, 1, team1, team2);
    }

    public void addInfraction1Batch (View v) throws SQLException {
        // Getting the player selected in the spinner
        String p1 = ((Spinner)findViewById(R.id.spinner10)).getSelectedItem().toString();
        Button button = (Button) v;
        Player p = t1.get(((Spinner)findViewById(R.id.spinner10)).getSelectedItemPosition());
        String infraction1 = ((Spinner)findViewById(R.id.spinner12)).getSelectedItem().toString();
        Toast.makeText(this, p1 + " got a " + infraction1, Toast.LENGTH_SHORT).show();
        tc.addInfraction(infraction1, p);
    }

    public void addShot2 (View v) throws SQLException {
        String p2 = ((Spinner)findViewById(R.id.spinner11)).getSelectedItem().toString();
        CheckBox checkbox = (CheckBox) findViewById(R.id.checkBox4);
        isGoal = checkbox.isChecked();

        Button button = (Button) v;
        if (isGoal) {
            TextView score = (TextView) findViewById(R.id.textView15);
            score2 += 1;
            String scoreString = "" + score2 + "";
            score.setText(scoreString);
            Toast.makeText(this, p2 + " scored a goal!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, p2 + " was stopped!", Toast.LENGTH_SHORT).show();
        }
        Player p = t2.get(((Spinner) findViewById(R.id.spinner11)).getSelectedItemPosition());
        tc.addShot(isGoal, p, g, 2, team2, team1);
    }

    public void addInfraction2Batch (View v) throws SQLException {
        Button button = (Button) v;
        String p2 = ((Spinner)findViewById(R.id.spinner11)).getSelectedItem().toString();
        Player p = t2.get(((Spinner)findViewById(R.id.spinner11)).getSelectedItemPosition());
        String infraction2 = ((Spinner)findViewById(R.id.spinner13)).getSelectedItem().toString();
        Toast.makeText(this, p2 + " got a " + infraction2, Toast.LENGTH_SHORT).show();
        tc.addInfraction(infraction2, p);
    }

    public void endGameBatch (View v) throws SQLException {
        Button button = (Button) v;
        String winner;
        if (score1 > score2) {
            winner = " with " +  extras.getString("Team1").toString() + " winning";
        } else if (score2 > score1) {
            winner = " with " + extras.get("Team2").toString() + " winning";
        } else {
            winner = " as a tie";
        }
        new AlertDialog.Builder(this)
                .setTitle("End Game")
                .setMessage("Are you sure you want to end this game" + winner + "?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(), InputData.class));
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        tc.endGame(g);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
