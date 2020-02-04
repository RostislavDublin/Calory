import {CrudDialogFieldConfig} from './crud-dialog-field-config';
import {CrudMode} from '../../crud-mode.enum';
import {from, Observable} from "rxjs";

export class CrudDialogConfig {

  private _itemTitle: string = "Item";

  private _width: string = "90%";
  private _fields: CrudDialogFieldConfig[];
  private _crudMode: CrudMode = CrudMode.Read;
  private _item: any = null;

  private _createHandler: (whatToSubmit) => Observable<any> = (whatToSubmit) => from([true]);
  private _updateHandler: (whatToSubmit) => Observable<any> = (whatToSubmit) => from([true]);
  private _deleteHandler: (whatToSubmit) => Observable<any> = (whatToSubmit) => from([true]);


  private _submitter: (crudMode: CrudMode, whatToSubmit) => Observable<any>
    = (crudMode, whatToSubmit) => {
    return new Observable((observer) => {
      if (crudMode === CrudMode.Create) {
        this._createHandler(whatToSubmit).subscribe(data => observer.next(data), error => observer.error(error));
      } else if (crudMode === CrudMode.Update) {
        this._updateHandler(whatToSubmit).subscribe(data => observer.next(data), error => observer.error(error));
      } else if (crudMode === CrudMode.Delete) {
        this._deleteHandler(whatToSubmit).subscribe(data => observer.next(data), error => observer.error(error));
      } else {
        observer.next(true);
      }
    })
  }

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
    this._item = item ? item : {};

    //set column default values on Create
    if (mode === CrudMode.Create) {
      this.fields.forEach(f => {
        if (!this._item[f.id] && f.defaultValue) {
          this._item[f.id] = f.defaultValue();
        }
      })
    }
    return this;
  }

  get submitter(): (crudMode: CrudMode, whatToSubmit) => Observable<any> {
    return this._submitter;
  }

  set submitter(value: (crudMode: CrudMode, whatToSubmit) => Observable<any>) {
    this._submitter = value;
    this._submitter.apply(this);
  }

  set deleteHandler(value: (whatToSubmit) => Observable<any>) {
    this._deleteHandler = value;
  }

  set updateHandler(value: (whatToSubmit) => Observable<any>) {
    this._updateHandler = value;
  }

  set createHandler(value: (whatToSubmit) => Observable<any>) {
    this._createHandler = value;
  }

  set fields(value: CrudDialogFieldConfig[]) {
    this._fields = value;
  }

  get width(): string {
    return this._width;
  }

  set width(value: string) {
    this._width = value;
  }

  get itemTitle(): string {
    return this._itemTitle;
  }

  set itemTitle(value: string) {
    this._itemTitle = value;
  }

}
