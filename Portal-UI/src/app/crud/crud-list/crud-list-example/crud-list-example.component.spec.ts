import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CrudListExampleComponent } from './crud-list-example.component';

describe('CrudListExampleComponent', () => {
  let component: CrudListExampleComponent;
  let fixture: ComponentFixture<CrudListExampleComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CrudListExampleComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CrudListExampleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
