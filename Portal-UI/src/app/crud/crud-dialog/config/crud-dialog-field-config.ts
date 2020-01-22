import {Validators} from "@angular/forms";

export class CrudDialogFieldConfig {
  id: string;
  placeholder: string;
  type?: string = 'text';
  disabled?: boolean = false
  validation?: { validator: Validators, errors: [{ errorName: string, errorMessage: string }] }[] = [];

  public getValidators(): Validators[] {
    const validators: Validators[] = [];

    if (!this.validation || this.validation.length === 0) {
      return validators;
    }

    for (let entry of this.validation) {
      validators.push(entry.validator);
    }

    return validators;
  }

  constructor(construct: {
    id: string, placeholder: string, type?: string, disabled?: boolean,
    validation?: Array<{ validator: Validators, errors: [{ errorName: string, errorMessage: string }] }>
  }) {
    this.id = construct.id;
    this.placeholder = construct.placeholder;
    this.type = (construct.type ? construct.type : 'text');
    this.disabled = (construct.disabled ? construct.disabled : false);
    this.validation = (construct.validation ? construct.validation : []);
  }
}
