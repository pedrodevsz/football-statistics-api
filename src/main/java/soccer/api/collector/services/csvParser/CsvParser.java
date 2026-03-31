package soccer.api.collector.services.csvParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

@Service
public class CsvParser {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("[dd/MM/yyyy][dd/MM/yy]");
    private static final List<String> DATE_HEADERS = Arrays.asList("date");
    private static final List<String> HOME_TEAM_HEADERS = Arrays.asList("hometeam", "home team", "teamhome", "home");
    private static final List<String> AWAY_TEAM_HEADERS = Arrays.asList("awayteam", "away team", "teamaway", "away");
    private static final List<String> HOME_GOALS_HEADERS = Arrays.asList("fthg", "homegoals", "home goals", "hg", "hgoal");
    private static final List<String> AWAY_GOALS_HEADERS = Arrays.asList("ftag", "awaygoals", "away goals", "ag", "agoal");
    private static final List<String> HOME_SHOTS_HEADERS = Arrays.asList("hs", "home shots", "homeshots");
    private static final List<String> AWAY_SHOTS_HEADERS = Arrays.asList("as", "away shots", "awayshots");
    private static final List<String> HOME_SHOTS_ON_TARGET_HEADERS = Arrays.asList("hst", "home shots on target", "homeshotstarget");
    private static final List<String> AWAY_SHOTS_ON_TARGET_HEADERS = Arrays.asList("ast", "away shots on target", "awayshotstarget");
    private static final List<String> HOME_CORNERS_HEADERS = Arrays.asList("hc", "home corners", "homecorners");
    private static final List<String> AWAY_CORNERS_HEADERS = Arrays.asList("ac", "away corners", "awaycorners");
    private static final List<String> HOME_FOULS_HEADERS = Arrays.asList("hf", "home fouls", "homefouls");
    private static final List<String> AWAY_FOULS_HEADERS = Arrays.asList("af", "away fouls", "awayfouls");

    public List<MatchCsv> parse(InputStream inputStream) throws IOException, CsvException {
        try (InputStreamReader inputReader = new InputStreamReader(inputStream);
             CSVReader reader = new CSVReader(inputReader)) {

            List<String[]> lines = reader.readAll();
            if (lines.isEmpty()) {
                return Collections.emptyList();
            }

            String[] header = lines.get(0);
            Map<String, Integer> headerIndex = buildHeaderIndex(header);
            int dateIndex = findHeaderIndex(headerIndex, DATE_HEADERS);
            int homeTeamIndex = findHeaderIndex(headerIndex, HOME_TEAM_HEADERS);
            int awayTeamIndex = findHeaderIndex(headerIndex, AWAY_TEAM_HEADERS);
            int homeGoalsIndex = findHeaderIndex(headerIndex, HOME_GOALS_HEADERS);
            int awayGoalsIndex = findHeaderIndex(headerIndex, AWAY_GOALS_HEADERS);
            int homeShotsIndex = findHeaderIndex(headerIndex, HOME_SHOTS_HEADERS);
            int awayShotsIndex = findHeaderIndex(headerIndex, AWAY_SHOTS_HEADERS);
            int homeShotsOnTargetIndex = findHeaderIndex(headerIndex, HOME_SHOTS_ON_TARGET_HEADERS);
            int awayShotsOnTargetIndex = findHeaderIndex(headerIndex, AWAY_SHOTS_ON_TARGET_HEADERS);
            int homeCornersIndex = findHeaderIndex(headerIndex, HOME_CORNERS_HEADERS);
            int awayCornersIndex = findHeaderIndex(headerIndex, AWAY_CORNERS_HEADERS);
            int homeFoulsIndex = findHeaderIndex(headerIndex, HOME_FOULS_HEADERS);
            int awayFoulsIndex = findHeaderIndex(headerIndex, AWAY_FOULS_HEADERS);

            if (!isValidHeaderIndex(dateIndex, homeTeamIndex, awayTeamIndex, homeGoalsIndex, awayGoalsIndex)) {
                return Collections.emptyList();
            }

            List<MatchCsv> result = new ArrayList<>();
            for (int rowIndex = 1; rowIndex < lines.size(); rowIndex++) {
                String[] row = lines.get(rowIndex);
                if (isEmptyRow(row) || isHeaderRow(row, header)) {
                    continue;
                }

                if (!hasRequiredColumns(row, dateIndex, homeTeamIndex, awayTeamIndex, homeGoalsIndex, awayGoalsIndex)) {
                    continue;
                }

                String date = safeValue(row, dateIndex);
                String homeTeam = safeValue(row, homeTeamIndex);
                String awayTeam = safeValue(row, awayTeamIndex);
                String homeGoals = safeValue(row, homeGoalsIndex);
                String awayGoals = safeValue(row, awayGoalsIndex);

                if (isBlank(date) || isBlank(homeTeam) || isBlank(awayTeam) || isBlank(homeGoals) || isBlank(awayGoals)) {
                    continue;
                }

                if (!isInteger(homeGoals) || !isInteger(awayGoals)) {
                    continue;
                }

                if (!isValidDate(date)) {
                    continue;
                }

                MatchCsv dto = new MatchCsv();
                dto.setDate(date);
                dto.setHomeTeam(homeTeam);
                dto.setAwayTeam(awayTeam);
                dto.setHomeGoals(homeGoals);
                dto.setAwayGoals(awayGoals);
                dto.setHomeShots(safeValue(row, homeShotsIndex));
                dto.setAwayShots(safeValue(row, awayShotsIndex));
                dto.setHomeShotsOnTarget(safeValue(row, homeShotsOnTargetIndex));
                dto.setAwayShotsOnTarget(safeValue(row, awayShotsOnTargetIndex));
                dto.setHomeCorners(safeValue(row, homeCornersIndex));
                dto.setAwayCorners(safeValue(row, awayCornersIndex));
                dto.setHomeFouls(safeValue(row, homeFoulsIndex));
                dto.setAwayFouls(safeValue(row, awayFoulsIndex));
                result.add(dto);
            }

            return result;
        }
    }

    private Map<String, Integer> buildHeaderIndex(String[] header) {
        Map<String, Integer> index = new HashMap<>();
        if (header == null) {
            return index;
        }

        for (int i = 0; i < header.length; i++) {
            String key = safeValue(header, i).toLowerCase();
            if (!key.isEmpty()) {
                index.put(key, i);
            }
        }
        return index;
    }

    private int findHeaderIndex(Map<String, Integer> headerIndex, List<String> possibleNames) {
        for (String name : possibleNames) {
            Integer index = headerIndex.get(name.toLowerCase());
            if (index != null) {
                return index;
            }
        }
        return -1;
    }

    private boolean isValidHeaderIndex(int... indexes) {
        for (int index : indexes) {
            if (index < 0) {
                return false;
            }
        }
        return true;
    }

    private boolean hasRequiredColumns(String[] row, int... indexes) {
        if (row == null) {
            return false;
        }
        for (int index : indexes) {
            if (index < 0 || index >= row.length) {
                return false;
            }
        }
        return true;
    }

    private boolean isEmptyRow(String[] row) {
        if (row == null || row.length == 0) {
            return true;
        }
        for (String value : row) {
            if (!isBlank(value)) {
                return false;
            }
        }
        return true;
    }

    private boolean isHeaderRow(String[] row, String[] header) {
        if (row == null || header == null || row.length != header.length) {
            return false;
        }
        for (int i = 0; i < row.length; i++) {
            String rowValue = safeValue(row, i).toLowerCase();
            String headerValue = safeValue(header, i).toLowerCase();
            if (!rowValue.equals(headerValue)) {
                return false;
            }
        }
        return true;
    }

    private String safeValue(String[] row, int index) {
        if (row == null || index < 0 || index >= row.length) {
            return "";
        }
        String value = row[index];
        return value == null ? "" : value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private boolean isValidDate(String value) {
        try {
            LocalDate.parse(value, DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }
}
