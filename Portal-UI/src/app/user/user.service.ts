import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {EMPTY, Observable} from 'rxjs';
import {User} from '../model/user';
import {environment} from '../../environments/environment';
import {map, publishReplay, refCount} from "rxjs/operators";
import {AuthenticationService} from "../login/authentication.service";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private usersCache: Map<number, CachedUser> = new Map<number, CachedUser>();

  private usersUrl: string;

  constructor(
    private http: HttpClient,
    private authenticationService: AuthenticationService
  ) {
    this.usersUrl = environment.apiGatewayUrl + '/auth/users';
  }

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.usersUrl);
  }

  public save(user: User) {
    return this.http.post<User>(this.usersUrl, user);
  }

  public update(user: User) {
    const url: string = this.usersUrl.concat('/').concat(user.id);
    return this.http.put<User>(url, user);
  }

  public delete(user: User) {
    const url: string = this.usersUrl.concat('/').concat(user.id);
    return this.http.delete(url);
  }

  public getUserById(userId: number, cacheTtl: number = 60000): Observable<User> {

    var cachedUser: CachedUser = this.usersCache.get(userId);

    if (!cachedUser || (new Date().getTime() - cachedUser.cachedDate.getTime()) > cacheTtl) {
      if (!cachedUser) {
        cachedUser = new CachedUser();
        this.usersCache.set(userId, cachedUser);
      }
      cachedUser.user = this.http.get<User>(this.usersUrl + "/" + userId).pipe(publishReplay(1), refCount());
      cachedUser.cachedDate = new Date();
    }

    return cachedUser.user;
  }

  getUsersAccessibleToLoggedInUser(): Observable<User[]> {
    if (!this.authenticationService.isLoggedIn()) {
      return EMPTY;
    } else if (this.authenticationService.getLoggedInUserAuthorities().indexOf(AuthenticationService.USER_ALL_CRUD_PRIVILEGE) > -1) {
      return this.getUsers();
    } else if (this.authenticationService.getLoggedInUserAuthorities().indexOf(AuthenticationService.USER_OWN_CRUD_PRIVILEGE) > -1) {
      return this.getUserById(this.authenticationService.getLoggedInUserId()).pipe(map(u => [u]));
    }
    return EMPTY;
  }
}

class CachedUser {
  cachedDate: Date;
  user: Observable<User>;
}
