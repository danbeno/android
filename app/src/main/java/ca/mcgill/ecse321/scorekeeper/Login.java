package ca.mcgill.ecse321.scorekeeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_input_data, menu);
        return true;
    }

    private EditText username;
    private EditText password;

    public void goToInputData (View v) {
        Button button = (Button) v;
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        System.out.println(username);
        System.out.println(password);
        if (username.getText().toString().equals("root") && password.getText().toString().equals("password")) {
            startActivity(new Intent(getApplicationContext(), InputData.class));
        } else {
            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.TOP| Gravity.LEFT, 0, 0);
            toast.makeText(Login.this, "Incorrect Username or Password", toast.LENGTH_SHORT).show();
        }
    }

    public void goToMain (View v) {
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
}
