import {ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {UserService} from '../user.service';
import {User} from '../../model/user';
import {Router} from '@angular/router';
import {UserFormComponent} from '../user-form/user-form.component';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {MatTableDataSource} from '@angular/material/table';
import {SelectionModel} from '@angular/cdk/collections';
import {MatMenuTrigger} from '@angular/material/menu';
import {MatPaginator} from '@angular/material/paginator';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

  users: User[];
  displayedColumns: string[] = ['id', 'name', 'email', 'dob', 'gender', 'createdDate', 'modifiedDate'];
  dataSource = new MatTableDataSource<User>();
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  selection = new SelectionModel<User>(false, []);

  @ViewChild(MatMenuTrigger, {static: false})
  contextMenu: MatMenuTrigger;
  contextMenuPosition = {x: '0px', y: '0px'};

  constructor(
    private userService: UserService,
    private router: Router,
    public dialog: MatDialog,
    private changeDetectorRefs: ChangeDetectorRef
  ) {
  }

  ngOnInit() {
    this.dataSource.paginator = this.paginator;
    this.refresh();
  }

  refresh() {
    this.userService.getUsers()
      .subscribe(data => {
        this.users = data;
        this.dataSource.data = this.users;
        this.changeDetectorRefs.detectChanges();
      });
  }

  openDialog(crudMode: string, user: User): void {

    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.data = {crudMode, user};
    dialogConfig.width = '500px';

    const dialogRef = this.dialog.open(UserFormComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(res => {
      this.refresh();
    });
  }

  tableKeydown(event: KeyboardEvent) {
    if (event.key === 'ArrowDown' || event.key === 'ArrowUp') {
      if (!this.selection.isEmpty()) {
        let newSelection;
        const currentSelection = this.selection.selected[0];
        let currentIndex = this.dataSource.data.findIndex(row => row === currentSelection);

        if (event.key === 'ArrowDown' && currentIndex < (this.dataSource.data.length - 1)) {
          currentIndex = currentIndex + 1;
        } else if (event.key === 'ArrowUp' && currentIndex > 0) {
          currentIndex = currentIndex - 1;
        }
        const desiredPage: number = Math.floor((currentIndex) / this.paginator.pageSize);
        while (this.paginator.pageIndex !== desiredPage) {
          if (this.paginator.pageIndex < desiredPage) {
            this.paginator.nextPage();
          } else {
            this.paginator.previousPage();
          }
        }
        newSelection = this.dataSource.data[currentIndex];
        if (newSelection) {
          this.selection.toggle(newSelection);
        }
      }
    }
  }

  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();
    this.paginator.firstPage();
  }

  /**
   * https://stackblitz.com/edit/angular-material-context-menu?file=app%2Fcontext-menu-example.html
   */
  onContextMenu(event: MouseEvent, user: User) {
    event.preventDefault();
    this.contextMenuPosition.x = event.clientX + 'px';
    this.contextMenuPosition.y = event.clientY + 'px';
    this.contextMenu.menuData = {user};
    this.contextMenu.openMenu();
  }

  onContextMenuDeleteUser(user: User) {
    this.userService.delete(user).subscribe(next => this.refresh());
  }
}
