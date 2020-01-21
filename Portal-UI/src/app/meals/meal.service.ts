import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Meal} from '../model/meal';
import {AuthenticationService} from '../login/authentication.service';
import {environment} from '../../environments/environment';
import {map} from 'rxjs/operators';
import {User} from "../model/user";

@Injectable({
  providedIn: 'root'
})
export class MealService {
  private mealsUrl: string = environment.apiGatewayUrl + '/calories/meals';

  constructor(
    private http: HttpClient,
    private authenticationService: AuthenticationService) {
  }

  getMeals(): Observable<Meal[]> {
    return this.http.get<any>(this.mealsUrl)
      .pipe(map(w => {
        const meals: Meal[] = w;
        return meals;
      }));
  }
  getCurrentUserMeals(): Observable<Meal[]> {
    const currentUserId = this.authenticationService.getLoggedInUserId();
    const params = new HttpParams().set('userId', currentUserId.toString());
    return this.http.get<any>(this.mealsUrl, {params})
      .pipe(map(w => {
        const meals: Meal[] = w;
        return meals;
      }));
  }

  public save(meal: Meal) {
    return this.http.post<Meal>(this.mealsUrl, meal);
  }

  public update(meal: Meal) {
    const url: string = this.mealsUrl.concat('/').concat(meal.id.toString());
    return this.http.put<Meal>(url, meal);
  }

  public delete(meal: Meal) {
    const url: string = this.mealsUrl.concat('/').concat(meal.id.toString());
    return this.http.delete(url);
  }

}
