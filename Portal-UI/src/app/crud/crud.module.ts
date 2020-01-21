import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CrudListComponent} from './crud-list/crud-list.component';
import {MaterialModule} from '../material.module';
import {CrudListExampleComponent} from './crud-list/crud-list-example/crud-list-example.component';
import {TestParentComponent} from '../test/test-parent/test-parent.component';
import {TestChildComponent} from '../test/test-child/test-child.component';
import {CrudDialogComponent} from './crud-dialog/crud-dialog.component';
import {ReactiveFormsModule} from '@angular/forms';

@NgModule({
  declarations: [CrudListComponent, CrudListExampleComponent, TestParentComponent, TestChildComponent, CrudDialogComponent],
  exports: [
    CrudListComponent,
    CrudDialogComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    ReactiveFormsModule
  ],
  entryComponents: [CrudDialogComponent]
})
export class CrudModule {
}
