package ondotsystems.com.imagefinder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ondotsystems.com.imagefinder.adapter.ImageAdapter;
import ondotsystems.com.imagefinder.model.PixabayImage;
import ondotsystems.com.imagefinder.util.Config;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = SearchActivity.class.getSimpleName();
    RecyclerView recyclerView;
    ImageAdapter adapter;

    EditText searchText;
    Button loadImages;

    ArrayList<PixabayImage> imageList;
    RequestQueue queue;

    String searchUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchText = findViewById(R.id.searchView);
        loadImages = findViewById(R.id.btn_search);
        recyclerView = findViewById(R.id.images_rv);
        // set a GridLayoutManager with default vertical orientation and 2 number of columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        imageList = new ArrayList<>();

        queue = Volley.newRequestQueue(this);

        loadImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchUrl = searchText.getText().toString();
                Log.i(TAG, "search string entered is " + searchUrl);
                loadPixabayImages(searchUrl);
                searchText.setText("");
            }
        });
    }

    public void loadPixabayImages(String searchUrl) {

        String url = Config.BASE_URL + Config.API_KEY + Config.QUERY + searchUrl + Config.TYPE;

        Log.i(TAG, "search url is " + url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("hits");
                    int len = jsonArray.length();

                    for (int i = 0; i < len; i++) {

                        JSONObject hit = jsonArray.getJSONObject(i);

                        String imageUrl = hit.getString("previewURL");
                        int downloadCount = hit.getInt("downloads");

                        imageList.add(new PixabayImage(imageUrl, downloadCount));
                    }

                    adapter = new ImageAdapter(SearchActivity.this, imageList);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(jsonObjectRequest);

    }
}
