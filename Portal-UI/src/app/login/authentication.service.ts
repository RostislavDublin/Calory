import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {BehaviorSubject, Observable} from 'rxjs';
import * as jwt_decode from 'jwt-decode';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService implements CanActivate {

  public static TOKEN_GET_PATH = environment.apiGatewayUrl + '/auth/oauth/token';
  public static USER_ID_KEY = 'authenticatedUserId';
  public static USER_NAME_KEY = 'authenticatedUserName';
  public static USER_AUTHORITIES_KEY = 'authenticatedUserAuthorities';
  public static JWT_TOKEN_KEY = 'authenticatedUserToken';

  // https://netbasal.com/angular-2-persist-your-login-status-with-behaviorsubject-45da9ec43243
  isLoginSubject = new BehaviorSubject<boolean>(this.isUserLoggedIn());

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    console.log('New AuthenticationService: ' + new Date());
  }

  login(loginPayload): Observable<any> {

    return new Observable((observer) => {
      this.removeAuthToken();

      const params = new HttpParams()
        .set('username', loginPayload.username)
        .set('password', loginPayload.password)
        .set('grant_type', 'password')
        .set('client_id', 'browser');

      const headers = new HttpHeaders({
        'Content-type': 'application/x-www-form-urlencoded; charset=utf-8',
        Authorization: 'Basic ' + btoa('browser:secret')
      });

      const options = {headers};
      this.http.post(AuthenticationService.TOKEN_GET_PATH, params.toString(), options)
        .subscribe(data => {
          this.setAuthToken(data);
          observer.next(data);
        }, error => {
          const errorToken = {
            status: 401,
            message: 'Authentication error'
          }
          observer.next(errorToken);
        });
      return () => {
      };
    });
  }

  logout() {
    this.removeAuthToken();
    this.router.navigate(['/']);
  }

  setAuthToken(token) {

    const decoded = jwt_decode(token.access_token);

    sessionStorage.setItem(AuthenticationService.JWT_TOKEN_KEY, token.access_token);
    sessionStorage.setItem(AuthenticationService.USER_NAME_KEY, decoded.user_name);
    sessionStorage.setItem(AuthenticationService.USER_AUTHORITIES_KEY, JSON.stringify(decoded.authorities));
    sessionStorage.setItem(AuthenticationService.USER_ID_KEY, decoded.user_id);
    this.isLoginSubject.next(true);
  }

  removeAuthToken() {
    sessionStorage.removeItem(AuthenticationService.USER_NAME_KEY);
    sessionStorage.removeItem(AuthenticationService.USER_AUTHORITIES_KEY);
    sessionStorage.removeItem(AuthenticationService.USER_ID_KEY);
    sessionStorage.removeItem(AuthenticationService.JWT_TOKEN_KEY);
    this.isLoginSubject.next(false);
  }

  isUserLoggedIn() {
    return (sessionStorage.getItem(AuthenticationService.JWT_TOKEN_KEY) !== null);
  }

  getLoggedInUserName(): string {
    const user = sessionStorage.getItem(AuthenticationService.USER_NAME_KEY);
    if (user === null) {
      return '';
    }
    return user;
  }

  getLoggedInUserAuthorities(): string[] {
    const authorities = sessionStorage.getItem(AuthenticationService.USER_AUTHORITIES_KEY);
    if (authorities === null) {
      return [];
    }
    return JSON.parse(authorities);
  }

  getLoggedInUserId(): number {
    const userId = sessionStorage.getItem(AuthenticationService.USER_ID_KEY);
    if (userId === null) {
      return 0;
    }
    return Number(userId);
  }

  isLoggedIn(): Observable<boolean> {
    return this.isLoginSubject.asObservable();
  }

  /**
   * https://stackoverflow.com/questions/34331478/angular-redirect-to-login-page
   */
  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    if (this.isUserLoggedIn()) {
      return true;
    }
    // not logged in so redirect to login page with the return url
    this.router.navigate(['/login'], {queryParams: {returnUrl: state.url}});
    return false;
  }
}
