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
import com.watermelon.Helpers.TvShowHelper;
import com.watermelon.Models.TvSeries;
import com.watermelon.Models.TvSeriesEpisode;
import com.watermelon.Models.TvSeriesFull;
import com.watermelon.Models.TvSeriesGenre;
import com.watermelon.Repository.Api.ApiBuilder;
import com.watermelon.Repository.Api.ApiService;
import com.watermelon.Repository.Api.ApiModels.JsonTvShowSearchRoot;
import com.watermelon.Repository.Api.ApiModels.TvShowBasicInfo.JsonTvShow;
import com.watermelon.Repository.Api.ApiModels.TvShowBasicInfo.JsonTvShowBasicRoot;
import com.watermelon.Repository.Api.ApiModels.TvShowDetails.JsonTvShowFullRoot;
import com.watermelon.UI.WatermelonMainActivity;
import com.watermelon.Repository.AppRepoHelpClasses.NetworkBoundResource;
import com.watermelon.Repository.AppRepoHelpClasses.Resource;
import com.watermelon.Repository.AppRepoHelpClasses.MultiTaskHandler;
import com.watermelon.Models.CalendarTvShowEpisode;
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
    private LiveData<List<CalendarTvShowEpisode>> calendarListObservable;
    private LiveData<List<TvSeries>> discoverListObservable;
    private MutableLiveData<List<String>> statisticsTvShowsListObservable;
    private MutableLiveData<TvSeriesSeason> seasonEpisodesListObservable;
    private MutableLiveData<Boolean> syncState;
    private MutableLiveData<List<TvSeries>> searchTvShowsListObservable;

    private final MultiTaskHandler handler;

    public AppRepository(Application application) {
        context = application.getApplicationContext();
        AppDatabase database = AppDatabase.getInstance(application);
        appDao = database.appDao();
        watchlistListObservable = appDao.getWatchlistTvShowsFull(WatermelonMainActivity.TVSHOW_WATCHED_FLAG_YES);
        calendarListObservable = appDao.getCalendarTvShows(WatermelonMainActivity.TVSHOW_WATCHED_FLAG_YES);
        discoverListObservable = appDao.getDiscoverTvShows();
        statisticsTvShowsListObservable = new MutableLiveData<>();
        seasonEpisodesListObservable = new MutableLiveData<>();
        syncState = new MutableLiveData<>();
        searchTvShowsListObservable = new MutableLiveData<>();

        if (!WatermelonMainActivity.TEST_MODE) {
            handler = new MultiTaskHandler(WatermelonMainActivity.TV_SHOW_MOST_POPULAR_PAGES_COUNT) {
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

    public LiveData<List<CalendarTvShowEpisode>> getCalendarListObservable() {
        return calendarListObservable;
    }

    public LiveData<List<TvSeries>> getDiscoverListObservable() {
        return discoverListObservable;
    }

    public LiveData<List<String>> getStatisticsTvShowsListObservable() {
        return statisticsTvShowsListObservable;
    }

    public LiveData<TvSeriesSeason> getSeasonEpisodesListObservable() {
        return seasonEpisodesListObservable;
    }

    public LiveData<List<TvSeries>> getSearchTvShowsListObservable() {
        return searchTvShowsListObservable;
    }

    public LiveData<Boolean> getSyncState() {
        return syncState;
    }

    public void initialFetchDataFromApi() {
        if (ConnectivityHelper.isConnectedFast(context)) {
            ApiService apiService = ApiBuilder.getRetrofitInstance().create(ApiService.class);
            for (int i = 1; i <= WatermelonMainActivity.TV_SHOW_MOST_POPULAR_PAGES_COUNT; i++) {
                apiService.getTvShowsBasic(i).enqueue(new Callback<JsonTvShowBasicRoot>() {
                    @Override
                    public void onResponse(Call<JsonTvShowBasicRoot> call, Response<JsonTvShowBasicRoot> response) {
                        if (response.isSuccessful()) {
                            addTvShowsToDb(TvShowHelper.toTvShowArray(response.body()));
                            Log.d("initialFetchData", "Succesfull call");
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonTvShowBasicRoot> call, Throwable t) {
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
        JsonTvShowBasicRoot jsonTvShowBasicRoot = new JsonTvShowBasicRoot();
        try (InputStream is = assetManager.open(context.getString(R.string.mostPopularTvShowsOffline)); // or file2
             Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            Gson gson = new Gson();
            jsonTvShowBasicRoot = gson.fromJson(reader, JsonTvShowBasicRoot.class);
            addTvShowsToDb(TvShowHelper.toTvShowArray(jsonTvShowBasicRoot));
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
            JsonTvShowFullRoot jsonTvShowFullRoot = new JsonTvShowFullRoot();
            try (InputStream is = assetManager.open(fileName); // or file2
                 Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                Gson gson = new Gson();
                jsonTvShowFullRoot = gson.fromJson(reader, JsonTvShowFullRoot.class);
                addDetailsToDbAsync(jsonTvShowFullRoot);
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
                List<TvSeriesFull> tvSeriesFulls = appDao.getStatisticsTvShowsFull(WatermelonMainActivity.TVSHOW_WATCHED_FLAG_YES);
                List<String> dataForStatistics = new ArrayList<>();
                int showsWithNextEpisodesCounter = 0;
                int showsNotEndedCounter = 0;
                int episodesCounter = 0;
                int episodeProgressCounter = 0;
                int totalRuntimeCounter = 0;
                for (TvSeriesFull tvSeriesFull : tvSeriesFulls) {
                    TvSeries tvSeries = tvSeriesFull.getTvSeries();
                    List<TvSeriesEpisode> episodes = tvSeriesFull.getEpisodes();
                    if (TvShowHelper.getNextWatched(episodes) != null) {
                        showsWithNextEpisodesCounter++;
                    }
                    if (tvSeries.getTvShowStatus() != WatermelonMainActivity.STATUS_ENDED) {
                        showsNotEndedCounter++;
                    }
                    episodesCounter += episodes.size();
                    episodeProgressCounter += TvShowHelper.getEpisodeProgress(episodes);
                    totalRuntimeCounter += episodes.size() * Integer.parseInt(tvSeries.getTvShowRuntime());
                }
                String showsCount = String.valueOf(tvSeriesFulls.size());
                String showsWithNextEpisodesCount = String.valueOf(showsWithNextEpisodesCounter);
                String showsNotEndedCount = String.valueOf(showsNotEndedCounter);
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
                statisticsTvShowsListObservable.postValue(strings);
            }
        }.execute();
    }

    public LiveData<Resource<TvSeriesFull>> fetchTvSeriesDetails(int id) {
        return new NetworkBoundResource<TvSeriesFull, JsonTvShowFullRoot>() {
            @Override
            protected void saveCallResult(@NonNull JsonTvShowFullRoot item) {
                detailsToDb(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable TvSeriesFull data) {
                return ConnectivityHelper.isConnectedFast(context);
            }

            @NonNull
            @Override
            protected LiveData<TvSeriesFull> loadFromDb() {
                return appDao.getTvShowFullById(id);
            }

            @NonNull
            @Override
            protected Call<JsonTvShowFullRoot> createCall() {
                ApiService apiService = ApiBuilder.getRetrofitInstance().create(ApiService.class);
                return apiService.getTvShowDetailed(id);
            }
        }.getAsLiveData();
    }

    public void fetchTvShowDetailsFromSearch(int id) {
        if (ConnectivityHelper.isConnectedFast(context)) {
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
        } else {
            Log.d("", "no network available or not a fast one :D");
        }
    }

    public void fetchTvShowEpisodesBySeason(int id, int seasonNum) {
        Log.d("", "fetchTvShowEpisodesBySeason");
        new AsyncTask<Void, Void, TvSeriesSeason>() {
            @Override
            protected TvSeriesSeason doInBackground(Void... voids) {
                List<TvSeriesEpisode> episodes = appDao.getTvShowEpisodesByIdAndSeasonNum(id, seasonNum);
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
    private void detailsToDb(JsonTvShowFullRoot item) {
        Log.d("", "detailsToDb");
        TvSeriesFull tvSeriesFull = TvShowHelper.jsonToModel(item);
        TvSeries tvSeries = tvSeriesFull.getTvSeries();

        int id = tvSeries.getTvShowId();
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

        TvSeriesFull dbTvSeries = appDao.getTvShowByApiId(id);

        if (dbTvSeries != null) {
            appDao.updateTvSeriesDetails(tvSeries.getTvShowId(), tvSeries.getTvShowDesc(), tvSeries.getTvShowRuntime(), tvSeries.getTvShowYoutubeLink(), tvSeries.getTvShowRating());
        } else {
            appDao.insertTvShow(new TvSeries(tvSeries.getTvShowId(), tvSeries.getTvShowName(), tvSeries.getTvShowStartDate(), tvSeries.getTvShowEndDate(), tvSeries.getTvShowCountry(), tvSeries.getTvShowNetwork(), tvSeries.getTvShowStatus(), tvSeries.getTvShowImagePath()));
            appDao.updateTvSeriesDetails(tvSeries.getTvShowId(), tvSeries.getTvShowDesc(), tvSeries.getTvShowRuntime(), tvSeries.getTvShowYoutubeLink(), tvSeries.getTvShowRating());
        }

//        TvSeriesEpisode testEpisode = new TvSeriesEpisode(35624, 7, 1, "Test Episode", "2020-08-11");
//        episodes.add(testEpisode);
        if (dbTvSeries.getEpisodes().size() == 0) {
            appDao.insertAllTvShowEpisodes(episodes);
        } else {
            String lastEpisodeDate = appDao.getDateForTheLastEpisodeOfTvShowAired(id);
            for (TvSeriesEpisode episode : episodes) {
                if (DateHelper.compareDates(lastEpisodeDate, episode.getEpisodeAirDate())) {
                    appDao.insertTvShowEpisode(episode);
                }
            }
        }

        if (dbTvSeries.getGenres().size() == 0) {
            appDao.insertAllTvShowGenres(genres);
        }

        if (dbTvSeries.getPictures().size() == 0) {
            appDao.insertAllTvShowPictures(pictures);
        }
    }


    private void addDetailsToDbAsync(JsonTvShowFullRoot item) {
        Log.d("", "addDetailsToDbAsync");
        new AsyncTask<JsonTvShowFullRoot, Void, Void>() {
            @Override
            protected Void doInBackground(JsonTvShowFullRoot... jsonTvShowFullRoots) {
                detailsToDb(jsonTvShowFullRoots[0]);
                return null;
            }
        }.execute(item);
    }

    private void addTvShowsToDb(List<TvSeries> tvSeries) {
        Log.d("", "add tv shows to db");
        new AsyncTask<List<TvSeries>, Void, Void>() {
            @Override
            protected synchronized Void doInBackground(List<TvSeries>... params) {
                List<TvSeries> apiShows = params[0];
                List<Integer> ids = new ArrayList<>();
                for (int i = 0; i < apiShows.size(); i++) {
                    ids.add(apiShows.get(i).getTvShowId());
                }
                List<Integer> existingShowsIds = appDao.getTvShowsIfExists(ids);
                for (TvSeries tvSeries : apiShows) {
                    if (existingShowsIds.contains(tvSeries.getTvShowId())) {
                        //update tv show fix all slots
                        appDao.updateTvShow(tvSeries.getId(), tvSeries.getTvShowName(), tvSeries.getTvShowStatus(), tvSeries.getTvShowStartDate(), tvSeries.getTvShowEndDate(), tvSeries.getTvShowCountry(), tvSeries.getTvShowNetwork(), tvSeries.getTvShowImagePath());
                    } else {
                        appDao.insertTvShow(tvSeries);
                    }
                }
                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {

                if (!WatermelonMainActivity.TEST_MODE) {
                    handler.taskComplete();
                }
            }
        }.execute(tvSeries);
    }

    public void addTvShowToDb(TvSeries tvSeries) {
        Log.d("", "add tv shows to db");
        new AsyncTask<TvSeries, Void, Void>() {
            @Override
            protected synchronized Void doInBackground(TvSeries... params) {
                if (appDao.getTvShowByApiId(params[0].getTvShowId()) != null) {
                    //update tv show fix all slots
                    appDao.updateTvShow(tvSeries.getId(), tvSeries.getTvShowName(), tvSeries.getTvShowStatus(), tvSeries.getTvShowStartDate(), tvSeries.getTvShowEndDate(), tvSeries.getTvShowCountry(), tvSeries.getTvShowNetwork(), tvSeries.getTvShowImagePath());
                } else {
                    appDao.insertTvShow(tvSeries);
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
                appDao.updateTvShowWatchedFlag(id, flag);
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

    public void setTvShowAllSeasonWatched(Pair<List<Integer>, Boolean> params) {
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
        Call<JsonTvShowSearchRoot> jsonTvShowSearchRootCall = apiService.getTvShowSearch(searchWord, pageNum);
        jsonTvShowSearchRootCall.enqueue(new Callback<JsonTvShowSearchRoot>() {
            @Override
            public void onResponse(Call<JsonTvShowSearchRoot> call, Response<JsonTvShowSearchRoot> response) {
                JsonTvShowSearchRoot root = response.body();
                addTvShowsToSearchList(root);
                Log.e("", String.valueOf(response));
            }

            @Override
            public void onFailure(Call<JsonTvShowSearchRoot> call, Throwable t) {
                Log.e("BIGFAIL", t.getMessage());
            }
        });
    }

    private void addTvShowsToSearchList(JsonTvShowSearchRoot root) {
        List<TvSeries> searchedTvSeries = new ArrayList<>();
        for (int i = 0; i < root.getTvShows().size(); i++) {
            JsonTvShow jsonTvShow = root.getTvShows().get(i);
            TvSeries tvSeries = new TvSeries(jsonTvShow.getId(), jsonTvShow.getName(), jsonTvShow.getStartDate(), jsonTvShow.getEndDate(), jsonTvShow.getCountry(), jsonTvShow.getNetwork(), jsonTvShow.getStatus(), jsonTvShow.getImageThumbnailPath());
            searchedTvSeries.add(tvSeries);
        }
        searchTvShowsListObservable.setValue(searchedTvSeries);
    }

    public void clearSearchList() {
        List<TvSeries> emptyTvSeries = new ArrayList<>();
        searchTvShowsListObservable.setValue(emptyTvSeries);
    }


    //TvSeries

    //TvSeriesPicture
    //TvSeriesEpisode
    //TvSeriesGenre
    //DetailsFragment
    //TvSeries AsyncTasks

}
