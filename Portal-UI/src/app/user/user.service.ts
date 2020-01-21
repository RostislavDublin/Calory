import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {User} from '../model/user';
import {environment} from '../../environments/environment';
import {publishReplay, refCount} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private usersCache: Map<number, CachedUser> = new Map<number, CachedUser>();

  private usersUrl: string;

  constructor(private http: HttpClient) {
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
}

class CachedUser {
  cachedDate: Date;
  user: Observable<User>;
}
