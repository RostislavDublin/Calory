import {ChangeDetectionStrategy, Component} from '@angular/core';
import {CrudListConfig} from '../../crud/crud-list/config/crud-list-config';
import {MealService} from '../meal.service';
import {Meal} from '../../model/meal';
import {map} from "rxjs/operators";
import {UserService} from "../../user/user.service";
import {Validators} from "@angular/forms";
import {CrudDialogFieldConfig} from "../../crud/crud-dialog/config/crud-dialog-field-config";

@Component({
  selector: 'app-meal-list',
  templateUrl: './meal-list.component.html',
  styleUrls: ['./meal-list.component.css'],
  changeDetection: ChangeDetectionStrategy.Default,
})
export class MealListComponent {

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

    const dialogConfig = this.mealListConfig.crudDialogConfig;

    dialogConfig.fields = [
      new CrudDialogFieldConfig({id: 'id', placeholder: 'Id', disabled: true}),
      new CrudDialogFieldConfig({id: 'userId', placeholder: 'UserId', disabled: true}),
      new CrudDialogFieldConfig({
        id: 'mealDate', placeholder: 'Date', type: 'date',
        validation: [
          {validator: Validators.required, errors: [{errorName: "required", errorMessage: "Required"}]}
        ]
      }),
      new CrudDialogFieldConfig({
        id: 'mealTime', placeholder: 'Time', type: 'time',
        validation: [
          {validator: Validators.required, errors: [{errorName: "required", errorMessage: "Required"}]}
        ]
      }),
      new CrudDialogFieldConfig({
        id: 'meal', placeholder: 'Name',
        validation: [
          {validator: Validators.required, errors: [{errorName: "required", errorMessage: "Required"}]}
        ]
      }),
      new CrudDialogFieldConfig({
        id: 'calories', placeholder: 'Calories', type: 'number',
        validation: [
          {validator: Validators.required, errors: [{errorName: "required", errorMessage: "Required"}]},
          {validator: Validators.pattern("^[0-9]*$"), errors: [{errorName: "pattern", errorMessage: "Non-negative number required"}]},
        ]
      }),
    ];

    dialogConfig.createHandler = whatToSubmit => this.mealService.save(whatToSubmit);
    dialogConfig.updateHandler = whatToSubmit => this.mealService.update(whatToSubmit);
    dialogConfig.deleteHandler = whatToSubmit => this.mealService.delete(whatToSubmit);
  }

  onRefreshButtonClick() {
    this.mealListConfig.requestAsyncDataFromSupplier();
  }
}
