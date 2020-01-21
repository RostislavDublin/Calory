import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {CrudListConfig} from '../../crud/crud-list/config/crud-list-config';
import {MealService} from '../meal.service';
import {Meal} from '../../model/meal';
import {map} from "rxjs/operators";
import {UserService} from "../../user/user.service";
import {Observable} from "rxjs";
import {CrudMode} from "../../crud/crud-mode.enum";

@Component({
  selector: 'app-meal-list',
  templateUrl: './meal-list.component.html',
  styleUrls: ['./meal-list.component.css'],
  changeDetection: ChangeDetectionStrategy.Default,
})
export class MealListComponent implements OnInit {

  meals: Meal[] = [];
  mealListConfig: CrudListConfig<Meal> = new CrudListConfig('Meal List', [
      {id: 'id', name: 'Id'},
      //{id: 'userId', name: 'UserId'},
      {id: 'userName', name: 'User Name'},
      {id: 'mealDate', name: 'Date', filter: true},
      {id: 'mealTime', name: 'Time', filter: true},
      {id: 'meal', name: 'Meal', filter: true},
      {id: 'calories', name: 'Calories', filter: true},
    ],
    this.meals
  );

  constructor(
    private mealService: MealService,
    private userService: UserService
  ) {
    console.log('Meal list in Constructor');

    this.mealListConfig.itemName = "Meal";
    this.mealListConfig.asyncDataSupplier = () => {
      return mealService.getMeals().pipe(map(mm => {
        mm.forEach(m => {
          userService.getUserById(m['userId']).toPromise().then(n => m['userName'] = n.name);
        })

        return mm;
      }));
    };

    this.mealListConfig.entryClassProvider = (entry) => {
      return {'highlight': entry.userDayExpectationExceeded}
    }

    this.mealListConfig.crudDialogConfig.fields = [
      {id: 'id', name: 'Id', placeholder: 'Id'},
      {id: 'userId', name: 'UserId', placeholder: 'UserId'},
      {id: 'mealDate', name: 'Date', placeholder: 'Date'},
      {id: 'mealTime', name: 'Date', placeholder: 'Time'},
      {id: 'meal', name: 'Meal', placeholder: 'Name'},
      {id: 'calories', name: 'Calories', placeholder: 'Calories'},
    ];

    this.mealListConfig.crudDialogConfig.submitter = (crudMode, whatToSubmit) => {
      return new Observable((observer) => {
        if (crudMode === CrudMode.Create) {
          this.mealService.save(whatToSubmit).subscribe(data => observer.next(data), error => observer.next(error));
        } else if (crudMode === CrudMode.Update) {
          this.mealService.update(whatToSubmit).subscribe(data => observer.next(data), error => observer.next(error));
        } else if (crudMode === CrudMode.Delete) {
          this.mealService.delete(whatToSubmit).subscribe(data => observer.next(data), error => observer.next(error));
        } else {
          observer.next(true);
        }
      })
    }

  }

  ngOnInit() {
    console.log('Meal list onInit');
  }

  onRefreshButtonClick() {
    this.mealListConfig.requestAsyncDataFromSupplier();
  }
}
