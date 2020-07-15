package com.watermelon.Repository;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.watermelon.Helpers.ConnectivityHelper;
import com.watermelon.Helpers.TvSeriesHelper;
import com.watermelon.Models.TvSeriesCalendarEpisode;
import com.watermelon.Models.TvSeries;
import com.watermelon.Models.TvSeriesEpisode;
import com.watermelon.Models.TvSeriesFull;
import com.watermelon.Models.TvSeriesGenre;
import com.watermelon.Repository.Api.ApiBuilder;
import com.watermelon.Repository.Api.ApiService;
import com.watermelon.Repository.Api.ApiModels.JsonTvSeriesSearchRoot;
import com.watermelon.Repository.Api.ApiModels.TvSeriesBasicInfo.JsonTvSeries;
import com.watermelon.Repository.Api.ApiModels.TvSeriesBasicInfo.JsonTvSeriesBasicRoot;
import com.watermelon.Repository.Api.ApiModels.TvSeriesDetails.JsonTvSeriesFullRoot;
import com.watermelon.UI.WatermelonActivity;
import com.watermelon.Repository.AppRepoHelpClasses.NetworkBoundResource;
import com.watermelon.Repository.AppRepoHelpClasses.Resource;
import com.watermelon.Repository.AppRepoHelpClasses.MultiTaskHandler;
import com.watermelon.Helpers.DateHelper;
import com.watermelon.Models.TvSeriesPicture;
import com.watermelon.Models.TvSeriesSeason;
import com.watermelon.R;
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

    private LiveData<List<TvSeriesFull>> watchlistListObservable;
    private LiveData<List<TvSeriesCalendarEpisode>> calendarListObservable;
    private LiveData<List<TvSeries>> discoverListObservable;
    private MutableLiveData<List<String>> statisticsTvSeriesListObservable;
    private MutableLiveData<TvSeriesSeason> seasonEpisodesListObservable;
    private MutableLiveData<Boolean> syncState;
    private MutableLiveData<List<TvSeries>> searchTvSeriesListObservable;

    private final MultiTaskHandler handler;

    public AppRepository(Application application) {
        context = application.getApplicationContext();
        AppDatabase database = AppDatabase.getInstance(application);
        appDao = database.appDao();
        watchlistListObservable = appDao.getWatchlistTvSeriesFull(WatermelonActivity.TVSERIES_WATCHED_FLAG_YES);
        calendarListObservable = appDao.getCalendarTvSeries(WatermelonActivity.TVSERIES_WATCHED_FLAG_YES);
        discoverListObservable = appDao.getDiscoverTvSeries();
        statisticsTvSeriesListObservable = new MutableLiveData<>();
        seasonEpisodesListObservable = new MutableLiveData<>();
        syncState = new MutableLiveData<>();
        searchTvSeriesListObservable = new MutableLiveData<>();

        if (!WatermelonActivity.TEST_MODE) {
            handler = new MultiTaskHandler(WatermelonActivity.TV_SERIES_MOST_POPULAR_PAGES_COUNT) {
                @Override
                protected void onAllTasksCompleted() {
                    syncState.setValue(true);
                }
            };

        }
    }

    public LiveData<List<TvSeriesFull>> getWatchlistListObservable() {
        return watchlistListObservable;
    }

    public LiveData<List<TvSeriesCalendarEpisode>> getCalendarListObservable() {
        return calendarListObservable;
    }

    public LiveData<List<TvSeries>> getDiscoverListObservable() {
        return discoverListObservable;
    }

    public LiveData<List<String>> getStatisticsTvSeriesListObservable() {
        return statisticsTvSeriesListObservable;
    }

    public LiveData<TvSeriesSeason> getSeasonEpisodesListObservable() {
        return seasonEpisodesListObservable;
    }

    public LiveData<List<TvSeries>> getSearchTvSeriesListObservable() {
        return searchTvSeriesListObservable;
    }

    public LiveData<Boolean> getSyncState() {
        return syncState;
    }

    public void initialFetchDataFromApi() {
        if (ConnectivityHelper.isConnectedFast(context)) {
            ApiService apiService = ApiBuilder.getRetrofitInstance().create(ApiService.class);
            for (int i = 1; i <= WatermelonActivity.TV_SERIES_MOST_POPULAR_PAGES_COUNT; i++) {
                apiService.getTvSeriesBasic(i).enqueue(new Callback<JsonTvSeriesBasicRoot>() {
                    @Override
                    public void onResponse(Call<JsonTvSeriesBasicRoot> call, Response<JsonTvSeriesBasicRoot> response) {
                        if (response.isSuccessful()) {
                            addTvSeriesToDb(TvSeriesHelper.toTvSeriesArray(response.body()));
                            Log.d("initialFetchData", "Succesfull call");
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonTvSeriesBasicRoot> call, Throwable t) {
                        Log.d("initialFetchData", "Error");
                    }
                });
            }
        } else {
            syncState.setValue(true);
        }
    }

    public void initialFetchTestDataFromOffline() {
        AssetManager assetManager = context.getAssets();
        JsonTvSeriesBasicRoot jsonTvSeriesBasicRoot = new JsonTvSeriesBasicRoot();
        try (InputStream is = assetManager.open(context.getString(R.string.mostPopularTvSeriesOffline)); // or file2
             Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            Gson gson = new Gson();
            jsonTvSeriesBasicRoot = gson.fromJson(reader, JsonTvSeriesBasicRoot.class);
            addTvSeriesToDb(TvSeriesHelper.toTvSeriesArray(jsonTvSeriesBasicRoot));
            syncState.setValue(true);
        } catch (IOException e) {
            e.getMessage();
        }
    }

    public void fetchTestDetailsFromOffline() {
        AssetManager assetManager = context.getAssets();
        List<String> testData = new ArrayList<>();
        testData.add(context.getString(R.string.gameOfThronesDetails));
        testData.add(context.getString(R.string.theFlashDetails));
        testData.add(context.getString(R.string.theWalkingDeadDetails));

        for (String fileName : testData) {
            JsonTvSeriesFullRoot jsonTvSeriesFullRoot = new JsonTvSeriesFullRoot();
            try (InputStream is = assetManager.open(fileName); // or file2
                 Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                Gson gson = new Gson();
                jsonTvSeriesFullRoot = gson.fromJson(reader, JsonTvSeriesFullRoot.class);
                addDetailsToDbAsync(jsonTvSeriesFullRoot);
            } catch (IOException e) {
                e.getMessage();
            }
        }


    }

    public void fetchStatistics() {
        Log.d("Statistics", "fetch statistics");
        new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... voids) {
                List<TvSeriesFull> tvSeriesFulls = appDao.getStatisticsTvSeriesFull(WatermelonActivity.TVSERIES_WATCHED_FLAG_YES);
                List<String> dataForStatistics = new ArrayList<>();
                int showsWithNextEpisodesCounter = 0;
                int showsRunningCounter = 0;
                int episodesCounter = 0;
                int episodeProgressCounter = 0;
                int totalRuntimeCounter = 0;
                for (TvSeriesFull tvSeriesFull : tvSeriesFulls) {
                    TvSeries tvSeries = tvSeriesFull.getTvSeries();
                    List<TvSeriesEpisode> episodes = tvSeriesFull.getEpisodes();
                    if (TvSeriesHelper.getNextWatched(episodes) != null) {
                        showsWithNextEpisodesCounter++;
                    }
                    if (tvSeries.getTvSeriesStatus().contains(WatermelonActivity.STATUS_RUNNING)) {
                        showsRunningCounter++;
                    }
                    episodesCounter += episodes.size();
                    episodeProgressCounter += TvSeriesHelper.getEpisodeProgress(episodes);
                    totalRuntimeCounter += episodes.size() * Integer.parseInt(tvSeries.getTvSeriesRuntime());
                }
                String showsCount = String.valueOf(tvSeriesFulls.size());
                String showsWithNextEpisodesCount = String.valueOf(showsWithNextEpisodesCounter);
                String showsNotEndedCount = String.valueOf(showsRunningCounter);
                String episodesCount = String.valueOf(episodesCounter);
                String episodeProgressCount = String.valueOf(episodeProgressCounter);
                String totalRuntime = String.valueOf(totalRuntimeCounter);

                dataForStatistics.add(showsCount);
                dataForStatistics.add(showsWithNextEpisodesCount);
                dataForStatistics.add(showsNotEndedCount);
                dataForStatistics.add(episodesCount);
                dataForStatistics.add(episodeProgressCount);
                dataForStatistics.add(totalRuntime);

                return dataForStatistics;
            }

            @Override
            protected void onPostExecute(List<String> strings) {
                statisticsTvSeriesListObservable.postValue(strings);
            }
        }.execute();
    }

    public LiveData<Resource<TvSeriesFull>> fetchTvSeriesDetails(int id) {
        return new NetworkBoundResource<TvSeriesFull, JsonTvSeriesFullRoot>() {
            @Override
            protected void saveCallResult(@NonNull JsonTvSeriesFullRoot item) {
                detailsToDb(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable TvSeriesFull data) {
                return ConnectivityHelper.isConnectedFast(context);
            }

            @NonNull
            @Override
            protected LiveData<TvSeriesFull> loadFromDb() {
                return appDao.getTvSeriesFullById(id);
            }

            @NonNull
            @Override
            protected Call<JsonTvSeriesFullRoot> createCall() {
                ApiService apiService = ApiBuilder.getRetrofitInstance().create(ApiService.class);
                return apiService.getTvSeriesDetailed(id);
            }
        }.getAsLiveData();
    }

    public void fetchTvSeriesDetailsFromSearch(int id) {
        if (ConnectivityHelper.isConnectedFast(context)) {
            Log.d("", "getDetailsFromWeb");
            ApiService apiService = ApiBuilder.getRetrofitInstance().create(ApiService.class);
            apiService.getTvSeriesDetailed(id).enqueue(new Callback<JsonTvSeriesFullRoot>() {
                @Override
                public void onResponse(Call<JsonTvSeriesFullRoot> call, Response<JsonTvSeriesFullRoot> response) {
                    if (response.isSuccessful()) {
                        addDetailsToDbAsync(response.body());
                    }
                }

                @Override
                public void onFailure(Call<JsonTvSeriesFullRoot> call, Throwable t) {

                }
            });
        } else {
            Log.d("", "no network available or not a fast one :D");
        }
    }

    public void fetchTvSeriesEpisodesBySeason(int id, int seasonNum) {
        Log.d("", "fetchTvSeriesEpisodesBySeason");
        new AsyncTask<Void, Void, TvSeriesSeason>() {
            @Override
            protected TvSeriesSeason doInBackground(Void... voids) {
                List<TvSeriesEpisode> episodes = appDao.getTvSeriesEpisodesByIdAndSeasonNum(id, seasonNum);
                TvSeriesSeason season = new TvSeriesSeason(seasonNum, episodes);
                return season;
            }

            @Override
            protected void onPostExecute(TvSeriesSeason tvSeriesSeason) {
                seasonEpisodesListObservable.postValue(tvSeriesSeason);
            }
        }.execute();
    }

    //Help Db methods
    private void detailsToDb(JsonTvSeriesFullRoot item) {
        Log.d("", "detailsToDb");
        TvSeriesFull tvSeriesFull = TvSeriesHelper.jsonToModel(item);
        TvSeries tvSeries = tvSeriesFull.getTvSeries();

        int id = tvSeries.getTvSeriesId();
        List<TvSeriesEpisode> episodes = tvSeriesFull.getEpisodes();

        Collections.sort(episodes, new Comparator<TvSeriesEpisode>() {
            @Override
            public int compare(TvSeriesEpisode ep1, TvSeriesEpisode ep2) {
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

        for (TvSeriesEpisode episode : episodes) {
            String shortDate = episode.getEpisodeAirDate();
            if (shortDate.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
                episode.setEpisodeAirDate(shortDate);
            } else {
                episode.setEpisodeAirDate("");
            }
        }
        List<TvSeriesGenre> genres = tvSeriesFull.getGenres();
        List<TvSeriesPicture> pictures = tvSeriesFull.getPictures();

        TvSeriesFull dbTvSeries = appDao.getTvSeriesByApiId(id);

        if (dbTvSeries != null) {
            appDao.updateTvSeriesDetails(tvSeries.getTvSeriesId(), tvSeries.getTvSeriesDesc(), tvSeries.getTvSeriesRuntime(), tvSeries.getTvSeriesYoutubeLink(), tvSeries.getTvSeriesRating());
        } else {
            appDao.insertTvSeries(new TvSeries(tvSeries.getTvSeriesId(), tvSeries.getTvSeriesName(), tvSeries.getTvSeriesStartDate(), tvSeries.getTvSeriesEndDate(), tvSeries.getTvSeriesCountry(), tvSeries.getTvSeriesNetwork(), tvSeries.getTvSeriesStatus(), tvSeries.getTvSeriesImagePath()));
            appDao.updateTvSeriesDetails(tvSeries.getTvSeriesId(), tvSeries.getTvSeriesDesc(), tvSeries.getTvSeriesRuntime(), tvSeries.getTvSeriesYoutubeLink(), tvSeries.getTvSeriesRating());
        }

//        TvSeriesEpisode testEpisode = new TvSeriesEpisode(35624, 7, 1, "Test Episode", "2020-08-11");
//        episodes.add(testEpisode);

        if (dbTvSeries == null){
            appDao.insertAllTvSeriesEpisodes(episodes);
            appDao.insertAllTvSeriesGenres(genres);
            appDao.insertAllTvSeriesPictures(pictures);
        }else {
            if (dbTvSeries.getEpisodes().size() == 0) {
                appDao.insertAllTvSeriesEpisodes(episodes);
            } else {
                String lastEpisodeDate = appDao.getDateForTheLastEpisodeOfTvSeriesAired(id);
                for (TvSeriesEpisode episode : episodes) {
                    if (DateHelper.compareDates(lastEpisodeDate, episode.getEpisodeAirDate())) {
                        appDao.insertTvSeriesEpisode(episode);
                    }
                }
            }
            if (dbTvSeries.getGenres().size() == 0) {
                appDao.insertAllTvSeriesGenres(genres);
            }
            if (dbTvSeries.getPictures().size() == 0) {
                appDao.insertAllTvSeriesPictures(pictures);
            }
        }
    }


    private void addDetailsToDbAsync(JsonTvSeriesFullRoot item) {
        Log.d("", "addDetailsToDbAsync");
        new AsyncTask<JsonTvSeriesFullRoot, Void, Void>() {
            @Override
            protected Void doInBackground(JsonTvSeriesFullRoot... jsonTvSeriesFullRoots) {
                detailsToDb(jsonTvSeriesFullRoots[0]);
                return null;
            }
        }.execute(item);
    }

    private void addTvSeriesToDb(List<TvSeries> tvSeries) {
        Log.d("", "add tv shows to db");
        new AsyncTask<List<TvSeries>, Void, Void>() {
            @Override
            protected synchronized Void doInBackground(List<TvSeries>... params) {
                List<TvSeries> apiShows = params[0];
                List<Integer> ids = new ArrayList<>();
                for (int i = 0; i < apiShows.size(); i++) {
                    ids.add(apiShows.get(i).getTvSeriesId());
                }
                List<Integer> existingShowsIds = appDao.getTvSeriesIfExists(ids);
                for (TvSeries tvSeries : apiShows) {
                    if (existingShowsIds.contains(tvSeries.getTvSeriesId())) {
                        //update tv show fix all slots
                        appDao.updateTvSeries(tvSeries.getId(), tvSeries.getTvSeriesName(), tvSeries.getTvSeriesStatus(), tvSeries.getTvSeriesStartDate(), tvSeries.getTvSeriesEndDate(), tvSeries.getTvSeriesCountry(), tvSeries.getTvSeriesNetwork(), tvSeries.getTvSeriesImagePath());
                    } else {
                        appDao.insertTvSeries(tvSeries);
                    }
                }
                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {

                if (!WatermelonActivity.TEST_MODE) {
                    handler.taskComplete();
                }
            }
        }.execute(tvSeries);
    }

    public void addSingleTvSeriesToDb(TvSeries tvSeries) {
        Log.d("", "add tv shows to db");
        new AsyncTask<TvSeries, Void, Void>() {
            @Override
            protected synchronized Void doInBackground(TvSeries... params) {
                if (appDao.getTvSeriesByApiId(params[0].getTvSeriesId()) != null) {
                    //update tv show fix all slots
                    appDao.updateTvSeries(tvSeries.getId(), tvSeries.getTvSeriesName(), tvSeries.getTvSeriesStatus(), tvSeries.getTvSeriesStartDate(), tvSeries.getTvSeriesEndDate(), tvSeries.getTvSeriesCountry(), tvSeries.getTvSeriesNetwork(), tvSeries.getTvSeriesImagePath());
                } else {
                    appDao.insertTvSeries(tvSeries);
                }
                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {
            }
        }.execute(tvSeries);
    }

    public void setTvSeriesWatchedFlag(Pair<Integer, Boolean> params) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                int id = params.first;
                boolean flag = params.second;
                appDao.updateTvSeriesWatchedFlag(id, flag);
                return null;
            }
        }.execute();
    }

    public void setTvSeriesEpisodeWatchedFlag(Pair<Integer, Boolean> params) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                int id = params.first;
                boolean flag = params.second;
                appDao.updateTvSeriesEpisodeWatchedFlag(id, flag);
                return null;
            }
        }.execute();
    }

    public void setTvSeriesAllSeasonWatched(Pair<List<Integer>, Boolean> params) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                List<Integer> ids = params.first;
                boolean flag = params.second;
                appDao.updateTvSeriesAllSeasonWatched(ids, flag);
                return null;
            }
        }.execute();
    }

    //-------------------------------------------------


    //Api calls

    public void searchTvSeries(String searchWord, int pageNum) {
        ApiService apiService = ApiBuilder.getRetrofitInstance().create(ApiService.class);
        Call<JsonTvSeriesSearchRoot> jsonTvSeriesSearchRootCall = apiService.getTvSeriesSearch(searchWord, pageNum);
        jsonTvSeriesSearchRootCall.enqueue(new Callback<JsonTvSeriesSearchRoot>() {
            @Override
            public void onResponse(Call<JsonTvSeriesSearchRoot> call, Response<JsonTvSeriesSearchRoot> response) {
                JsonTvSeriesSearchRoot root = response.body();
                addTvSeriesToSearchList(root);
                Log.e("", String.valueOf(response));
            }

            @Override
            public void onFailure(Call<JsonTvSeriesSearchRoot> call, Throwable t) {
                Log.e("BIGFAIL", t.getMessage());
            }
        });
    }

    private void addTvSeriesToSearchList(JsonTvSeriesSearchRoot root) {
        List<TvSeries> searchedTvSeries = new ArrayList<>();
        for (int i = 0; i < root.getTvShows().size(); i++) {
            JsonTvSeries jsonTvSeries = root.getTvShows().get(i);
            TvSeries tvSeries = new TvSeries(jsonTvSeries.getId(), jsonTvSeries.getName(), jsonTvSeries.getStartDate(), jsonTvSeries.getEndDate(), jsonTvSeries.getCountry(), jsonTvSeries.getNetwork(), jsonTvSeries.getStatus(), jsonTvSeries.getImageThumbnailPath());
            searchedTvSeries.add(tvSeries);
        }
        searchTvSeriesListObservable.setValue(searchedTvSeries);
    }

    public void clearSearchList() {
        List<TvSeries> emptyTvSeries = new ArrayList<>();
        searchTvSeriesListObservable.setValue(emptyTvSeries);
    }


    //TvSeries

    //TvSeriesPicture
    //TvSeriesEpisode
    //TvSeriesGenre
    //DetailsFragment
    //TvSeries AsyncTasks

}
