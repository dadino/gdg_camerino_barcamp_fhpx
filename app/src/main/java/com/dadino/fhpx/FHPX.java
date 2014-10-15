package com.dadino.fhpx;

import android.util.Log;

import java.util.List;

import retrofit.Callback;
import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;


public class FHPX {
    public interface FHCallbacks {
        public void onFHPhotoDownloaded(List<Photo> photos);

        public void onErrorDownloading();
    }

    private final FHCallbacks mCallbacks;
    private final FiveHundredPxService service;

    public interface FiveHundredPxService {
        @GET("/v1/photos?feature=popular&sort=rating&image_size=5&rpp=40&q={searchterm}&type=photos")
        void getPopularPhotos(@Path("searchterm") String searchTerm, Callback<PhotosResponse> cb);

        static class PhotosResponse {
            public List<Photo> photos;
        }


    }

    public static final String CONSUMER_KEY = "mZaLQIbE5S42fSxSlz9VFVfinv4v6NGZfuBtWKyf";
    private static final String TAG = "500PX";

    public FHPX(FHCallbacks callbacks) {
        mCallbacks = callbacks;

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.500px.com")
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addQueryParam("consumer_key", CONSUMER_KEY);
                    }
                })
                         .build();

        service = restAdapter.create(FiveHundredPxService.class);

    }

    public void downloadPhotos() {
        service.getPopularPhotos("nature", callback);
    }

    public static List<Photo> getPhotosFromResponse(FiveHundredPxService.PhotosResponse response) {
        if (response == null || response.photos == null) {
            return null;
        } else {
            if (response.photos.size() == 0) {
                Log.w(TAG, "No photos returned from 500px API.");
                return null;
            } else {
                return response.photos;
            }
        }

    }

    public Callback<FiveHundredPxService.PhotosResponse> callback = new Callback<FiveHundredPxService.PhotosResponse>() {
        @Override
        public void success(FiveHundredPxService.PhotosResponse photosResponse, Response response) {
            Log.d(TAG, "SUCCESS");
            List<Photo> mPhotos = FHPX.getPhotosFromResponse(photosResponse);

            mCallbacks.onFHPhotoDownloaded(mPhotos);
            Log.d(TAG, "PHOTOS SAVED");
        }

        @Override
        public void failure(RetrofitError error) {
            Log.d(TAG, "FAILURE");
            Log.d(TAG, "Message: " + error.getMessage());
            if (error.getResponse() != null) {
                Log.d(TAG, "Status code: " + error.getResponse().getStatus());
                Log.d(TAG, "Reason: " + error.getResponse().getReason());
            }
            mCallbacks.onErrorDownloading();
        }
    };
}
