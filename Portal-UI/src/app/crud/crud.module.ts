import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CrudListComponent} from './crud-list/crud-list.component';
import {MaterialModule} from '../material.module';
import {TestParentComponent} from '../test/test-parent/test-parent.component';
import {TestChildComponent} from '../test/test-child/test-child.component';
import {CrudDialogComponent} from './crud-dialog/crud-dialog.component';
import {ReactiveFormsModule} from '@angular/forms';
import {MatSelectModule} from "@angular/material/select";

@NgModule({
  declarations: [CrudListComponent, TestParentComponent, TestChildComponent, CrudDialogComponent],
  exports: [
    CrudListComponent,
    CrudDialogComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    ReactiveFormsModule,
    MatSelectModule
  ],
  entryComponents: [CrudDialogComponent]
})
export class CrudModule {
}
