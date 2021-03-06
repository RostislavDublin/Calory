import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {CrudMode} from '../crud-mode.enum';
import {CrudDialogConfig} from './config/crud-dialog-config';
import {PartialObserver} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-crud-dialog',
  templateUrl: './crud-dialog.component.html',
  styleUrls: ['./crud-dialog.component.css']
})
export class CrudDialogComponent implements OnInit {

  CRUD_MODES = CrudMode;
  crudForm: FormGroup;
  crudTitle: string;
  crudSubTitle: string;
  crudDialogErrors: string[] = [];

  constructor(
    private dialogRef: MatDialogRef<CrudDialogComponent>,
    @Inject(MAT_DIALOG_DATA)private crudDialogConfig: CrudDialogConfig,
    public fb: FormBuilder,
  ) {
  }

  ngOnInit() {
    this.initForm();
    console.log(this.findInvalidControls());
    console.log(this.crudForm.valid);

  }

  private initForm() {

    const controlsConfig = {};
    const item = this.crudDialogConfig.item;
    for (const field of this.crudDialogConfig.fields) {
      const value = (item && item[field.id]) ? item[field.id] : '';
      const disabled = field.disabled || (this.crudDialogConfig.crudMode === CrudMode.Delete || this.crudDialogConfig.crudMode === CrudMode.Read);
      controlsConfig[field.id] = [{value, disabled: disabled}, field.getValidators()];
    }

    this.crudForm = this.fb.group(controlsConfig, {});

    switch (this.crudDialogConfig.crudMode) {
      case CrudMode.Create:
        this.crudTitle = 'Add '+ this.crudDialogConfig.itemTitle;
        break;
      case CrudMode.Read:
        this.crudTitle = 'View '+ this.crudDialogConfig.itemTitle;
        break;
      case CrudMode.Update:
        this.crudTitle = 'Edit '+ this.crudDialogConfig.itemTitle;
        break;
      case CrudMode.Delete:
        this.crudTitle = 'Delete '+ this.crudDialogConfig.itemTitle;

        break;
    }
  }

  set(control: string, value: any) {
    this.crudForm.get(control).setValue(value);
  }

  cancelDialog() {
    console.log('CrudDialog cancelCrudForm');
    this.dialogRef.close();
  }

  /* Get errors */
  public handleError = (controlName: string, errorName: string) => {
    // console.log(this.findInvalidControls());
    return this.crudForm.controls[controlName].hasError(errorName);
  };

  public findInvalidControls() {
    const invalid = [];
    const controls = this.crudForm.controls;
    for (const name in controls) {
      if (controls[name].invalid) {
        invalid.push(name);
      }
    }
    return invalid;
  }


  /* Submit form (Create and Update) */
  submitCrudForm() {
    console.log('CrudDialog submitCrudForm');
    if (this.findInvalidControls().length === 0) {
      const itemToSubmit = this.crudForm.getRawValue();

      this.crudDialogConfig
        .submitter(this.crudDialogConfig.crudMode, itemToSubmit)
        .subscribe(this.submitDialogSubscriber);

    }
  }

  submitDialogSubscriber: PartialObserver<any> = {
    next: (result) => {
      this.crudDialogErrors = [];
      this.dialogRef.close();
    },
    error: error => {
      if (error instanceof HttpErrorResponse) {
        console.log('Status: %d, message: %s', error.status, error.message);
        if(error.status === 0) {
          this.crudDialogErrors = ["SERVER UNAVAILABLE: " + error.message];
        } else{
          this.crudDialogErrors = [error.message];
        }
      } else {
        this.crudDialogErrors = [error];
      }
    }
  };


}
