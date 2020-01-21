import {Component, EventEmitter, Output} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthenticationService} from '../login/authentication.service';
import {AppComponent} from '../app.component';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  @Output() public sidenavToggle = new EventEmitter();

  constructor(
    private appComponent: AppComponent,
    private route: ActivatedRoute,
    private router: Router,
    private auth: AuthenticationService
  ) {
  }

  onToggleSidenav() {
    this.sidenavToggle.emit();
  }
}
