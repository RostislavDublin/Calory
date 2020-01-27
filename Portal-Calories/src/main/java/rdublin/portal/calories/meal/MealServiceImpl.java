package rdublin.portal.calories.meal;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rdublin.portal.calories.userSetting.UserSettingRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("mealService")
public class MealServiceImpl implements MealService {

    @Autowired
    MealRepository mealRepository;

    @Autowired
    UserSettingRepository userSettingRepository;

    @Override
    public List<Meal> findAllByUserAndMealPeriod(
            Integer userId, LocalDate dateFrom, LocalDate dateTo, LocalTime timeFrom, LocalTime timeTo) {
        return mealRepository.findAllByUserAndMealPeriod(userId, dateFrom, dateTo, timeFrom, timeTo);
    }

    @Override
    @Transactional
    public void delete(int id) {
        Meal meal = findById(id);
        if (meal == null) {
            throw new EmptyResultDataAccessException(
                    String.format("No Meal entity with id %s exists!", id), 1);
        }

        mealRepository.deleteById(id);

        calcUserDayExpectationsExceeded(meal.getUserId(), meal.getMealDate());
    }

    @Override
    public Meal findById(int id) {
        return mealRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Meal update(MealDto mealDto) {
        Meal meal = findById(mealDto.getId());
        if (meal == null) {
            throw new EmptyResultDataAccessException(
                    String.format("No Meal entity with id %s exists!", mealDto.getId()), 1);
        }

        int oldUserId = meal.getUserId();
        LocalDate oldMealDate = meal.getMealDate();

        BeanUtils.copyProperties(mealDto, meal);

        meal = mealRepository.save(meal);

        calcUsersDaysExpectationsExceeded(oldUserId, oldMealDate, meal.getUserId(), meal.getMealDate());

        return meal;
    }

    @Override
    @Transactional
    public Meal create(MealDto mealDto) {
        Meal newMeal = new Meal();
        BeanUtils.copyProperties(mealDto, newMeal, "id");

        newMeal = mealRepository.save(newMeal);
        calcUserDayExpectationsExceeded(newMeal.getUserId(), newMeal.getMealDate());

        return newMeal;
    }

    @Override
    @Transactional
    public void calcUsersDaysExpectationsExceeded(
            int oldUserId, LocalDate oldMealDate,
            int newUserId, LocalDate newMealDate
    ) {
        calcUserDayExpectationsExceeded(oldUserId, oldMealDate);
        if (oldMealDate != newMealDate) {
            calcUserDayExpectationsExceeded(oldUserId, newMealDate);
        }

        if (oldUserId != newUserId) {
            calcUserDayExpectationsExceeded(newUserId, oldMealDate);

            if (oldMealDate != newMealDate) {
                calcUserDayExpectationsExceeded(newUserId, newMealDate);
            }
        }
    }

    @Override
    @Transactional
    public void calcUserDayExpectationsExceeded(int userId, LocalDate mealDate) {

        int estimation =
                Optional.ofNullable(userSettingRepository.findByUserId(userId))
                        .map(us -> us.getCaloriesExpected()).orElse(0);

        List<Meal> userDayMeals = mealRepository.findAllByUserAndMealPeriod(userId, mealDate, mealDate, null, null);

        int consumption = userDayMeals.stream().collect(Collectors.summingInt(m -> m.getCalories()));

        boolean expectationExceeded = consumption > estimation;

        userDayMeals.stream().forEach(m -> {
            m.setUserDayExpectationExceeded(expectationExceeded);
            mealRepository.save(m);
        });

    }

    @Override
    @Transactional
    public void calcUserAllDaysExpectationsExceeded(int userId) {
        List<LocalDate> userMealDays = mealRepository.findAllUserMealDays(userId);

        userMealDays.stream().forEach(d -> {
            calcUserDayExpectationsExceeded(userId, d);
        });

    }
}
