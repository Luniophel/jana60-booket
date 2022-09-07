package jana60.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import jana60.model.Events;

public interface EventsRepository extends CrudRepository<Events, Integer>{
	public List<Events> findByNameContainingIgnoreCase(String queryName);
}
