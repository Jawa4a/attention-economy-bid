package main.parser;

import main.dataModel.AuctionInput;
import main.dataModel.Summary;
import main.dataModel.Video;
import main.dataModel.Viewer;

import java.util.Arrays;

public final class InputParser {

    public boolean isAuctionLine(String line) {
        return line != null && line.contains("video.") && line.contains("viewer.");
    }

    public boolean isWinLine(String line) {
        return line != null && line.startsWith("W ");
    }

    public boolean isLossLine(String line) {
        return "L".equals(line);
    }

    public boolean isSummaryLine(String line) {
        return line != null && line.startsWith("S ");
    }

    public AuctionInput parseAuctionLine(String line) {
        if (!isAuctionLine(line)) {
            throw new IllegalArgumentException("Not an auction line: " + line);
        }

        String videoCategory = null;
        long viewCount = 0L;
        long commentCount = 0L;
        boolean subscribed = false;
        String age = "";
        String gender = "";
        String[] interests = new String[0];

        String[] parts = line.split(",");

        for (String part : parts) {
            int eq = part.indexOf('=');
            if (eq <= 0 || eq == part.length() - 1) {
                continue;
            }

            String key = part.substring(0, eq).trim();
            String value = part.substring(eq + 1).trim();

            switch (key) {
                case "video.category":
                    videoCategory = value;
                    break;
                case "video.viewCount":
                    viewCount = parseLongSafe(value);
                    break;
                case "video.commentCount":
                    commentCount = parseLongSafe(value);
                    break;
                case "viewer.subscribed":
                    subscribed = "Y".equalsIgnoreCase(value);
                    break;
                case "viewer.age":
                    age = value;
                    break;
                case "viewer.gender":
                    gender = value;
                    break;
                case "viewer.interests":
                    if (!value.isEmpty()) {
                        interests = Arrays.stream(value.split(";"))
                                .map(String::trim)
                                .filter(s -> !s.isEmpty())
                                .toArray(String[]::new);
                    }
                    break;
                default:
                    break;
            }
        }

        if (videoCategory == null) {
            throw new IllegalArgumentException("Missing video.category in line: " + line);
        }

        Video video = new Video(videoCategory, viewCount, commentCount);
        Viewer viewer = new Viewer(subscribed, age, gender, interests);
        return new AuctionInput(video, viewer);
    }

    public int parseWinCost(String line) {
        if (!isWinLine(line)) {
            throw new IllegalArgumentException("Not a win line: " + line);
        }

        String value = line.substring(2).trim();
        return parseIntSafe(value);
    }

    public Summary parseSummaryLine(String line) {
        if (!isSummaryLine(line)) {
            throw new IllegalArgumentException("Not a summary line: " + line);
        }

        String[] parts = line.trim().split("\\s+");
        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid summary line: " + line);
        }

        long points = parseLongSafe(parts[1]);
        long spent = parseLongSafe(parts[2]);
        return new Summary(points, spent);
    }

    private int parseIntSafe(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private long parseLongSafe(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return 0L;
        }
    }
}