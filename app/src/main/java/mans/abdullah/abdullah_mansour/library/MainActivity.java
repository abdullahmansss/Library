package mans.abdullah.abdullah_mansour.library;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText book_title;
    Button search;
    ListView listView;
    Adapter adapter;
    String GOOGLE_BOOKS_API;
    ImageView agenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        book_title = (EditText) findViewById(R.id.edit_txt_book);
        search = (Button) findViewById(R.id.search_btn);
        listView = (ListView) findViewById(R.id.listview);
        agenda = (ImageView) findViewById(R.id.agenda);

        adapter = new Adapter(getApplicationContext(),0 , new ArrayList<DataClass>());

        listView.setAdapter(adapter);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agenda.setVisibility(View.INVISIBLE);

                String s = book_title.getText().toString();

                GOOGLE_BOOKS_API =
                        "https://www.googleapis.com/books/v1/volumes?q=" + s;

                AsyncTask newtask = new AsyncTask();
                newtask.execute(GOOGLE_BOOKS_API);

                InputMethodManager imm = (InputMethodManager) getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });
    }

    public class AsyncTask extends android.os.AsyncTask<String, Void, ArrayList<DataClass>>
    {
        @Override
        protected ArrayList<DataClass> doInBackground(String... strings)
        {
            if (strings.length < 1 || strings[0] == null) {
                return null;
            }

            ArrayList <DataClass> books = Utils.fetchBooksData(strings[0]);

            return books;
        }

        @Override
        protected void onPostExecute(ArrayList<DataClass> dataClasses)
        {
            adapter.clear();

            if (dataClasses != null && !dataClasses.isEmpty()) {
                adapter.addAll(dataClasses);
            }
        }
    }
}
