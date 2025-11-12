package um.playlist.mapper;

import um.playlist.model.Video;
import um.playlist.dto.VideoDTO;

public class VideoMapper {

    public static VideoDTO toDto(Video v) {
        if (v == null) return null;
        VideoDTO d = new VideoDTO();
        d.setId(v.getId());
        d.setTitle(v.getTitle());
        d.setUrl(v.getUrl());
        d.setLikes(v.getLikes());
        d.setFavorite(v.isFavorite());
        return d;
    }

    public static Video toEntity(VideoDTO d) {
        if (d == null) return null;
        Video v = new Video();
        v.setId(d.getId());
        v.setTitle(d.getTitle());
        v.setUrl(d.getUrl());
        if (d.getLikes() != null) v.setLikes(d.getLikes());
        if (d.getFavorite() != null) v.setFavorite(d.getFavorite());
        return v;
    }

    public static void updateEntity(VideoDTO d, Video v) {
        if (d.getTitle() != null) v.setTitle(d.getTitle());
        if (d.getUrl() != null) v.setUrl(d.getUrl());
        if (d.getLikes() != null) v.setLikes(d.getLikes());
        if (d.getFavorite() != null) v.setFavorite(d.getFavorite());
    }
}

