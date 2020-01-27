package rdublin.portal.calories.userSetting;

import java.util.List;

public interface UserSettingService {

    List<UserSetting> findAll();

    void deleteById(int Id);

    void deleteByUserId(int userId);

    UserSetting findById(int Id);

    UserSetting findByUserId(int userId);

    UserSetting provision(UserSettingDto userSetting);

    UserSetting create(UserSettingDto userSetting);

}
