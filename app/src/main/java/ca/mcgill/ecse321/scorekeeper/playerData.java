package ca.mcgill.ecse321.scorekeeper;

import static ca.mcgill.ecse321.scorekeeper.Constants1.FIRST_COLUMN;
import static ca.mcgill.ecse321.scorekeeper.Constants1.SECOND_COLUMN;
import static ca.mcgill.ecse321.scorekeeper.Constants1.THIRD_COLUMN;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import ca.mcgill.ecse321.team5.controller.Team5Controller;
import ca.mcgill.ecse321.team5.model.LeagueManager;
import ca.mcgill.ecse321.team5.model.Player;
import ca.mcgill.ecse321.team5.model.Team;

/**
 *
 * @author Paresh N. Mayani
 */
public class playerData extends Activity
{
    private ArrayList<HashMap> list;
    List<Player> players;

    LeagueManager lm;
    Team5Controller tc;
    Bundle extras;
    int maxPlayers;

    public void onCreate(Bundle savedInstanceState)
    {
        extras = getIntent().getExtras();
        lm = LeagueManager.getInstance();
        try {
            tc = new Team5Controller();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int id = extras.getInt("id");
        if (id==1) {
            tc.sortPlayersByGoals();
        } else {
            tc.sortPlayersByInfractions();
        }
        players = lm.getPlayers();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_data);
        maxPlayers = (8>players.size()) ? players.size() : 8;
        ListView lview = (ListView) findViewById(R.id.listview);
        populateList();
        listviewAdapter1 adapter = new listviewAdapter1(this, list);
        lview.setAdapter(adapter);
    }

    private void populateList() {

        list = new ArrayList<HashMap>();
        HashMap temp = new HashMap();
        temp.put(FIRST_COLUMN, "Name");
        temp.put(SECOND_COLUMN, "Goals");
        temp.put(THIRD_COLUMN, "Infractions");
        list.add(temp);
        for (int i = 0; i < maxPlayers; i++) {
            temp = new HashMap();
            temp.put(FIRST_COLUMN, players.get(i).getName());
            temp.put(SECOND_COLUMN, players.get(i).getNumGoals());
            temp.put(THIRD_COLUMN, players.get(i).numberOfInfractions());
            list.add(temp);
        }
    }
}