package jana60.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import jana60.model.Events;
import jana60.model.Image;

public interface ImageRepository extends CrudRepository<Image, Integer> {
	public List<Image> findByimageEvent(Events event);
	
	public List<Image> findByPosterAndImageEvent(boolean value, Events event);
}
