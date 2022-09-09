package jana60.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import jana60.model.Events;
import jana60.model.Image;

public interface ImageRepository extends CrudRepository<Image, Integer> {
	public List<Image> findByimageEvent(Events event);
	
	public List<Image> findByPosterAndImageEvent(boolean value, Events event);

	
	/*	Counter per immagini settate come Poster
	 *	Da utilizzare per verificare la presenza di più immagini poster==true, poiché normalmente
	 *	è prevista la presenza di un unico elemento con tale valore.
	 */
	public int countByPosterTrue();
	
//	 @Modifying
//	 @Query("UPDATE Image img SET img.poster = :name, u.surname = :surname  WHERE u.id = :userId")
//	 void updateImagePosterFalse(@Param("userId") int userId, @Param("name") String name, @Param("surname") String surname);
}
