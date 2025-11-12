package um.playlist.controller;

import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import um.playlist.dto.VideoDTO;
import um.playlist.mapper.VideoMapper;
import um.playlist.model.Video;
import um.playlist.service.VideoService;
import um.playlist.exception.ResourceNotFoundException;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/videos")
public class VideoRestController {

    private final VideoService service;
    public VideoRestController(VideoService service){ this.service = service; }

    @GetMapping
    public Page<VideoDTO> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String sort,
            @RequestParam(required = false) String q) {

        Sort s = Sort.by(Sort.Order.by(sort.split(",")[0]).with(Sort.Direction.fromString(sort.split(",")[1])));
        Pageable p = PageRequest.of(page, size, s);

        Page<Video> result;
        if (q != null && !q.isBlank()) {
            result = service.searchByTitle(q, p);
        } else {
            result = service.findAll(p);
        }
        return result.map(VideoMapper::toDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoDTO> get(@PathVariable Long id){
        return service.find(id).map(VideoMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Video no encontrado: " + id));
    }

    @PostMapping
    public ResponseEntity<VideoDTO> create(@Valid @RequestBody VideoDTO dto){
        Video v = VideoMapper.toEntity(dto);
        v = service.save(v);
        return ResponseEntity.ok(VideoMapper.toDto(v));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<VideoDTO> partialUpdate(@PathVariable Long id, @RequestBody VideoDTO dto){
        Video v = service.find(id).orElseThrow(() -> new ResourceNotFoundException("No encontrado"));
        VideoMapper.updateEntity(dto, v);
        v = service.save(v);
        return ResponseEntity.ok(VideoMapper.toDto(v));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        if (service.find(id).isEmpty()) throw new ResourceNotFoundException("No encontrado");
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<VideoDTO> like(@PathVariable Long id){
        Video v = service.find(id).orElseThrow(() -> new ResourceNotFoundException("No encontrado"));
        v.setLikes(v.getLikes()+1);
        v = service.save(v);
        return ResponseEntity.ok(VideoMapper.toDto(v));
    }

    @PostMapping("/{id}/favorite")
    public ResponseEntity<VideoDTO> toggleFavorite(@PathVariable Long id){
        Video v = service.find(id).orElseThrow(() -> new ResourceNotFoundException("No encontrado"));
        v.setFavorite(!v.isFavorite());
        v = service.save(v);
        return ResponseEntity.ok(VideoMapper.toDto(v));
    }
}
