package nhlgameupdatelambda.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import nhlgameupdatelambda.data.BoxscoreResponse;

import java.net.MalformedURLException;
import java.net.URL;

public class NhlApiDao {
    // https://github.com/chaanakyaaM/max_nhl_scraper/blob/main/max_nhl_scraper/max_nhl_scraper.py#L13-L23
    // https://gitlab.com/dword4/nhlapi/-/blob/master/new-api.md
    public static final String PLAY_BY_PLAY_ENDPOINT = "https://api-web.nhle.com/v1/gamecenter/%s/play-by-play";
    private static final String SCHEDULE_ENDPOINT = "https://api-web.nhle.com/v1/club-schedule-season/{{team_abbr}}/{{season}}";
    private static final String BOXSCORE_ENDPOINT = "https://api-web.nhle.com/v1/gamecenter/%s/boxscore";
    private static final String SHIFT_REPORT_HOME_ENDPOINT = "http://www.nhl.com/scores/htmlreports/%s/TH%s.HTM";
    private static final String SHIFT_REPORT_AWAY_ENDPOINT = "http://www.nhl.com/scores/htmlreports/%s/TV%s.HTM";
    private static final String SHIFT_API_ENDPOINT = "https://api.nhle.com/stats/rest/en/shiftcharts?cayenneExp=gameId=%s";

    private static final ObjectMapper mapper = new ObjectMapper();

    public BoxscoreResponse getBoxscore(final String gameId) {
        try {
            final URL url = getBoxscoreEndpoint(gameId);
            return mapper.readValue(url, BoxscoreResponse.class);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Unable to fetch boxscore data for gameId %s", gameId), e);
        }
    }

    private URL getBoxscoreEndpoint(final String gameId) throws MalformedURLException {
        return new URL(String.format(BOXSCORE_ENDPOINT, gameId));
    }
    private String getPlayByPlayEndpoint(final String gameId) {
        return String.format(PLAY_BY_PLAY_ENDPOINT, gameId);
    }
    private String getShiftReportHomeEndpoint(final String season, final String gameId) {
        return String.format(SHIFT_REPORT_HOME_ENDPOINT, season, gameId.substring(4));
    }

    private String getShiftReportAwayEndpoint(final String season, final String gameId) {
        return String.format(SHIFT_REPORT_AWAY_ENDPOINT, season, gameId.substring(4));
    }

    private String getShiftApiEndpoint(final String gameId) {
        return String.format(SHIFT_API_ENDPOINT, gameId);
    }
}
