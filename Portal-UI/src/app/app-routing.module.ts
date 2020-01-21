import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {UserListComponent} from './user/user-list/user-list.component';
import {LoginComponent} from './login/login.component';
import {MealListComponent} from './meals/meal-list/meal-list.component';
import {AuthenticationService} from './login/authentication.service';
import {CrudListExampleComponent} from './crud/crud-list/crud-list-example/crud-list-example.component';
import {TestParentComponent} from './test/test-parent/test-parent.component';

const routes: Routes = [
  {path: 'users', component: UserListComponent, canActivate: [AuthenticationService]},
  {path: 'meals', component: MealListComponent, canActivate: [AuthenticationService]},
  {path: 'login', component: LoginComponent},
  {path: 'logout', component: LoginComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
