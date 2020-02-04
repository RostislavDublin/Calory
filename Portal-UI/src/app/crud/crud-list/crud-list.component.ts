import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  Input,
  OnInit,
  TemplateRef,
  ViewChild
} from '@angular/core';
import {CrudListConfig} from './config/crud-list-config';
import {MatTable, MatTableDataSource} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {SelectionModel} from '@angular/cdk/collections';
import {MatSort} from '@angular/material';
import {MatMenuTrigger} from '@angular/material/menu';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {CrudDialogComponent} from '../crud-dialog/crud-dialog.component';
import {CrudDialogConfig} from '../crud-dialog/config/crud-dialog-config';
import {CrudMode} from '../crud-mode.enum';
import {HttpErrorResponse} from "@angular/common/http";
import {Subscriber} from "rxjs";

@Component({
  selector: 'app-crud-list',
  templateUrl: './crud-list.component.html',
  styleUrls: ['./crud-list.component.css'],
  changeDetection: ChangeDetectionStrategy.Default
})
export class CrudListComponent<T> implements OnInit {

  /**
   * This supplier knows how to (re)generate a subscriber, which knows how to populate the CRUD-list by fresh data.
   * We feed this supplier to the CrudListConfig, which subscribes it to its internal asyncDataSupplier.
   */
  private subscriberSupplier: () => Subscriber<T[]> = () => {
    const subscriber = new Subscriber<T[]>(
      (next) => {
        this.dataSource.data = next;
      }, error => {
        if (error instanceof HttpErrorResponse) {
          console.log('Status: %d, message: %s', error.status, error.message);
          if (error.status === 0) {
            this.errorDialogCaption = 'SERVER UNAVAILABLE';
            this.errorDialogContent = error.message;
          } else {
            this.errorDialogCaption = 'ERROR';
            this.errorDialogContent = error.message;
          }
        } else {
          this.errorDialogCaption = 'ERROR';
          this.errorDialogContent = error;
        }

        this.dialog1.open(this.errorDialog);
      }
    );
    subscriber.add(ss => {
      console.log("Subscription teardown: " + ss);
    })
    return subscriber;
  }

  constructor(
    public dialog: MatDialog,
    public dialog1: MatDialog,
    private cdref: ChangeDetectorRef
  ) {
    console.log('CRUD list constructor');
  }


  @Input() private config: CrudListConfig<T>;

  dataSource = new MatTableDataSource<T>([]);

  @ViewChild(MatTable, {static: false}) matTable: MatTable<any>;

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort: MatSort;
  selection = new SelectionModel<T>(false, []);

  @ViewChild(MatMenuTrigger, {static: false})
  contextMenu: MatMenuTrigger;
  contextMenuPosition = {x: '0px', y: '0px'};
  crudModes = CrudMode;

  ngOnInit() {
    console.log('CRUD list onInit');

    this.dataSource.paginator = this.paginator;
    this.dataSource.data = this.config.data;

    this.config.addAsyncDataSubscriberSupplier(this.subscriberSupplier);

    this.config.requestAsyncDataFromSupplier();

    this.dataSource.sort = this.sort;
    this.dataSource.filterPredicate = this.config.clientFilterPredicate();
  }

  applyFilter(columnId: string, filterValue: string) {

    this.config.setFilterValue(columnId, filterValue.toString().trim().toLowerCase());

    const clientFiltersValues = {};
    for (const entry of this.config.getFilterValues().entries()) {
      if (this.config.getFilterById(entry[0]).execution === 'client') {
        clientFiltersValues[entry[0]] = entry[1];
      }
    }

    this.dataSource.filter = JSON.stringify(clientFiltersValues);

    console.log("Client filters data" + this.dataSource.filter);

    this.config.requestAsyncDataFromSupplier();
    this.paginator.firstPage();

  }

  /**
   * https://stackblitz.com/edit/angular-material-context-menu?file=app%2Fcontext-menu-example.html
   */
  onContextMenu(event: MouseEvent, data: any) {
    event.preventDefault();
    this.contextMenuPosition.x = event.clientX + 'px';
    this.contextMenuPosition.y = event.clientY + 'px';
    this.contextMenu.menuData = {data};
    this.contextMenu.openMenu();
  }

  openCrudDialog(crudMode: CrudMode, item: T) {
    console.log('CRUD Dialog opened in mode: ' + crudMode);

    const matDialogConfig = new MatDialogConfig<CrudDialogConfig>();
    matDialogConfig.disableClose = true;
    matDialogConfig.autoFocus = true;
    matDialogConfig.data = this.config.crudDialogConfig.setModeAndItem(crudMode, item);
    matDialogConfig.width = this.config.crudDialogConfig.width;
    matDialogConfig.panelClass = "crud-dialog-panel"

    const dialogRef = this.dialog.open(CrudDialogComponent, matDialogConfig);

    dialogRef.afterClosed().subscribe(res => {
      this.config.requestAsyncDataFromSupplier();
    });

  }

  onContextMenuEditItem(data: T) {
    console.log('Edit item: ' + JSON.stringify(data));
    this.openCrudDialog(CrudMode.Update, data);
  }

  onContextMenuDeleteItem(data: T) {
    console.log('Delete item: ' + JSON.stringify(data));
    this.openCrudDialog(CrudMode.Delete, data);
  }

  ngAfterContentChecked() {
    this.cdref.detectChanges();
  }

  @ViewChild('errorDialog', {static: false}) errorDialog: TemplateRef<any>;
  private errorDialogCaption: string;
  private errorDialogContent: string;

  onRefreshButtonClick() {
    this.config.requestAsyncDataFromSupplier();
  }


}
