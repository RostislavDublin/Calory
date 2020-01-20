package rdublin.portal.calories.userSetting;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("userSettingService")
@Transactional
public class UserSettingServiceImpl implements UserSettingService {

    @Autowired
    UserSettingRepository userSettingRepository;

    @Override
    public List<UserSetting> findAll() {

        List<UserSetting> list = new ArrayList<>();
        userSettingRepository.findAll().iterator().forEachRemaining(list::add);
        return list;

    }

    @Override
    public void deleteById(int Id) {
        userSettingRepository.deleteById(Id);
    }

    @Override
    public void deleteByUserId(int userId) {
        UserSetting userSetting = userSettingRepository.findByUserId(userId);
        if (userSetting == null) {
            throw new EmptyResultDataAccessException(
                    String.format("No UserSetting entity with User ID %s exists!", userId), 1);
        }

        userSettingRepository.delete(userSetting);
    }

    @Override
    public UserSetting findById(int Id) {
        return userSettingRepository.findById(Id).orElse(null);
    }

    @Override
    public UserSetting findByUserId(int userId) {
        return userSettingRepository.findByUserId(userId);
    }

    @Override
    public UserSetting provision(UserSettingDto userSettingDto) {
        UserSetting userSetting = findByUserId(userSettingDto.getUserId());

        if (userSetting == null) {
            userSetting = UserSetting.builder().caloriesExpected(0).build();
        }

        BeanUtils.copyProperties(userSettingDto, userSetting, "id");

        userSetting = userSettingRepository.save(userSetting);

        return userSetting;
    }

    @Override
    public UserSetting create(UserSettingDto userSettingDto) {
        UserSetting newUserSetting = new UserSetting();
        BeanUtils.copyProperties(userSettingDto, newUserSetting, "id");
        return userSettingRepository.save(newUserSetting);
    }
}
