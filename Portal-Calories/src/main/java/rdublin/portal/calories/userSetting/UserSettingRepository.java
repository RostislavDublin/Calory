package rdublin.portal.calories.userSetting;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import rdublin.portal.calories.meal.Meal;

import java.util.List;

@RepositoryRestResource(path = "userSettings", collectionResourceRel = "userSettings")
public interface UserSettingRepository extends PagingAndSortingRepository<UserSetting, Integer> {

    @RestResource
    List<Meal> findAllByUserId(Integer userId);

}
