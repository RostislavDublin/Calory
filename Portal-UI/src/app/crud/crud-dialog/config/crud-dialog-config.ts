import {CrudDialogFieldConfig} from './crud-dialog-field-config';
import {CrudMode} from '../../crud-mode.enum';
import {from, Observable} from "rxjs";

export class CrudDialogConfig {

  set fields(value: CrudDialogFieldConfig[]) {
    this._fields = value;
  }

  private _fields: CrudDialogFieldConfig[];
  private _crudMode: CrudMode = CrudMode.Read;
  private _item: any = null;

  private _submitter: (crudMode: CrudMode, whatToSubmit) => Observable<any>
    = (crudMode, whatToSubmit) => from([true]);

  constructor(fields?: CrudDialogFieldConfig[]) {
    this._fields = fields ? fields : [];
  }

  get fields(): CrudDialogFieldConfig[] {
    return this._fields;
  }

  get item(): any {
    return this._item;
  }

  get crudMode(): CrudMode {
    return this._crudMode;
  }

  setModeAndItem(mode: CrudMode, item: any): CrudDialogConfig {
    this._crudMode = mode;
    this._item = item;
    return this;
  }

  get submitter(): (crudMode: CrudMode, whatToSubmit) => Observable<any> {
    return this._submitter;
  }

  set submitter(value: (crudMode: CrudMode, whatToSubmit) => Observable<any>) {
    this._submitter = value;
    this._submitter.apply(this);
  }

}
