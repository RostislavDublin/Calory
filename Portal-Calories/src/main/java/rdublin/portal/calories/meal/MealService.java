package rdublin.portal.calories.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MealService {

    @Autowired
    MealRepository mealRepository;

}
