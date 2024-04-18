package nhlgameupdatelambda.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nhlgameupdatelambda.data.boxscore.BoxscoreResponse;
import nhlgameupdatelambda.data.playbyplay.PlayByPlay;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

@Slf4j
public class NhlApiDao {
    // https://github.com/chaanakyaaM/max_nhl_scraper/blob/main/max_nhl_scraper/max_nhl_scraper.py#L13-L23
    // https://gitlab.com/dword4/nhlapi/-/blob/master/new-api.md
    private static final String PLAY_BY_PLAY_ENDPOINT = "https://api-web.nhle.com/v1/gamecenter/%s/play-by-play";
    private static final String SCHEDULE_ENDPOINT = "https://api-web.nhle.com/v1/club-schedule-season/{{team_abbr}}/{{season}}";
    private static final String BOXSCORE_ENDPOINT = "https://api-web.nhle.com/v1/gamecenter/%s/boxscore";
    private static final String SHIFT_REPORT_HOME_ENDPOINT = "http://www.nhl.com/scores/htmlreports/%s/TH%s.HTM";
    private static final String SHIFT_REPORT_AWAY_ENDPOINT = "http://www.nhl.com/scores/htmlreports/%s/TV%s.HTM";
    private static final String SHIFT_API_ENDPOINT = "https://api.nhle.com/stats/rest/en/shiftcharts?cayenneExp=gameId=%s";

    private static final String USER_AGENT = "User-Agent";
    private static final String USER_AGENT_VALUE = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:121.0) Gecko/20100101 Firefox/121.0";
    private static final String ACCEPT = "Accept";
    private static final String ACCEPT_VALUE = "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8";

    private final ObjectMapper objectMapper;

    @Inject
    public NhlApiDao(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public BoxscoreResponse getBoxscore(final String gameId) {
        try {
            log.info("Getting Boxscore from NhlApi for gameId: " + gameId);
            final URLConnection urlConnection = getBoxscoreEndpoint(gameId)
                    .openConnection();
            addRequestProperties(urlConnection);

            final BoxscoreResponse boxscore = objectMapper.readValue(urlConnection.getInputStream(), BoxscoreResponse.class);
            log.info("Fetched Boxscore from NhlApi for gameId: " + boxscore.getId());
            return boxscore;
        } catch (final Exception e) {
            throw new RuntimeException(String.format("Unable to fetch boxscore data for gameId %s", gameId), e);
        }
    }

    public PlayByPlay getPlayByPlay(final String gameId) {
        try {
            log.info("Getting PlayByPlay from NhlApi for gameId: " + gameId);
            final URLConnection urlConnection = getPlayByPlayEndpoint(gameId)
                    .openConnection();
            addRequestProperties(urlConnection);

            final PlayByPlay playByPlay = objectMapper.readValue(urlConnection.getInputStream(), PlayByPlay.class);
            log.info("Fetched PlayByPlay from NhlApi for gameId: " + playByPlay.getId());
            return playByPlay;
        } catch (final Exception e) {
            throw new RuntimeException(String.format("Unable to fetch playByPlay data for gameId %s", gameId), e);
        }
    }

    private void addRequestProperties(final URLConnection urlConnection) {
        urlConnection.addRequestProperty(USER_AGENT, USER_AGENT_VALUE);
        urlConnection.addRequestProperty(ACCEPT, ACCEPT_VALUE);
    }

    private URL getBoxscoreEndpoint(final String gameId) throws MalformedURLException {
        return new URL(String.format(BOXSCORE_ENDPOINT, gameId));
    }
    private URL getPlayByPlayEndpoint(final String gameId) throws MalformedURLException {
        return new URL(String.format(PLAY_BY_PLAY_ENDPOINT, gameId));
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
