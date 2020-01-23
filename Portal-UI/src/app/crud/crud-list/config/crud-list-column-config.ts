export class CrudListColumnConfig {
  id: string;
  name: string;
  formatter?: (string) => string;

  public formatted(value): string {
    return (this.formatter ? this.formatter(value) : value);
  }

  constructor(construct: { id: string, name: string, formatter?: (string) => string }) {
    this.id = construct.id;
    this.name = construct.name;
    this.formatter = construct.formatter;
  }
}
