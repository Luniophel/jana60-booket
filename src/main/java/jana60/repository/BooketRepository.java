package jana60.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import jana60.model.Booking;
import jana60.model.Events;

public interface BooketRepository extends CrudRepository<Booking, Integer> {
	public List<Booking> findAllById (Integer id);

}
