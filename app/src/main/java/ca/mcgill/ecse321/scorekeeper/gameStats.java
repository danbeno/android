package ca.mcgill.ecse321.scorekeeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.SQLException;
import java.util.List;

import ca.mcgill.ecse321.team5.controller.Team5Controller;
import ca.mcgill.ecse321.team5.model.Game;
import ca.mcgill.ecse321.team5.model.LeagueManager;
import ca.mcgill.ecse321.team5.model.Player;
import ca.mcgill.ecse321.team5.model.Team;

public class gameStats extends AppCompatActivity {

    List<Game> games;
    Game g;
    Bundle extras;
    Team5Controller tc;
    LeagueManager lm;
    Team t1, t2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            tc = new Team5Controller();
            games = tc.loadGamesData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        extras = getIntent().getExtras();

        g = games.get(extras.getInt("GameID"));

        t1 = g.getTeam(1);
        t2 = g.getTeam(2);
        //Placing data in their respective text view.
        TextView textView16 = (TextView) findViewById(R.id.textView16);
        textView16.setText(t1.getName());

        TextView textView17 = (TextView) findViewById(R.id.textView17);
        textView17.setText(t2.getName());

        TextView textView21 = (TextView) findViewById(R.id.textView21);
        int t1shots = 0, t2shots = 0, t1inf = 0, t2inf = 0;
        for (Player p: t1.getPlayers()) {
            t1shots += p.numberOfShots();
            t1inf += p.numberOfInfractions();
        }
        textView21.setText("" + t1shots + "");

        TextView textView24 = (TextView) findViewById(R.id.textView24);
        for (Player p : t2.getPlayers()) {
            t2shots += p.numberOfShots();
            t2inf += p.numberOfInfractions();
        }
        textView24.setText("" + t2shots + "");

        TextView textView22 = (TextView) findViewById(R.id.textView22);
        textView22.setText("" + g.getTeamOneScore() + "");

        TextView textView25 = (TextView) findViewById(R.id.textView25);
        textView25.setText("" + g.getTeamTwoScore());

        TextView textView23 = (TextView) findViewById(R.id.textView23);
        textView23.setText("" + t1inf + "");

        TextView textView26 = (TextView) findViewById(R.id.textView26);
        textView26.setText("" + t2inf + "");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_stats);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_stats, menu);
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

    public void goToGame(View v) {
        Button button = (Button) v;
        startActivity(new Intent(getApplicationContext(), BatchMode.class));
    }
}
