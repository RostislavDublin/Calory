import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppRoutingModule} from './app-routing.module';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {AppComponent} from './app.component';
import {UserListComponent} from './user/user-list/user-list.component';
import {NavbarComponent} from './navbar/navbar.component';
import {LoginComponent} from './login/login.component';
import {AuthInterceptorService} from './login/auth-interceptor.service';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
/* Angular material */
import {MaterialModule} from './material.module';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {UserFormComponent} from './user/user-form/user-form.component';
import {FlexLayoutModule} from '@angular/flex-layout';
import { MealListComponent } from './meals/meal-list/meal-list.component';
import {CrudModule} from './crud/crud.module';


@NgModule({
  declarations: [
    AppComponent,
    UserListComponent,
    NavbarComponent,
    LoginComponent,
    UserFormComponent,
    MealListComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    MaterialModule,
    FlexLayoutModule,
    CrudModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptorService,
      multi: true
    }
  ],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  entryComponents: [UserFormComponent]
})
export class AppModule {
}
