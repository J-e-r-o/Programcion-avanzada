package um.playlist.service;

import org.springframework.stereotype.Service;
import um.playlist.repository.VideoRepository;
import um.playlist.model.Video;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.*;

@Service
public class VideoService {
    private final VideoRepository repo;
    public VideoService(VideoRepository repo){ this.repo = repo; }

    public List<Video> all(){ return repo.findAll(); }
    public Video save(Video v){ return repo.save(v); }
    public void delete(Long id){ repo.deleteById(id); }
    public Optional<Video> find(Long id){ return repo.findById(id); }

    // paginación
    public Page<Video> findAll(Pageable p) { return repo.findAll(p); }

    // búsqueda simple por título (requiere añadir método en repo)
    public Page<Video> searchByTitle(String q, Pageable p) {
        return repo.findByTitleContainingIgnoreCase(q, p);

    }

}
