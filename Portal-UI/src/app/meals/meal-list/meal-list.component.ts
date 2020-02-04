import {ChangeDetectionStrategy, Component} from '@angular/core';
import {CrudListConfig} from '../../crud/crud-list/config/crud-list-config';
import {MealService} from '../meal.service';
import {Meal} from '../../model/meal';
import {map} from "rxjs/operators";
import {UserService} from "../../user/user.service";
import {Validators} from "@angular/forms";
import {CrudDialogFieldConfig} from "../../crud/crud-dialog/config/crud-dialog-field-config";
import {CrudListColumnConfig} from "../../crud/crud-list/config/crud-list-column-config";
import {formatDate} from "@angular/common";
import {CrudListFilterConfig} from "../../crud/crud-list/config/crud-list-filter-config";
import {AuthenticationService} from "../../login/authentication.service";
import {User} from "../../model/user";

@Component({
  selector: 'app-meal-list',
  templateUrl: './meal-list.component.html',
  styleUrls: ['./meal-list.component.css'],
  changeDetection: ChangeDetectionStrategy.Default,
})
export class MealListComponent {

  dateFormatter = (v) => formatDate(v, 'MM/dd/yyyy', 'en-US');

  meals: Meal[] = [];
  usersFilterOptions = {};

  mealListConfig: CrudListConfig<Meal> = new CrudListConfig({
      listTitle: 'Meal List',
      itemTitle: 'Meal',
      columns: [
        new CrudListColumnConfig({id: 'id', name: 'Id'}),
        //{id: 'userId', name: 'UserId'},
        new CrudListColumnConfig({id: 'userName', name: 'User Name'}),
        new CrudListColumnConfig({id: 'mealDate', name: 'Date', formatter: this.dateFormatter}),
        new CrudListColumnConfig({id: 'mealTime', name: 'Time'}),
        new CrudListColumnConfig({id: 'meal', name: 'Meal'}),
        new CrudListColumnConfig({id: 'calories', name: 'Calories'}),
      ],
      filters: [
        new CrudListFilterConfig({
            id: 'userId', name: 'User', inputType: 'select',
            initialValue: this.authenticationService.getLoggedInUserId(),
            optionsData: this.usersFilterOptions,
          }
        ),
        new CrudListFilterConfig({
          id: 'dateFrom', name: 'Start Date',
          inputType: 'date',
          initialValue: '2020-01-01'
        }),
        new CrudListFilterConfig({id: 'dateTo', name: 'End Date', inputType: 'date'}),
        new CrudListFilterConfig({id: 'timeFrom', name: 'Start Time', inputType: 'time'}),
        new CrudListFilterConfig({id: 'timeTo', name: 'End Time', inputType: 'time'}),
      ],
      data: this.meals
    }
  );


  constructor(
    private authenticationService: AuthenticationService,
    private mealService: MealService,
    private userService: UserService
  ) {
    console.log('Meal list in Constructor');

    //populate User filter options
    if (authenticationService.isLoggedInHasAnyAuthority(AuthenticationService.MEAL_ALL_CRUD_PRIVILEGE)) {
      userService.getUsersAccessibleToLoggedInUser().toPromise().then(users => {
        users.forEach(u => {
          this.usersFilterOptions[u.id] = u.name
        });
      })
    } else {
      this.usersFilterOptions[authenticationService.getLoggedInUserId()] = authenticationService.getLoggedInUserName()
    }

    this.mealListConfig.asyncDataSupplier = (filterValues) => {
      return mealService.getMeals(filterValues).pipe(map(mm => {
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
      //Meal record Id
      new CrudDialogFieldConfig({id: 'id', placeholder: 'Id', disabled: true, inputClass: () => 'invisible'}),
      //User Id
      new CrudDialogFieldConfig({
        id: 'userId', placeholder: 'UserId', disabled: true,
        defaultValue: () => {
          //take currently chosen in the User Filter
          return this.mealListConfig.getFilterValue("userId")
        }
      }),
      //User Name
      new CrudDialogFieldConfig({
        id: 'userName', placeholder: 'User', disabled: true,
        defaultValue: () => {
          //take currently chosen in the User Filter
          const userId = this.mealListConfig.getFilterValue("userId");
          //... and show his name (take it from the User Filter options list
          return userId
            ? this.mealListConfig.getFilterById('userId').optionsData[userId]
            : authenticationService.getLoggedInUserName();
        }
      }),
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
          {
            validator: Validators.pattern("^[0-9]*$"),
            errors: [{errorName: "pattern", errorMessage: "Non-negative number required"}]
          },
        ]
      }),
    ];

    dialogConfig.createHandler = whatToSubmit => this.mealService.save(whatToSubmit);
    dialogConfig.updateHandler = whatToSubmit => this.mealService.update(whatToSubmit);
    dialogConfig.deleteHandler = whatToSubmit => this.mealService.delete(whatToSubmit);
  }
}
