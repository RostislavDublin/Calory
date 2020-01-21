import {CrudListColumnConfig} from './crud-list-column-config';
import {Observable, Subject} from 'rxjs';
import {CrudDialogConfig} from '../../crud-dialog/config/crud-dialog-config';

export class CrudListConfig<T> {
  get itemName(): string {
    return this._itemName;
  }

  set itemName(value: string) {
    this._itemName = value;
  }
  get columns(): CrudListColumnConfig[] {
    return this._columns;
  }

  private _listTitle: string = "Item List";
  private _itemName: string = "Item";

  private _columns: CrudListColumnConfig[];
  /**
   * Data collection. Can be initially set in constructor.
   */
  private _data: T[];
  /**
   * Async data subject (both Observable and observer). Allows to subscribe on data updates and also to provide new data.
   */
  private _asyncData: Subject<T[]> = new Subject<T[]>();
  /**
   * Async data supplier. Something that can be triggered to request new data and put it to the _asyncData stream.
   */
  private _asyncDataSupplier: () => Observable<T[]> = null;


  constructor(listTitle: string, columns: CrudListColumnConfig[], data: T[]) {
    this._listTitle = listTitle;
    this._columns = columns;
    this._data = data;

    this._asyncData.subscribe(next => {
      this._data = next;
    });
  }

  get listTitle(): string {
    return this._listTitle;
  }

  set listTitle(value: string) {
    this._listTitle = value;
  }


  get crudDialogConfig(): CrudDialogConfig {
    return this._crudDialogConfig;
  }

  set asyncDataSupplier(value: () => Observable<T[]>) {
    this._asyncDataSupplier = value;
  }

  get data(): T[] {
    return this._data;
  }

  get asyncData(): Subject<T[]> {
    return this._asyncData;
  }

  private _crudDialogConfig: CrudDialogConfig = new CrudDialogConfig();

  requestAsyncDataFromSupplier(): void {
    this._asyncDataSupplier()//.apply(this)
      .subscribe(next => {
        this._asyncData.next(next);
      });
  }

  getColumnIds(): string[] {
    return this._columns.map(c => c.id);
  }

  getColumnsToFilter(): CrudListColumnConfig[] {
    return this._columns.filter(c => c.filter);
  }

  filterPredicate() {
    const myFilterPredicate = (data: T, filter: string): boolean => {

      const filtrs = JSON.parse(filter);

      for (const key in filtrs) {
        if (filtrs.hasOwnProperty(key)) {
          const val: string = filtrs[key];
          if (val && !String(data[key]).toLowerCase().includes(val)) {
            return false;
          }
        }
      }

      return true;
    };
    return myFilterPredicate;
  }

  /**
   * Callback to style list's row
   */
  private _entryClassProvider: (entry: T) => {};

  set entryClassProvider(value: (entry: T) => {}) {
    this._entryClassProvider = value;
  }

  getEntryClass(entry: T): {} {
    return this._entryClassProvider(entry);
  }

}
