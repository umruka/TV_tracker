package com.example.tvtracker.Repository;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tvtracker.Models.TvShowFull;
import com.example.tvtracker.Helpers.TvShowHelper;
import com.example.tvtracker.Repository.Api.ApiBuilder;
import com.example.tvtracker.Repository.Api.ApiService;
import com.example.tvtracker.Repository.Api.ApiModels.JsonTvShowSearchRoot;
import com.example.tvtracker.Repository.Api.ApiModels.TvShowBasicInfo.JsonTvShow;
import com.example.tvtracker.Repository.Api.ApiModels.TvShowBasicInfo.JsonTvShowBasicRoot;
import com.example.tvtracker.Repository.Api.ApiModels.TvShowDetails.JsonEpisode;
import com.example.tvtracker.Repository.Api.ApiModels.TvShowDetails.JsonTvShowFull;
import com.example.tvtracker.Repository.Api.ApiModels.TvShowDetails.JsonTvShowFullRoot;
import com.example.tvtracker.UI.MainActivity;
import com.example.tvtracker.Repository.AppRepoHelpClasses.NetworkBoundResource;
import com.example.tvtracker.Repository.AppRepoHelpClasses.Resource;
import com.example.tvtracker.Repository.AppRepoHelpClasses.MultiTaskHandler;
import com.example.tvtracker.Models.CalendarTvShowEpisode;
import com.example.tvtracker.Helpers.DateHelper;
import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.Models.TvShowEpisode;
import com.example.tvtracker.Models.TvShowGenre;
import com.example.tvtracker.Models.TvShowPicture;
import com.example.tvtracker.Models.TvShowSeason;
import com.example.tvtracker.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AppRepository {
    private AppDao appDao;
    private Context context;


    private MutableLiveData<List<TvShowFull>> watchlistListObservable;
    private MutableLiveData<List<CalendarTvShowEpisode>> calendarListObservable;
    private MutableLiveData<TvShowSeason> seasonEpisodesListObservable;
    private MutableLiveData<List<String>> statisticsTvShowsListObservable;
    private MutableLiveData<Boolean> syncState;
    private MutableLiveData<List<TvShow>> searchTvShowsListObservable;

    private final MultiTaskHandler handler;

    public AppRepository(Application application) {
        context = application.getApplicationContext();
        AppDatabase database = AppDatabase.getInstance(application);
        appDao = database.appDao();
        watchlistListObservable = new MutableLiveData<>();
        calendarListObservable = new MutableLiveData<>();
        seasonEpisodesListObservable = new MutableLiveData<>();
        statisticsTvShowsListObservable = new MutableLiveData<>();
        syncState = new MutableLiveData<>();
        searchTvShowsListObservable = new MutableLiveData<>();

        if(!MainActivity.TEST_MODE) {
            handler = new MultiTaskHandler(MainActivity.TV_SHOW_MOST_POPULAR_PAGES_COUNT) {
                @Override
                protected void onAllTasksCompleted() {
                    syncState.setValue(true);
                }
            };

        }
    }

    public MutableLiveData<List<CalendarTvShowEpisode>> getCalendarListObservable() {
        return calendarListObservable;
    }
    public MutableLiveData<List<TvShowFull>> getWatchlistListObservable() {
        return watchlistListObservable;
    }
    public LiveData<TvShowSeason> getSeasonEpisodesListObservable() {
        return seasonEpisodesListObservable;
    }
    public LiveData<Boolean> getSyncState() {
        return syncState;
    }
    public LiveData<List<TvShow>> getSearchTvShowsListObservable() {
        return searchTvShowsListObservable;
    }

    public LiveData<List<String>> getStatisticsTvShowsListObservable() {
        return statisticsTvShowsListObservable;
    }


    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


    public void initialFetchData(){
        if(isNetworkConnected()) {
            ApiService apiService = ApiBuilder.getRetrofitInstance().create(ApiService.class);
            for (int i = 1; i <= MainActivity.TV_SHOW_MOST_POPULAR_PAGES_COUNT; i++) {
                apiService.getTvShowsBasic(i).enqueue(new Callback<JsonTvShowBasicRoot>() {
                    @Override
                    public void onResponse(Call<JsonTvShowBasicRoot> call, Response<JsonTvShowBasicRoot> response) {
                        if (response.isSuccessful()) {
//                    setTvShowsListObservableStatus(Status.SUCCESS, null);
                            addTvShowsToDb(toTvShowArray(response.body()));
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonTvShowBasicRoot> call, Throwable t) {
                        Log.d("Fetch from web", "error");
//                setTvShowsListObservableStatus(Status.ERROR, t.getMessage());
                    }
                });
            }
        }else {
            syncState.setValue(true);
        }
        //else if not network is available
    }

    public void initialFetchTestData(){
        AssetManager assetManager = context.getAssets();
        JsonTvShowBasicRoot jsonTvShowBasicRoot = new JsonTvShowBasicRoot();
        try (InputStream is = assetManager.open(context.getString(R.string.mostPopularTvShowsOffline)); // or file2
             Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            Gson gson = new Gson();
            jsonTvShowBasicRoot = gson.fromJson(reader, JsonTvShowBasicRoot.class);
            addTvShowsToDb(toTvShowArray(jsonTvShowBasicRoot));
            syncState.setValue(true);
        } catch (IOException e) {
            e.getMessage();
        }
    }

    public void fetchTestDetails(){
        AssetManager assetManager = context.getAssets();
        List<String> testData = new ArrayList<>();
        testData.add(context.getString(R.string.gameOfThronesDetails));
        testData.add(context.getString(R.string.theFlashDetails));
        testData.add(context.getString(R.string.theWalkingDeadDetails));

        for (String fileName : testData){
            JsonTvShowFullRoot jsonTvShowFullRoot = new JsonTvShowFullRoot();
            try (InputStream is = assetManager.open(fileName); // or file2
                 Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                Gson gson = new Gson();
                jsonTvShowFullRoot = gson.fromJson(reader, JsonTvShowFullRoot.class);
//                jsonTvShowFullRoot.getTvShow().setImagePath("file:///android_asset/"+fileName+".jpg");
                addDetailsToDbAsync(jsonTvShowFullRoot);
            } catch (IOException e) {
                e.getMessage();
            }
        }


    }



    public void fetchStatistics() {
        Log.d("", "load all tv shows from db");
        new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... voids) {
                List<TvShowFull> TvShowFulls =  appDao.getWatchlistTvShowsFull(MainActivity.TVSHOW_WATCHING_FLAG_YES);
                List<String> dataForStatistics = new ArrayList<>();
                int showsWithNextEpisodesCounter = 0;
                int showsNotEndedCounter = 0;
                int episodesCounter = 0;
                int episodeProgressCounter = 0;
                for (TvShowFull tvShowFull : TvShowFulls) {
                    if (TvShowHelper.getNextWatched(tvShowFull.episodes) != null) {
                        showsWithNextEpisodesCounter++;
                    }
                    if (tvShowFull.tvShow.getTvShowStatus() != MainActivity.STATUS_ENDED) {
                        showsNotEndedCounter++;
                    }
                    episodesCounter += tvShowFull.episodes.size();
                    episodeProgressCounter += TvShowHelper.getEpisodeProgress(tvShowFull.episodes);
                }
                String showsCount = String.valueOf(TvShowFulls.size());
                String showsWithNextEpisodesCount = String.valueOf(showsWithNextEpisodesCounter);
                String showsNotEndedCount = String.valueOf(showsNotEndedCounter);
                String episodesCount = String.valueOf(episodesCounter);
                String episodeProgressCount = String.valueOf(episodeProgressCounter);

                dataForStatistics.add(showsCount);
                dataForStatistics.add(showsWithNextEpisodesCount);
                dataForStatistics.add(showsNotEndedCount);
                dataForStatistics.add(episodesCount);
                dataForStatistics.add(episodeProgressCount);

                return dataForStatistics;
            }

            @Override
            protected void onPostExecute(List<String> strings) {
                    statisticsTvShowsListObservable.postValue(strings);
            }
        }.execute();
    }

    public void fetchWatchlist() {
        Log.d("", "load all tv shows from db");
        new AsyncTask<Void, Void, List<TvShowFull>>() {
            @Override
            protected List<TvShowFull> doInBackground(Void... voids) {
                List<TvShowFull> TvShowFulls =  appDao.getWatchlistTvShowsFull(MainActivity.TVSHOW_WATCHING_FLAG_YES);

                for (int i = 0; i < TvShowFulls.size(); i++) {
                    TvShowFull item = TvShowFulls.get(i);
                    if ((item.tvShow.getTvShowStatus().contains(MainActivity.STATUS_ENDED) && TvShowHelper.getNextWatched(item.episodes) == null)) {
                        TvShowFulls.remove(i);
                    }
                }

//                Collections.sort(tvShowFullList, new Comparator<TvShowFull>() {
//                    @Override
//                    public int compare(TvShowFull tvShowFull, TvShowFull t1) {
//                        if(tvShowFull.getTvShowState()) {
//                            return -1;
//                        }else if(tvShowFull.getTvShow().)
//                        return 0;
//                    }
//                }
                return TvShowFulls;
            }

            @Override
            protected void onPostExecute(List<TvShowFull> TvShowFulls) {
                if((TvShowFulls != null) && TvShowFulls.size()>0) {
                    watchlistListObservable.postValue(TvShowFulls);
                }
            }
        }.execute();
    }

    public void fetchCalendar(){
        new AsyncTask<Void, Void, List<CalendarTvShowEpisode>>(){
            @Override
            protected List<CalendarTvShowEpisode> doInBackground(Void... voids) {
                List<CalendarTvShowEpisode> calendarTvShowEpisodes = appDao.getCalendarEntries(MainActivity.TVSHOW_WATCHING_FLAG_YES);
                 return calendarTvShowEpisodes;
            }

            @Override
            protected void onPostExecute(List<CalendarTvShowEpisode> calendarTvShowEpisodes) {
                if(calendarTvShowEpisodes != null){
                    calendarListObservable.postValue(calendarTvShowEpisodes);
                }
            }
        }.execute();
    }

    public LiveData<List<TvShow>> fetchDiscover2() {
     return appDao.getAllTvShows();
    }


    public LiveData<Resource<TvShowFull>> fetchTvShowDetails2(int id){
        return new NetworkBoundResource<TvShowFull, JsonTvShowFullRoot>() {
            @Override
            protected void saveCallResult(@NonNull JsonTvShowFullRoot item) {
            detailsToDb(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable TvShowFull data) {
                if(!isNetworkConnected()) {
                    return false;

                }
                return true;

            }

            @NonNull
            @Override
            protected LiveData<TvShowFull> loadFromDb() {
                MutableLiveData<TvShowFull> fullMutableLiveData = new MutableLiveData<>();
                Log.d("", "load tv show detail from db");
                new AsyncTask<Void, Void, TvShowFull>() {
                    @Override
                    protected TvShowFull doInBackground(Void... voids) {
                        TvShowFull tvShowFull = appDao.getTvShowFullById(id);
                        return tvShowFull;
                    }

                    @Override
                    protected void onPostExecute(TvShowFull tvShowFull) {
                        fullMutableLiveData.postValue(tvShowFull);
                    }
                }.execute();
                return fullMutableLiveData;
            }

            @NonNull
            @Override
            protected Call<JsonTvShowFullRoot> createCall() {
                ApiService apiService = ApiBuilder.getRetrofitInstance().create(ApiService.class);
                return apiService.getTvShowDetailed(id);
            }
        }.getAsLiveData();
    }

    public void fetchTvShowDetailsFromSearch(int id){
        if(isNetworkConnected()) {
            Log.d("", "getDetailsFromWeb");
            ApiService apiService = ApiBuilder.getRetrofitInstance().create(ApiService.class);
            apiService.getTvShowDetailed(id).enqueue(new Callback<JsonTvShowFullRoot>() {
                @Override
                public void onResponse(Call<JsonTvShowFullRoot> call, Response<JsonTvShowFullRoot> response) {
                    if (response.isSuccessful()) {
                        addDetailsToDbAsync(response.body());
                    }
                }

                @Override
                public void onFailure(Call<JsonTvShowFullRoot> call, Throwable t) {

                }
            });
        }else{
            Log.d("", "no network available");
        }
    }

    public void fetchTvShowEpisodesBySeason(int id, int seasonNum){
        Log.d("", "load tv show season from db");
        new AsyncTask<Void, Void, TvShowSeason>() {
            @Override
            protected TvShowSeason doInBackground(Void... voids) {
                List<TvShowEpisode> episodes = appDao.getTvShowEpisodesByIdAndSeasonNum(id, seasonNum);
                TvShowSeason season = new TvShowSeason(seasonNum, episodes);
                return season;
            }

            @Override
            protected void onPostExecute(TvShowSeason tvShowSeason) {
                seasonEpisodesListObservable.postValue(tvShowSeason);
            }
        }.execute();
    }

    //Help Db methods
    private void detailsToDb(JsonTvShowFullRoot item){
        Log.d("", "add details to db");
        TvShowFull tvShowFull = jsonToModel(item);
        TvShow tvShow = tvShowFull.tvShow;

        int id = tvShow.getTvShowId();
        List<TvShowEpisode> episodes = tvShowFull.episodes;

        Collections.sort(episodes, new Comparator<TvShowEpisode>() {
            @Override
            public int compare(TvShowEpisode ep1, TvShowEpisode ep2) {
                if (ep1.getEpisodeSeasonNum() == ep2.getEpisodeSeasonNum()) {
                    return 0;
                } else if (ep1.getEpisodeSeasonNum() > ep2.getEpisodeSeasonNum()) {
                    return 1;
                } else if (ep1.getEpisodeSeasonNum() < ep2.getEpisodeSeasonNum()) {
                    return -1;
                }
                return 0;
            }
        });

        for(TvShowEpisode episode : episodes){
            String shortDate = episode.getEpisodeAirDate();
            if(shortDate.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
                episode.setEpisodeAirDate(shortDate);
            }else{
                episode.setEpisodeAirDate("");
            }
        }
        List<TvShowGenre> genres = tvShowFull.genres;
        List<TvShowPicture> pictures = tvShowFull.pictures;

        List<TvShowEpisode> dbEpisodes = new ArrayList<>();
        List<TvShowGenre> dbGenres = new ArrayList<>();
        List<TvShowPicture> dbPictures = new ArrayList<>();

        TvShow dbTvShow = appDao.getTvShowByApiId(id);
        dbEpisodes = appDao.getTvShowEpisodesById(id);
        dbGenres = appDao.getTvShowGenresById(id);
        dbPictures = appDao.getTvShowPicturesByTvShowId(id);

        if(dbTvShow != null) {
            appDao.updateTvShowDetails(tvShow.getTvShowId(), tvShow.getTvShowDesc(), tvShow.getTvShowYoutubeLink(), tvShow.getTvShowRating());
        }else {
            appDao.insertTvShow(new TvShow(tvShow.getTvShowId(), tvShow.getTvShowName(), tvShow.getTvShowStartDate(), tvShow.getTvShowEndDate(), tvShow.getTvShowCountry(),  tvShow.getTvShowNetwork(), tvShow.getTvShowStatus(), tvShow.getTvShowImagePath()));
            appDao.updateTvShowDetails(tvShow.getTvShowId(), tvShow.getTvShowDesc(), tvShow.getTvShowYoutubeLink(), tvShow.getTvShowRating());
        }

//        TvShowEpisode testEpisode = new TvShowEpisode(35624, 7, 1, "Test Episode", "2020-08-11");
//        episodes.add(testEpisode);
        if(dbEpisodes.size() == 0) {
            appDao.insertAllTvShowEpisodes(episodes);
        }else{
            String lastEpisodeDate = appDao.getDateForTheLastEpisodeOfTvShowAired(id);
            for (TvShowEpisode episode : episodes){
                if(DateHelper.compareDates(lastEpisodeDate, episode.getEpisodeAirDate())){
                    appDao.insertTvShowEpisode(episode);
                }
            }
        }

        if(dbGenres.size() == 0) {
            appDao.insertAllTvShowGenres(genres);
        }

        if(dbPictures.size() == 0) {
            appDao.insertAllTvShowPictures(pictures);
        }
    }



    private void addDetailsToDbAsync(JsonTvShowFullRoot item){
        Log.d("", "add details to db");
        new AsyncTask<JsonTvShowFullRoot, Void, Void>() {
            @Override
            protected Void doInBackground(JsonTvShowFullRoot... jsonTvShowFullRoots) {
                detailsToDb(jsonTvShowFullRoots[0]);
                return null;
            }
        }.execute(item);
    }
    private void addTvShowsToDb(List<TvShow> tvShows)  {
        Log.d("", "add tv shows to db" );
        new  AsyncTask<List<TvShow>, Void, Void>() {
            @Override
            protected synchronized Void doInBackground(List<TvShow>... params) {
                List<TvShow> apiShows = params[0];
                List<Integer> ids = new ArrayList<>();
                for (int i = 0; i < apiShows.size(); i++) {
                    ids.add(apiShows.get(i).getTvShowId());
                }
                List<Integer> existingShowsIds = appDao.getTvShowsIfExists(ids);
                for (TvShow tvShow : apiShows) {
                    if (existingShowsIds.contains(tvShow.getTvShowId())) {
                        //update tv show fix all slots
                        appDao.updateTvShow(tvShow.getId(), tvShow.getTvShowName(), tvShow.getTvShowStatus(), tvShow.getTvShowStartDate(), tvShow.getTvShowEndDate(), tvShow.getTvShowCountry(), tvShow.getTvShowNetwork(), tvShow.getTvShowImagePath());
                    } else {
                        appDao.insertTvShow(tvShow);
                    }
                }
                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {

                if(!MainActivity.TEST_MODE){
                    handler.taskComplete();
                }
            }
        }.execute(tvShows);
    }
    public  void addTvShowToDb(TvShow tvShow){
        Log.d("", "add tv shows to db" );
        new  AsyncTask<TvShow, Void, Void>() {
            @Override
            protected synchronized Void doInBackground(TvShow... params) {
                    if (appDao.getTvShowByApiId(params[0].getTvShowId()) != null) {
                        //update tv show fix all slots
                        appDao.updateTvShow(tvShow.getId(), tvShow.getTvShowName(), tvShow.getTvShowStatus(), tvShow.getTvShowStartDate(), tvShow.getTvShowEndDate(), tvShow.getTvShowCountry(), tvShow.getTvShowNetwork(), tvShow.getTvShowImagePath());
                    } else {
                        appDao.insertTvShow(tvShow);
                    }
                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {
            }
        }.execute(tvShow);
    }

    public void setTvShowWatchingFlag(Pair<Integer, Boolean> params) {
        new AsyncTask<Void,Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                int id = params.first;
                boolean flag = params.second;
                appDao.updateTvShowWatchingFlag(id, flag);
                return null;
            }
        }.execute();
    }
    public void setTvShowEpisodeIsWatchedFlag(Pair<Integer, Boolean> params) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                int id = params.first;
                boolean flag = params.second;
                appDao.updateTvShowEpisodeWatchedFlag(id, flag);
                return null;
            }
        }.execute();
    }

    public void setTvShowAllSeasonWatched(Pair<List<Integer>, Boolean> params) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                List<Integer> ids = params.first;
                boolean flag = params.second;
                appDao.updateTvShowAllSeasonWatched(ids, flag);
                return null;
            }
        }.execute();
    }

    //-------------------------------------------------




    //Api calls

    public void searchTvShow(String searchWord, int pageNum) {
        ApiService apiService = ApiBuilder.getRetrofitInstance().create(ApiService.class);
        Call<JsonTvShowSearchRoot> jsonTvShowSearchRootCall = apiService.getTvShowSearch(searchWord, pageNum);
        jsonTvShowSearchRootCall.enqueue(new Callback<JsonTvShowSearchRoot>() {
            @Override
            public void onResponse(Call<JsonTvShowSearchRoot> call, Response<JsonTvShowSearchRoot> response) {
                JsonTvShowSearchRoot  root = response.body();
                searchTvShowsCallback(root);
                Log.e("BIGFAIL", String.valueOf(response));
            }

            @Override
            public void onFailure(Call<JsonTvShowSearchRoot> call, Throwable t) {
                Log.e("BIGFAIL", t.getMessage());
            }
        });
    }

    private void searchTvShowsCallback(JsonTvShowSearchRoot root){
        List<TvShow> searchedTvShows = new ArrayList<>();
        for (int i = 0; i < root.getTvShows().size(); i++) {
            JsonTvShow jsonTvShow = root.getTvShows().get(i);
            TvShow tvShow = new TvShow(jsonTvShow.getId(), jsonTvShow.getName(), jsonTvShow.getStartDate(), jsonTvShow.getEndDate(), jsonTvShow.getCountry(), jsonTvShow.getNetwork(), jsonTvShow.getStatus(), jsonTvShow.getImageThumbnailPath());
            searchedTvShows.add(tvShow);
        }
        searchTvShowsListObservable.setValue(searchedTvShows);
    }

    //Help Functions
    public void clearSearchedTvShows() {
        List<TvShow> emptyTvShows = new ArrayList<>();
        searchTvShowsListObservable.setValue(emptyTvShows);
    }

    private List<TvShow> toTvShowArray(JsonTvShowBasicRoot root) {
        List<TvShow> returnTvShows = new ArrayList<>();
        List<JsonTvShow> TVShows = root.getTVShows();
        for(int i=0;i<TVShows.size();i++){

            JsonTvShow urlTvShow = TVShows.get(i);;

            int tvShowId = urlTvShow.getId();
            String tvShowName = urlTvShow.getName();
            String tvShowStatus = urlTvShow.getStatus();
            String tvShowStartDate = urlTvShow.getStartDate();
            String tvShowEndDate = urlTvShow.getEndDate();
            String tvShowCountry = urlTvShow.getCountry();
            String tvShowNetwork = urlTvShow.getNetwork();
            String tvShowImage = urlTvShow.getImageThumbnailPath();


            TvShow tvShow = new TvShow(tvShowId, tvShowName, tvShowStartDate, tvShowEndDate, tvShowCountry, tvShowNetwork, tvShowStatus, tvShowImage);
            returnTvShows.add(tvShow);

        }
        return  returnTvShows;
    }

    private TvShowFull jsonToModel(JsonTvShowFullRoot root){
        JsonTvShowFull jsonTvShowFull = root.getTvShow();
        TvShow tvShow = new TvShow(jsonTvShowFull.getId(), jsonTvShowFull.getName(), jsonTvShowFull.getStartDate(), jsonTvShowFull.getEndDate(), jsonTvShowFull.getCountry(), jsonTvShowFull.getNetwork(), jsonTvShowFull.getStatus(), jsonTvShowFull.getImageThumbnailPath());
        tvShow.setTvShowDesc(jsonTvShowFull.getDescription());
        tvShow.setTvShowYoutubeLink(jsonTvShowFull.getYoutubeLink());
        tvShow.setTvShowRating(jsonTvShowFull.getRating());

        int id = jsonTvShowFull.getId();
        List<JsonEpisode> episodes = jsonTvShowFull.getJsonEpisodes();
        List<String> genres = jsonTvShowFull.getGenres();
        List<String> pictures = jsonTvShowFull.getPictures();

        List<TvShowGenre> tvShowGenres = new ArrayList<>();
        for(String genre : genres) {
            tvShowGenres.add(new TvShowGenre(id,genre));
        }

        List<TvShowEpisode> tvShowEpisodes = new ArrayList<>();
        for(JsonEpisode episode : episodes) {
            tvShowEpisodes.add(new TvShowEpisode(id,episode.getSeason(),episode.getEpisode(),episode.getName(),episode.getAirDate()));
        }

        List<TvShowPicture> tvShowPictures = new ArrayList<>();
        for(String picture : pictures) {
            tvShowPictures.add(new TvShowPicture(id, picture));
        }
        TvShowFull full = new TvShowFull();
        full.tvShow = tvShow;
        full.episodes = tvShowEpisodes;
        full.pictures = tvShowPictures;
        full.genres = tvShowGenres;
        return full;


    }

    //TvShow

    //TvShowPicture
    //TvShowEpisode
    //TvShowGenre
    //DetailsFragment
    //TvShow AsyncTasks

}
