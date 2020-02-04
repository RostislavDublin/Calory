import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Meal} from '../model/meal';
import {AuthenticationService} from '../login/authentication.service';
import {environment} from '../../environments/environment';
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class MealService {
  private mealsUrl: string = environment.apiGatewayUrl + '/calories/meals';

  constructor(
    private http: HttpClient,
    private authenticationService: AuthenticationService) {
  }

  getMeals(filterValues?: Map<string, any>): Observable<Meal[]> {
    let params = new HttpParams();
    if (filterValues) {
      if (filterValues.get('userId'))
        params = params.set('userId', filterValues.get('userId'));
      if (filterValues.get('dateFrom')) {
        params = params.set('dateFrom', filterValues.get('dateFrom'));
      }
      if (filterValues.get('dateTo')) {
        params = params.set('dateTo', filterValues.get('dateTo'));
      }
      if (filterValues.get('timeFrom')) {
        params = params.set('timeFrom', filterValues.get('timeFrom'));
      }
      if (filterValues.get('timeTo')) {
        params = params.set('timeTo', filterValues.get('timeTo'));
      }
    }
    return this.http.get<Meal[]>(this.mealsUrl, {params: params});
  }

  getCurrentUserMeals(): Observable<Meal[]> {
    const currentUserId = this.authenticationService.getLoggedInUserId();
    const params = new HttpParams().set('userId', currentUserId.toString());
    return this.http.get<Meal[]>(this.mealsUrl, {params});
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
