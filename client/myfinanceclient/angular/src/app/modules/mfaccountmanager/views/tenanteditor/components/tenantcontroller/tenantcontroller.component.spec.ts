import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TenantcontrollerComponent } from './tenantcontroller.component';

describe('TenantcontrollerComponent', () => {
  let component: TenantcontrollerComponent;
  let fixture: ComponentFixture<TenantcontrollerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TenantcontrollerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TenantcontrollerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
