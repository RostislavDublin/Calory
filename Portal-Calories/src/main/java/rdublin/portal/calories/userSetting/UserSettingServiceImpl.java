package rdublin.portal.calories.userSetting;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rdublin.portal.calories.meal.MealService;

import java.util.ArrayList;
import java.util.List;

@Service("userSettingService")
@Transactional
public class UserSettingServiceImpl implements UserSettingService {

    @Autowired
    UserSettingRepository userSettingRepository;

    @Autowired
    MealService mealService;

    @Override
    public List<UserSetting> findAll() {

        List<UserSetting> list = new ArrayList<>();
        userSettingRepository.findAll().iterator().forEachRemaining(list::add);
        return list;

    }

    @Override
    @Transactional
    public void deleteById(int id) {
        UserSetting userSetting = userSettingRepository.findById(id).orElse(null);
        if (userSetting == null) {
            throw new EmptyResultDataAccessException(
                    String.format("No UserSetting entity with ID %s exists!", id), 1);
        }

        userSettingRepository.deleteById(id);

        mealService.calcUserAllDaysExpectationsExceeded(userSetting.getUserId());
    }

    @Override
    @Transactional
    public void deleteByUserId(int userId) {
        UserSetting userSetting = userSettingRepository.findByUserId(userId);
        if (userSetting == null) {
            throw new EmptyResultDataAccessException(
                    String.format("No UserSetting entity with User ID %s exists!", userId), 1);
        }

        userSettingRepository.delete(userSetting);

        mealService.calcUserAllDaysExpectationsExceeded(userId);
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
    @Transactional
    public UserSetting provision(UserSettingDto userSettingDto) {
        UserSetting userSetting = findByUserId(userSettingDto.getUserId());

        if (userSetting == null) {
            userSetting = UserSetting.builder().caloriesExpected(0).build();
        }

        int oldCaloriesExpected = userSetting.getCaloriesExpected();

        BeanUtils.copyProperties(userSettingDto, userSetting, "id");

        userSetting = userSettingRepository.save(userSetting);

        if (oldCaloriesExpected != userSetting.getCaloriesExpected()) {
            mealService.calcUserAllDaysExpectationsExceeded(userSetting.getUserId());
        }

        return userSetting;
    }

    @Override
    @Transactional
    public UserSetting create(UserSettingDto userSettingDto) {
        UserSetting newUserSetting = new UserSetting();
        BeanUtils.copyProperties(userSettingDto, newUserSetting, "id");
        newUserSetting = userSettingRepository.save(newUserSetting);

        if (newUserSetting.getCaloriesExpected() != 0) {
            mealService.calcUserAllDaysExpectationsExceeded(newUserSetting.getUserId());
        }

        return newUserSetting;
    }
}
