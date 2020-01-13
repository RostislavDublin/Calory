package rdublin.portal.calories.meal;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(path = "links", collectionResourceRel = "links")
public interface MealRepository extends PagingAndSortingRepository<Meal, Integer> {

    @RestResource
    List<Meal> findAllByUserId(Integer userId);

}
