package jana60.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import jana60.model.Events;
import jana60.model.Location;

public interface EventsRepository extends CrudRepository<Events, Integer>
{
	public List<Events> findAllByEventLocation (Location id); 
	public List<Events> findByNameContainingIgnoreCase(String queryName);
}
