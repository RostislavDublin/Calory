import {Component, Inject, NgZone, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators} from '@angular/forms';
import {UserService} from '../user.service';
import {Router} from '@angular/router';
import {HttpErrorResponse} from '@angular/common/http';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import * as moment from 'moment';
import {User} from '../../model/user';
import {PartialObserver} from 'rxjs';

@Component({
  selector: 'app-my-modal',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.css'],
})

export class UserFormComponent implements OnInit {

  constructor(
    private dialogRef: MatDialogRef<UserFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public fb: FormBuilder,
    private userService: UserService,
    private router: Router,
    private ngZone: NgZone,
  ) {
  }

  /* Shorthands for form controls (used from within template) */
  get password() {
    return this.userForm.get('password');
  }

  get password2() {
    return this.userForm.get('password2');
  }

  user: User;
  userForm: FormGroup;
  minPasswordLength = 8;
  crudMode: string;
  crudTitle: string;
  passwordPanelExpanded = false;

  formSubmitSubscriber: PartialObserver<any> = {
    next: (result) => {
      this.dialogRef.close();
    },
    error: error => {
      if (error instanceof HttpErrorResponse) {
        console.log('Status: %d, message: %s', error.error.status, error.error.message);
        this.userForm.get('name').setErrors({notUnique: true});
      }
    }
  };

  minAgeInYears = 7;
  maxAgeInYears = 100;
  dobStartDate: Date = new Date('01/01/2000');

  ngOnInit() {
    this.initForm();
  }

  private initForm() {

    this.crudMode = this.data.crudMode ? this.data.crudMode : 'C';
    this.user = this.data.user;

    this.userForm = this.fb.group({
      name: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      dob: ['', [Validators.required, this.dateRangeValidator]],
      gender: [''],
      password: [''],
      password2: [''],
    }, {validator: this.passwordMatchValidator});

    switch (this.crudMode) {
      case 'C':
        this.crudTitle = 'Add User';
        this.passwordPanelExpanded = true;
        break;
      case 'R':
        this.initValues();
        this.crudTitle = 'View User';
        break;
      case 'U':
        this.initValues();
        this.crudTitle = 'Edit User';
        break;
      case 'D':
        this.crudTitle = 'Delete User';
        this.initValues();
        break;
    }

    this.setPasswordValidators();
  }

  initValues() {
    this.set('name', this.user.name);
    this.set('email', this.user.email);
    const dob: Date = new Date(this.user.dob);
    this.set('dob', dob);
    this.set('gender', this.user.gender);
  }

  /* Get errors */
  public handleError = (controlName: string, errorName: string) => {
    return this.userForm.controls[controlName].hasError(errorName);
  }

  /* Submit form (Create and Update) */
  submitUserForm() {
    if (this.userForm.valid) {
      const userToSave = this.userForm.value;
      delete userToSave.password2;
      if (!this.passwordPanelExpanded) {
        delete userToSave.password;
      }
      if (this.crudMode === 'C') {
        this.userService.save(userToSave).subscribe(this.formSubmitSubscriber);
      } else if (this.crudMode === 'U') {
        userToSave.id = this.user.id;
        this.userService.update(userToSave).subscribe(this.formSubmitSubscriber);
      }
    }
  }
  /**
   *
   */
  dateRangeValidator: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
    const strDate: string = control.value;
    if (!strDate) {
      return {required: true};
    }
    const date = moment(strDate);
    const curDate = moment(new Date());
    const diff = curDate.diff(date, 'years');

    if (diff < this.minAgeInYears) {
      return {tooYoung: true};
    } else if (diff > this.maxAgeInYears) {
      return {tooOld: true};
    }

    return null;
  }

  /* Called on each input in either password field */
  onPasswordInput() {
    if (this.userForm.hasError('passwordMismatch')) {
      this.password2.setErrors([{passwordMismatch: true}]);
    } else {
      this.password2.setErrors(null);
    }
  }

  passwordMatchValidator: ValidatorFn = (formGroup: FormGroup): ValidationErrors | null => {
    // no validation because password panel is closed
    if (!this.passwordPanelExpanded) {
      return null;
      // valid, cause passwords match
    } else if (formGroup.get('password').value === formGroup.get('password2').value) {
      return null;
 } else {
      return {passwordMismatch: true};
 }
  }


  setPasswordPanelExpanded(expanded: boolean) {
    console.log('fired setPasswordPanelExpanded(%s)', expanded);
    this.passwordPanelExpanded = expanded;
    // force password match validator run
    this.setPasswordValidators();
    this.password.updateValueAndValidity({onlySelf: true});
    this.password2.updateValueAndValidity({onlySelf: true});
    this.userForm.updateValueAndValidity({onlySelf: true});
    this.onPasswordInput();
  }

  setPasswordValidators() {
    if (this.passwordPanelExpanded) {
      this.password.setValidators([Validators.required, Validators.minLength(this.minPasswordLength)]);
      this.password2.setValidators([Validators.required, Validators.minLength(this.minPasswordLength)]);
    } else {
      this.password.setValidators(null);
      this.password2.setValidators(null);
    }
  }

  cancelDialog() {
    this.dialogRef.close();
  }

  set(control: string, value: any) {
    this.userForm.get(control).setValue(value);
  }

}
