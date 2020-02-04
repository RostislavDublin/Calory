import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AuthenticationService} from './authentication.service';
import {tap} from 'rxjs/operators';
import {Router} from '@angular/router';
import {MatDialog} from '@angular/material/dialog';

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptorService implements HttpInterceptor {

  constructor(
    private router: Router,
    private dialogRef: MatDialog
  ) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = sessionStorage.getItem(AuthenticationService.JWT_TOKEN_KEY);
    if (token) {
      request = request.clone({
        setHeaders: {
          Authorization: 'Bearer ' + token
        }
      });
    }
    return next.handle(request).pipe(tap(() => {
      },
      (err: any) => {
        if (err instanceof HttpErrorResponse) {
          // 401 Unauthorized
          if (err.status === 401) {
            // close any open matDialogues (cause they block redirects)
            this.dialogRef.closeAll();
            // navigate to the login page
            this.router.navigate(['login'], {queryParams: {returnUrl: this.router.url}});
            return;
          }
          // any other HttpErrorResponse error
          return;
        }
        //any other error
      }));
  }
}
