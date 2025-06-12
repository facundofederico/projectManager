package facundofederico.main.services;

import java.time.Duration;

public class Utils {
    public static String getFormattedDuration(Duration duration){
        long totalSeconds = duration.getSeconds();

        long days = totalSeconds / (24 * 3600);
        long hours = (totalSeconds % (24 * 3600)) / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        return String.format("%dd %dh %dm %ds", days, hours, minutes, seconds);
    }
}
