import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MockComponent } from 'ng-mocks';

import { DashboardContentComponent } from './dashboard-content.component';
import { DashboardService } from '../../services/dashboard.service';
import { DimmerComponent } from '../../../xva-common/components/dimmer/dimmer.component';

class DashboardServiceStub { }

describe('DashboardContentComponent', () => {
  let component: DashboardContentComponent;
  let fixture: ComponentFixture<DashboardContentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        DashboardContentComponent,
        MockComponent(DimmerComponent)
      ],
      providers: [
        { provide: DashboardService, useClass: DashboardServiceStub }
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardContentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
