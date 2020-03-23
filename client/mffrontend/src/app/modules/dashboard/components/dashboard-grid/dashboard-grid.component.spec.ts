import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GridsterModule } from 'angular-gridster2';
import { MockComponent } from 'ng-mocks';

import { DashboardGridComponent } from './dashboard-grid.component';
import { DashboardContentComponent } from '../dashboard-content/dashboard-content.component';
import { WidgetComponent } from '../../../widget/shared/components/widget/widget.component';
import { DashboardService } from '../../services/dashboard.service';

class DashboardServiceStub { }

describe('DashboardGridComponent', () => {
  let component: DashboardGridComponent;
  let fixture: ComponentFixture<DashboardGridComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        GridsterModule
      ],
      declarations: [
        DashboardGridComponent,
        MockComponent(DashboardContentComponent),
        MockComponent(WidgetComponent)
      ],
      providers: [
        { provide: DashboardService, useClass: DashboardServiceStub }
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardGridComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
