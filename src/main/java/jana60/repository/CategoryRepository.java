package jana60.repository;

import org.springframework.data.repository.CrudRepository;

import jana60.model.Category;

public interface CategoryRepository extends CrudRepository<Category, Integer>
{

	public Integer countByName(String name);
	
}
