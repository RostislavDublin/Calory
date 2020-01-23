import {CrudListFilterConfig} from './crud-list-filter-config';

describe('CrudListFilterConfig', () => {
  it('should create an instance', () => {
    expect(new CrudListFilterConfig({
      id: 'string',
      name: 'string',
    })).toBeTruthy();
  });
});
