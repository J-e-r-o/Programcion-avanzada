package um.playlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import um.playlist.model.Video;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



public interface VideoRepository extends JpaRepository<Video, Long> {

    Page<Video> findByTitleContainingIgnoreCase(String title, Pageable pageable);

}
