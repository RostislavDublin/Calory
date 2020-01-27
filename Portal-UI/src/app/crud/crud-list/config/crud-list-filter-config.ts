export class CrudListFilterConfig {
  id: string;
  name: string;
  inputType: string;
  execution: 'client' | 'server' = 'server'
  initialValue: any = ''
  optionsData?: {} = {};

  constructor(construct: {
    id: string,
    name: string,
    inputType?: string,
    execution?: 'client' | 'server',
    initialValue?: any,
    optionsData?: {};
  }) {
    this.id = construct.id;
    this.name = construct.name;
    this.inputType = construct.inputType;
    this.execution = construct.execution;
    this.initialValue = construct.initialValue;
    this.optionsData = (construct.optionsData ? construct.optionsData : {})
  }
}
