package com.dadino.fhpx;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;


public class PhotoListActivity extends Activity implements FHPX.FHCallbacks {

    private PhotoAdapter mAdapter;
    private FHPX mApiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);

        GridView mListView = (GridView) findViewById(R.id.listView);
        mAdapter = new PhotoAdapter(this, null);
        mListView.setAdapter(mAdapter);

       refreshPhotos();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.photo_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_refresh:
            refreshPhotos();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshPhotos(){
        if(mApiManager == null) {
            mApiManager = new FHPX(this);
        }
        mApiManager.downloadPhotos();
    }

    @Override
    public void onFHPhotoDownloaded(List<Photo> photos) {
        mAdapter.setPhotos(photos);
    }

    @Override
    public void onErrorDownloading() {
        Toast.makeText(this, "Error downloading photo list", Toast.LENGTH_SHORT).show();
    }
}
