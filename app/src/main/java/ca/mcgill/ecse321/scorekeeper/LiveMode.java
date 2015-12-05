package ca.mcgill.ecse321.scorekeeper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.SQLException;
import java.util.List;

import ca.mcgill.ecse321.team5.controller.Team5Controller;
import ca.mcgill.ecse321.team5.model.Game;
import ca.mcgill.ecse321.team5.model.Player;
import ca.mcgill.ecse321.team5.model.Team;

public class LiveMode extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner1, spinner2, spinner3, spinner4;
    String[] infractions = {"Yellow", "Red", "Penalty"};
    boolean isGoal;
    TextView score;
    int score1 = 0, score2 = 0;
    String p1, p2, infraction1 = infractions[0], infraction2 = infractions[0];
    Bundle extras;
    Team5Controller tc;
    List<Team> teams;
    List<Player> t1;
    List<Player> t2;
    Game g;
    List <Game> games;
    Team team1;
    Team team2;
    String[] team1Names;
    String[] team2Names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        extras = getIntent().getExtras();

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

        try {
            tc.loadPlayerData();
            tc.loadShotData();
            tc.loadInfData();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            games = tc.loadGamesData();
        } catch (SQLException e){
            e.printStackTrace();
        }

        // Getting the teams that play in the game and their players. The loading of the database
        // makes sure everything is ordered in terms of id.
        team1 = teams.get(extras.getInt("T1ID"));
        team2 = teams.get(extras.getInt("T2ID"));
        t1 = team1.getPlayers();
        t2 = team2.getPlayers();
        g = games.get(extras.getInt("Gameid"));

        team1Names = new String[t1.size()];
        team2Names = new String[t2.size()];
        // Storing player names for each team
        for(int i = 0; i < t1.size(); i++) {
            team1Names[i] = t1.get(i).getName();
        }

        for (int i = 0; i < t2.size(); i++)
        {
            team2Names[i] = t2.get(i).getName();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_mode);
        spinner1 = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, team1Names);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(this);

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, team2Names);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);

        spinner3 = (Spinner) findViewById(R.id.spinner3);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, infractions);
        spinner3.setAdapter(adapter3);
        spinner3.setOnItemSelectedListener(this);

        spinner4 = (Spinner) findViewById(R.id.spinner4);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, infractions);
        spinner4.setAdapter(adapter4);
        spinner4.setOnItemSelectedListener(this);

        TextView textView1 = (TextView)findViewById(R.id.textView);
        textView1.setText(extras.getString("Team1").toString());

        TextView textView2 = (TextView)findViewById(R.id.textView2);
        textView2.setText(extras.getString("Team2").toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_live_mode, menu);
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

    public void goToMain(View v) {
        Button button = (Button) v;
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }
    // Pretty much the same as batch mode for all the methods
    public void addGoal1 (View v) throws SQLException {
        p1 = ((Spinner)findViewById(R.id.spinner)).getSelectedItem().toString();
        CheckBox checkbox = (CheckBox) findViewById(R.id.checkBox);
        isGoal = checkbox.isChecked();

        Button button = (Button) v;
        if (isGoal) {
            score = (TextView) findViewById(R.id.textView3);
            score1 += 1;
            String scoreString = "" + score1 + "";
            score.setText(scoreString);
            Toast.makeText(this, p1 + " scored a goal!", Toast.LENGTH_SHORT).show();
       } else {
            Toast.makeText(this, p1 + " was stopped!", Toast.LENGTH_SHORT).show();
        }
        Player p = t1.get(((Spinner)findViewById(R.id.spinner)).getSelectedItemPosition());
        tc.addShot(isGoal, p, g, 1, team1, team2);
    }

    public void addInfraction1 (View v) throws SQLException {
        Button button = (Button) v;
        p1 = ((Spinner)findViewById(R.id.spinner)).getSelectedItem().toString();
        Player p = t1.get(((Spinner)findViewById(R.id.spinner)).getSelectedItemPosition());
        infraction1 = ((Spinner)findViewById(R.id.spinner3)).getSelectedItem().toString();
        Toast.makeText(this, p1 + " got a " + infraction1, Toast.LENGTH_SHORT).show();
        tc.addInfraction(infraction1, p);
    }

    public void addInfraction2 (View v) throws SQLException {
        Button button = (Button) v;
        p2 = ((Spinner)findViewById(R.id.spinner2)).getSelectedItem().toString();
        Player p = t2.get(((Spinner)findViewById(R.id.spinner2)).getSelectedItemPosition());
        infraction2 = ((Spinner)findViewById(R.id.spinner4)).getSelectedItem().toString();
        Toast.makeText(this, p2 + " got a " + infraction2, Toast.LENGTH_SHORT).show();
        tc.addInfraction(infraction2, p);
    }

    public void addGoal2 (View v) throws SQLException {
        p2 = ((Spinner)findViewById(R.id.spinner2)).getSelectedItem().toString();
        CheckBox checkbox = (CheckBox) findViewById(R.id.checkBox2);
        isGoal = checkbox.isChecked();

        Button button = (Button) v;
        if (isGoal) {
            score = (TextView) findViewById(R.id.textView4);
            score2 += 1;
            String scoreString = "" + score2 + "";
            score.setText(scoreString);
            Toast.makeText(this, p2 + " scored a goal!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, p2 + " was stopped!", Toast.LENGTH_SHORT).show();
        }
        Player p = t2.get(((Spinner)findViewById(R.id.spinner2)).getSelectedItemPosition());
        tc.addShot(isGoal, p, g, 2, team2, team1);
    }

    public void endGame (View v) throws SQLException {
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

//    public void chooseWin () {
//        if (score1 > score2) {
//            new AlertDialog.Builder(this).setTitle("Game Over!").setMessage("Team 1 " +
//                    "won!").show();
//        } else if (score2 > score1) {
//            new AlertDialog.Builder(this).setTitle("Game Over!").setMessage("Team 2 " +
//                    "won!").show();
//        } else {
//            new AlertDialog.Builder(this).setTitle("Game Over!").setMessage("It was " +
//                    "a tie!").show();
//        }
//    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
