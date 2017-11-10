import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WidgetHorizontalBarChartComponent } from './widget-horizontal-bar-chart.component';

describe('WidgetHorizontalBarChartComponent', () => {
  let component: WidgetHorizontalBarChartComponent;
  let fixture: ComponentFixture<WidgetHorizontalBarChartComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WidgetHorizontalBarChartComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WidgetHorizontalBarChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
