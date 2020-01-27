import {CrudListColumnConfig} from './crud-list-column-config';
import {Observable, Subject} from 'rxjs';
import {CrudDialogConfig} from '../../crud-dialog/config/crud-dialog-config';
import {CrudListFilterConfig} from "./crud-list-filter-config";

export class CrudListConfig<T> {

  private _listTitle: string = "Item List";
  private _itemName: string = "Item";

  private _columns: CrudListColumnConfig[];
  private _filters: CrudListFilterConfig[] = [];
  private _filterIdToFilterMap: Map<string, CrudListFilterConfig> = new Map<string, CrudListFilterConfig>();
  private _filterIdToValueMap: Map<string, string> = new Map<string, any>();
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
  private _asyncDataSupplier: (filterValues?: {}) => Observable<T[]> = null;

  /**
   * Callback to style list's row
   */
  private _entryClassProvider: (entry: T) => {};


  constructor(construct: {
    listTitle: string,
    columns: CrudListColumnConfig[],
    filters?: CrudListFilterConfig[],
    data: T[],
  }) {
    this._listTitle = construct.listTitle;
    this._columns = construct.columns;
    this._data = construct.data;

    //when async data arrive, it put into the collection
    this._asyncData.subscribe(next => {
      this._data = next;
    });

    if (construct.filters) {
      this._filters = construct.filters;
      for (const filter of this._filters) {
        this._filterIdToFilterMap.set(filter.id, filter);
        this.setFilterValue(filter.id, filter.initialValue ? filter.initialValue : '');
      }
    }
  }

  get itemName(): string {
    return this._itemName;
  }

  set itemName(value: string) {
    this._itemName = value;
  }

  get columns(): CrudListColumnConfig[] {
    return this._columns;
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

  set asyncDataSupplier(value: (filterValues: Map<string, any>) => Observable<T[]>) {
    this._asyncDataSupplier = value;
  }

  get data(): T[] {
    return this._data;
  }

  get asyncData(): Subject<T[]> {
    return this._asyncData;
  }

  private _crudDialogConfig: CrudDialogConfig = new CrudDialogConfig();

  getFilters() {
    return this._filters;
  }
  getFilterById(id: string): CrudListFilterConfig {
    return this._filterIdToFilterMap.get(id);
  }

  setFilterValue(id: string, value: any) {
    this._filterIdToValueMap.set(id, value);
  }

  getFilterValue(id: string): any {
    return this._filterIdToValueMap.get(id);
  }

  getFilterValues(): Map<string, string> {
    return this._filterIdToValueMap;
  }


  requestAsyncDataFromSupplier(): void {
    this._asyncDataSupplier(this.getFilterValues())
      .subscribe(next => {
        this._asyncData.next(next);
      });
  }

  getColumnIds(): string[] {
    return this._columns.map(c => c.id);
  }

  clientFilterPredicate() {
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


  set entryClassProvider(value: (entry: T) => {}) {
    this._entryClassProvider = value;
  }

  getEntryClass(entry: T): {} {
    return this._entryClassProvider(entry);
  }

}
