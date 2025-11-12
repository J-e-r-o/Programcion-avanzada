package um.playlist.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import um.playlist.model.Video;
import um.playlist.service.VideoService;

@Component
public class DataLoader implements CommandLineRunner {
    private final VideoService service;
    public DataLoader(VideoService service){ this.service = service; }

    @Override
    public void run(String... args) throws Exception {
        if (service.all().isEmpty()) {
            service.save(make("Introducción a Spring Boot", "https://www.youtube.com/embed/XXXXX"));
            service.save(make("Curso Java - Clase 1", "https://www.youtube.com/embed/YYYYY"));
            service.save(make("Arquitectura REST", "https://www.youtube.com/embed/ZZZZZ"));
        }
    }
    private Video make(String t, String u){
        Video v = new Video();
        v.setTitle(t); v.setUrl(u); v.setLikes(0); v.setFavorite(false);
        return v;
    }
}
