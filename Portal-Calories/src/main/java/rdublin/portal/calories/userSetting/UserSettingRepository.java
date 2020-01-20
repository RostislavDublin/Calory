package rdublin.portal.calories.userSetting;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserSettingRepository
        extends PagingAndSortingRepository<UserSetting, Integer> {

    UserSetting findByUserId(Integer userId);

    void deleteById(Integer Id);

}
