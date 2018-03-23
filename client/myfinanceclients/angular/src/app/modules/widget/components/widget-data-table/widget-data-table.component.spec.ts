import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WidgetDataTableComponent } from './widget-data-table.component';

describe('WidgetDataTableComponent', () => {
  let component: WidgetDataTableComponent;
  let fixture: ComponentFixture<WidgetDataTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WidgetDataTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WidgetDataTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
