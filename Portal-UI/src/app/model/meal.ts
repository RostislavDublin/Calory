import {Time} from "@angular/common";

export class Meal {
  id: number;
  userId: number;
  mealDate: Date;
  mealTime: Time;
  meal: string;
  calories: number;
  createdDate: Date;
  modifiedDate: Date;
  userDayExpectationExceeded: boolean;
}
