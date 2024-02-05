package nhlgameupdatelambda.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import nhlgameupdatelambda.data.BoxscoreResponse;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

public class NhlApiDao {
    // https://github.com/chaanakyaaM/max_nhl_scraper/blob/main/max_nhl_scraper/max_nhl_scraper.py#L13-L23
    // https://gitlab.com/dword4/nhlapi/-/blob/master/new-api.md
    public static final String PLAY_BY_PLAY_ENDPOINT = "https://api-web.nhle.com/v1/gamecenter/%s/play-by-play";
    private static final String SCHEDULE_ENDPOINT = "https://api-web.nhle.com/v1/club-schedule-season/{{team_abbr}}/{{season}}";
    private static final String BOXSCORE_ENDPOINT = "https://api-web.nhle.com/v1/gamecenter/%s/boxscore";
    private static final String SHIFT_REPORT_HOME_ENDPOINT = "http://www.nhl.com/scores/htmlreports/%s/TH%s.HTM";
    private static final String SHIFT_REPORT_AWAY_ENDPOINT = "http://www.nhl.com/scores/htmlreports/%s/TV%s.HTM";
    private static final String SHIFT_API_ENDPOINT = "https://api.nhle.com/stats/rest/en/shiftcharts?cayenneExp=gameId=%s";

    private static final String USER_AGENT = "User-Agent";
    private static final String USER_AGENT_VALUE = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:121.0) Gecko/20100101 Firefox/121.0";
    private static final String ACCEPT = "Accept";
    private static final String ACCEPT_VALUE = "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8";

    private static final ObjectMapper mapper = new ObjectMapper();

    public BoxscoreResponse getBoxscore(final String gameId) {
        try {
            final URL url = getBoxscoreEndpoint(gameId);
            final URLConnection urlConnection = url.openConnection();
            urlConnection.addRequestProperty(USER_AGENT, USER_AGENT_VALUE);
            urlConnection.addRequestProperty(ACCEPT, ACCEPT_VALUE);

            return mapper.readValue(urlConnection.getInputStream(), BoxscoreResponse.class);
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
