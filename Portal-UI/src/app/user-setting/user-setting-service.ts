import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {AuthenticationService} from '../login/authentication.service';
import {map} from 'rxjs/operators';
import {UserSetting} from "./user-setting";
import {EnvService} from "../config/env/env.service";

@Injectable({
  providedIn: 'root'
})
export class UserSettingService {
  private userSettingUrl: string = this.env.apiGatewayUrl + '/calories/userSettings';

  constructor(
    private env: EnvService,
    private http: HttpClient,
    private authenticationService: AuthenticationService) {
  }

  getUserSettings(userId: number): Observable<UserSetting> {
    const url: string = this.userSettingUrl.concat('/').concat(userId.toString());
    return this.http.get<UserSetting>(url)
      .pipe(map(w => {
        const userSetting: UserSetting = w;
        return userSetting;
      }));
  }

  getCurrentUserSettings(): Observable<UserSetting> {
    const currentUserId = this.authenticationService.getLoggedInUserId();
    return this.getUserSettings(currentUserId)
  }

  public save(userSetting: UserSetting) {
    return this.http.post<UserSetting>(this.userSettingUrl, userSetting);
  }

  public update(userSetting: UserSetting) {
    const url: string = this.userSettingUrl;
    return this.http.put<UserSetting>(url, userSetting);
  }

  public delete(id: string) {
    const url: string = this.userSettingUrl.concat('/', id);
    return this.http.delete(url);
  }
}
