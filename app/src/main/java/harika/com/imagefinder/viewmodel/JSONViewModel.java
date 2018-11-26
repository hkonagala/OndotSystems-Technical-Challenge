package harika.com.imagefinder.viewmodel;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import harika.com.imagefinder.R;
import harika.com.imagefinder.model.PixabayImage;
import harika.com.imagefinder.util.Config;

public class JSONViewModel extends ViewModel {
    //asynchronously fetch data
    public MutableLiveData<ArrayList<PixabayImage>> pixabayList;

    public JSONViewModel() {
    }

    public JSONViewModel(MutableLiveData<ArrayList<PixabayImage>> pixabayList) {
        this.pixabayList = pixabayList;
    }

    //get the data
    public LiveData<ArrayList<PixabayImage>> getPixabayList(Context context, String searchUrl) {
        //if the list is null
        if (pixabayList == null) {
            pixabayList = new MutableLiveData<>();
            //load it asynchronously from server
            getJSONResponse(context, searchUrl);
        }
        //return the list
        return pixabayList;
    }

    public void getJSONResponse(Context context, String searchUrl) {

        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage(context.getString(R.string.loading));
        pDialog.show();

        String url = Config.BASE_URL + Config.API_KEY + Config.QUERY + searchUrl + Config.TYPE;

        RequestQueue queue = Volley.newRequestQueue(context);

        Log.i("JSONViewModel", "search url is " + url);
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

                                ArrayList<PixabayImage> list = new ArrayList<>();
                                list.add(new PixabayImage(imageUrl, downloadCount));

                                pixabayList.setValue(list);
                            }
                            //todo how to update adapter dymanically?
//                            adapter.notifyDataSetChanged();
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

                Log.d("JSONViewModel", "Error: " + error.getMessage());

//                    if( error instanceof NetworkError) {
//                        emptyViewMessage.setText(getString(R.string.network_error));
//                    } else if( error instanceof ServerError) {
//                        emptyViewMessage.setText(getString(R.string.server_error));
//                    } else if( error instanceof AuthFailureError) {
//                        emptyViewMessage.setText(getString(R.string.auth_error));
//                    } else if( error instanceof ParseError) {
//                        emptyViewMessage.setText(getString(R.string.parse_error));
//                    } else if( error instanceof NoConnectionError) {
//                        emptyViewMessage.setText(getString(R.string.error404));
//                    } else if( error instanceof TimeoutError) {
//                        emptyViewMessage.setText(getString(R.string.error429));
//                    } else {
//                        emptyViewMessage.setText(getString(R.string.emptyView));
//                    }
            }
        });

        queue.add(jsonObjectRequest);

    }
}
