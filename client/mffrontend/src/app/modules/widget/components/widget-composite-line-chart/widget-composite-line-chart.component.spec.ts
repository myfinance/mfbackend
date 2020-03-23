import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WidgetCompositeLineChartComponent } from './widget-composite-line-chart.component';

describe('WidgetCompositeLineChartComponent', () => {
  let component: WidgetCompositeLineChartComponent;
  let fixture: ComponentFixture<WidgetCompositeLineChartComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WidgetCompositeLineChartComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WidgetCompositeLineChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
