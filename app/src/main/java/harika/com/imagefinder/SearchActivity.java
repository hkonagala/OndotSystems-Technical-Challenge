package harika.com.imagefinder;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import harika.com.imagefinder.adapter.ImageAdapter;
import harika.com.imagefinder.model.PixabayImage;
import harika.com.imagefinder.util.Config;
import harika.com.imagefinder.util.EmptyRecyclerView;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = SearchActivity.class.getSimpleName();

    EmptyRecyclerView recyclerView;
    ImageAdapter adapter;

    private ArrayList<PixabayImage> imageArrayList;

    EditText searchText;
    Button loadImages;

//    JSONViewModel model;

    RelativeLayout animationView;
    TextView emptyViewMessage;

    GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchText = findViewById(R.id.searchView);
        loadImages = findViewById(R.id.btn_search);
        recyclerView = findViewById(R.id.images_rv);
        animationView = findViewById(R.id.emptyContainer);
        emptyViewMessage = findViewById(R.id.empty_message);

        //set empty view for recyclerview
        recyclerView.setEmptyView(animationView);
        // set a GridLayoutManager with default vertical orientation and 2 number of columns
        gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        imageArrayList = new ArrayList<>();
        adapter = new ImageAdapter(SearchActivity.this, imageArrayList);
        recyclerView.setAdapter(adapter);
        getResults();

    }

    private void getResults() {

        loadImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchUrl = searchText.getText().toString();
                //format search string
                searchUrl = searchUrl.replace(" ", "+");
                Log.i(TAG, "search string entered is " + searchUrl);

                if(searchUrl != null){
                    //todo uncomment to implement ViewModel architecture
                    /*model = ViewModelProviders.of(SearchActivity.this).get(JSONViewModel.class);
                    model.getPixabayList(SearchActivity.this, searchUrl).
                            observe(SearchActivity.this, new Observer<ArrayList<PixabayImage>>() {
                        @Override
                        public void onChanged(ArrayList<PixabayImage> pixabayImages) {
                            adapter = new ImageAdapter(SearchActivity.this, pixabayImages);
                            recyclerView.setAdapter(adapter);
                        }
                    });*/

                    //refresh results
                    imageArrayList.clear();
                    parseJSONResults(SearchActivity.this, searchUrl);
                } else {
                    Toast.makeText(SearchActivity.this,
                            getString(R.string.empty_str),
                            Toast.LENGTH_LONG).show();
                }

            }
        });
    }


    public void parseJSONResults(Context context, String searchUrl) {

        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage(context.getString(R.string.loading));
        pDialog.show();

        String url = Config.BASE_URL + Config.API_KEY + Config.QUERY + searchUrl + Config.TYPE;

        RequestQueue queue = Volley.newRequestQueue(context);

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


                                imageArrayList.add(new PixabayImage(imageUrl, downloadCount));

                            }
                            adapter.notifyDataSetChanged();
                            pDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                error.printStackTrace();

                Log.d(TAG, "Error: " + error.getMessage());

                    if( error instanceof NetworkError) {
                        emptyViewMessage.setText(getString(R.string.network_error));
                    } else if( error instanceof ServerError) {
                        emptyViewMessage.setText(getString(R.string.server_error));
                    } else if( error instanceof AuthFailureError) {
                        emptyViewMessage.setText(getString(R.string.auth_error));
                    } else if( error instanceof ParseError) {
                        emptyViewMessage.setText(getString(R.string.parse_error));
                    } else if( error instanceof NoConnectionError) {
                        emptyViewMessage.setText(getString(R.string.error404));
                    } else if( error instanceof TimeoutError) {
                        emptyViewMessage.setText(getString(R.string.error429));
                    } else {
                        emptyViewMessage.setText(getString(R.string.emptyView));
                    }
            }
        });

        queue.add(jsonObjectRequest);

    }
}
