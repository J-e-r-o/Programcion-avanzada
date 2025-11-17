package um.playlist.util;

public class UrlHelper {
    public static String toEmbedUrl(String url) {
        if (url == null) return null;
        if (url.contains("youtube.com/watch?v=")) {
            return url.replace("watch?v=", "embed/");
        }
        if (url.contains("youtu.be/")) {
            return url.replace("youtu.be/", "www.youtube.com/embed/");
        }
        return url;
    }
}
