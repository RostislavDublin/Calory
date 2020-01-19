package rdublin.portal.calories.meal;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service("mealService")
public class MealServiceImpl implements MealService {

    @Autowired
    MealRepository mealRepository;

    @Override
    public List<Meal> findAllByUserAndMealPeriod(
            Integer userId, LocalDate dateFrom, LocalDate dateTo, LocalTime timeFrom, LocalTime timeTo) {
        return mealRepository.findAllByUserAndMealPeriod(userId, dateFrom, dateTo, timeFrom, timeTo);
    }

    @Override
    public void delete(int id) {
        mealRepository.deleteById(id);
    }

    @Override
    public Meal findById(int id) {
        return mealRepository.findById(id).orElse(null);
    }

    @Override
    public Meal update(MealDto mealDto) {
        Meal meal = findById(mealDto.getId());
        if (meal != null) {
            BeanUtils.copyProperties(mealDto, meal);
        }
        meal = mealRepository.save(meal);
        return meal;
    }

    @Override
    public Meal save(MealDto mealDto) {
        Meal newMeal = new Meal();
        BeanUtils.copyProperties(mealDto, newMeal, "id");
        return mealRepository.save(newMeal);
    }
}
