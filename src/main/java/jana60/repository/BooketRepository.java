package jana60.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import jana60.model.Booking;

public interface BooketRepository extends CrudRepository<Booking, Integer> {
	public List<Booking> findAllByid (Integer id);

}
