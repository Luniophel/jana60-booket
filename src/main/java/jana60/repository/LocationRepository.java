package jana60.repository;

import org.springframework.data.repository.CrudRepository;

import jana60.model.Location;

public interface LocationRepository extends CrudRepository<Location, Integer>{

	public Integer countByName(String name);
}
