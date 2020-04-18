package com.example.tvtracker.Models;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class TvShowCombined {
    @Embedded private TvShowBasic tvShowBasic;
    @Relation(
            parentColumn = "tv_show_id",
            entityColumn = "tv_show_id"
    )
    private List<TvShowDetails> tvShowDetails;

    public TvShowCombined(TvShowBasic tvShowBasic, List<TvShowDetails> tvShowDetails) {
        this.tvShowBasic = tvShowBasic;
        this.tvShowDetails = tvShowDetails;
    }

    public TvShowBasic getTvShowBasic() {
        return tvShowBasic;
    }

    public void setTvShowBasic(TvShowBasic tvShowBasic) {
        this.tvShowBasic = tvShowBasic;
    }

    public List<TvShowDetails> getTvShowDetails() {
        return tvShowDetails;
    }

    public void setTvShowDetails(List<TvShowDetails> tvShowDetails) {
        this.tvShowDetails = tvShowDetails;
    }
}
