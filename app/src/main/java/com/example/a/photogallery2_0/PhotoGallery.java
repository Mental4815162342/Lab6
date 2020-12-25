package com.example.a.photogallery2_0;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.a.photogallery2_0.api.FlickrAPI;
import com.example.a.photogallery2_0.api.ServiceAPI;
import com.example.a.photogallery2_0.db.PhotosDB;
import com.example.a.photogallery2_0.db.PhotosDao;
import com.example.a.photogallery2_0.model.Photo;
import com.example.a.photogallery2_0.model.PhotosResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoGallery extends AppCompatActivity {
    private final List<Photo> photos = new ArrayList<>();
    private final PhotoAdapter adapter = new PhotoAdapter(photos);
    private PhotosDao dao;
    private final FlickrAPI flickrAPI = ServiceAPI.getRetrofit().create(FlickrAPI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_activity);
        dao = PhotosDB.getDatabase(getApplicationContext()).photoDao();

        final RecyclerView recycler = findViewById(R.id.RecyclerView);
        recycler.setLayoutManager(new GridLayoutManager(this, 3));

        flickrAPI.getRecent().enqueue(new Callback<PhotosResponse>() {
            @Override
            public void onResponse(Call<PhotosResponse> call, Response<PhotosResponse> response) {
                photos.addAll(response.body().getPhotos().getPhoto());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<PhotosResponse> call, Throwable t) {
                AlertDialog alertDialog = new AlertDialog.Builder(PhotoGallery.this).create(); //Read Update
                alertDialog.setTitle("Info");
                alertDialog.setMessage("Something went wrong...");
                alertDialog.show();
            }
        });


        adapter.setOnLongClickPhotoListener(photo -> {
            dao.insertPhoto(photo);

            AlertDialog alertDialog = new AlertDialog.Builder(PhotoGallery.this).create(); //Read Update
            alertDialog.setTitle("Info");
            alertDialog.setMessage("Pictures saved!");
            alertDialog.show();
        });
        recycler.setAdapter(adapter);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            flickrAPI.getSearchPhotos(query).enqueue(new Callback<PhotosResponse>() {
                @Override
                public void onResponse(Call<PhotosResponse> call, Response<PhotosResponse> response) {
                    photos.addAll(response.body().getPhotos().getPhoto());
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<PhotosResponse> call, Throwable t) {
                    AlertDialog alertDialog = new AlertDialog.Builder(PhotoGallery.this).create(); //Read Update
                    alertDialog.setTitle("Info");
                    alertDialog.setMessage("Something went wrong...");
                    alertDialog.show();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_bar).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by defaul

        return true;
    }

    public void onLoadGalleryClick(MenuItem item) {
        photos.clear();
        photos.addAll(dao.LoadAll());
        adapter.notifyDataSetChanged();
    }

    public void onLoadRecentClick(MenuItem item) {
        flickrAPI.getRecent().enqueue(new Callback<PhotosResponse>() {
            @Override
            public void onResponse(Call<PhotosResponse> call, Response<PhotosResponse> response) {
                photos.clear();
                photos.addAll(response.body().getPhotos().getPhoto());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<PhotosResponse> call, Throwable t) {
                AlertDialog alertDialog = new AlertDialog.Builder(PhotoGallery.this).create(); //Read Update
                alertDialog.setTitle("Info");
                alertDialog.setMessage("Something went wrong...");
                alertDialog.show();
            }
        });
    }
}