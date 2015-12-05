package ca.mcgill.ecse321.scorekeeper;

import static ca.mcgill.ecse321.scorekeeper.Constants1.FIRST_COLUMN;
import static ca.mcgill.ecse321.scorekeeper.Constants1.SECOND_COLUMN;
import static ca.mcgill.ecse321.scorekeeper.Constants1.THIRD_COLUMN;
import static ca.mcgill.ecse321.scorekeeper.Constants1.FOURTH_COLUMN;

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
public class leagueData extends Activity
{
    private ArrayList<HashMap> list;
    List<Team> teams;
    LeagueManager lm;
    Team5Controller tc;
    Bundle extras;
    int maxTeams;

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
            tc.sortTeamsByGoals();
        } else if (id==2) {
            tc.sortTeamsByPoints();;
        } else {
            tc.sortTeamsByInfractions();
        }
        teams = lm.getTeams();
        // If 8 is greater than the amount of teams then we list the number of teams
        // else if 8 is less than the amount of teams we list 10 teams
        maxTeams = (8>teams.size()) ? teams.size() : 10;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_data);

        ListView lview = (ListView) findViewById(R.id.listview2);
        populateList();
        listviewAdapter2 adapter = new listviewAdapter2(this, list);
        lview.setAdapter(adapter);
    }

    private void populateList() {

        list = new ArrayList<HashMap>();

        HashMap temp = new HashMap();
        temp.put(FIRST_COLUMN, "Name");
        temp.put(SECOND_COLUMN, "Goals");
        temp.put(THIRD_COLUMN, "Points");
        temp.put(FOURTH_COLUMN, "Infractions");
        list.add(temp);

        for (int i = 0; i < maxTeams; i++) {
            temp = new HashMap();
            temp.put(FIRST_COLUMN, teams.get(i).getName());
            temp.put(SECOND_COLUMN, "" + getGoals(teams.get(i)) + "");
            temp.put(THIRD_COLUMN, teams.get(i).getNumPoints());
            temp.put(FOURTH_COLUMN, "" + getInfractions(teams.get(i)));
            list.add(temp);
        }
    }

    public int getGoals(Team t) {
        int goals = 0;
        for (Player p : t.getPlayers()) {
            goals += p.getNumGoals();
        }
        return goals;
    }

    public int getInfractions(Team t) {
        int infs = 0;
        for (Player p : t.getPlayers()) {
            infs += p.numberOfInfractions();
        }
        return infs;
    }
}