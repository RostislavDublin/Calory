import {NgModule} from '@angular/core';
import {
  MatButtonModule,
  MatCardModule,
  MatDatepickerModule,
  MatDialogModule,
  MatDividerModule,
  MatExpansionModule,
  MatFormFieldModule,
  MatInputModule,
  MatTableModule,
  MatSortModule
} from '@angular/material';
import {MatRadioModule} from '@angular/material/radio';
import {MatNativeDateModule} from '@angular/material/core';
import {MatListModule} from '@angular/material/list';
import {MatMenuModule} from '@angular/material/menu';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatIconModule} from '@angular/material/icon';


@NgModule({
  imports: [MatDialogModule, MatFormFieldModule, MatButtonModule, MatInputModule, MatDividerModule, MatCardModule,
    MatDatepickerModule, MatRadioModule, MatNativeDateModule, MatListModule, MatTableModule, MatExpansionModule,
    MatMenuModule, MatPaginatorModule, MatSidenavModule, MatToolbarModule, MatIconModule, MatSortModule],
  exports: [MatDialogModule, MatFormFieldModule, MatButtonModule, MatInputModule, MatDividerModule, MatCardModule,
    MatDatepickerModule, MatRadioModule, MatNativeDateModule, MatListModule, MatTableModule, MatExpansionModule,
    MatMenuModule, MatPaginatorModule, MatSidenavModule, MatToolbarModule, MatIconModule, MatSortModule]
})

export class MaterialModule {
}
