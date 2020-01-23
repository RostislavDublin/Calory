import {Component, EventEmitter, Output} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthenticationService} from '../login/authentication.service';
import {AppComponent} from '../app.component';
import {MatDialog, MatDialogConfig} from "@angular/material/dialog";
import {UserFormComponent} from "../user/user-form/user-form.component";
import {UserService} from "../user/user.service";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {

  AUTH = AuthenticationService;
  @Output() public sidenavToggle = new EventEmitter();

  constructor(
    private appComponent: AppComponent,
    private route: ActivatedRoute,
    private router: Router,
    private auth: AuthenticationService,
    public dialog: MatDialog,
    private userService: UserService
  ) {
  }

  onToggleSidenav() {
    this.sidenavToggle.emit();
  }


  register() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.data = {crudMode: 'C', user: null};
    dialogConfig.width = '500px';

    const dialogRef = this.dialog.open(UserFormComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(res => {
      // this.refresh();
    });

  }

  editProfile() {
    this.userService.getUserById(this.auth.getLoggedInUserId()).subscribe(next => {
      const dialogConfig = new MatDialogConfig();
      dialogConfig.disableClose = true;
      dialogConfig.autoFocus = true;
      dialogConfig.data = {crudMode: 'U', user: next};
      dialogConfig.width = '500px';

      const dialogRef = this.dialog.open(UserFormComponent, dialogConfig);
    });
  }
}
