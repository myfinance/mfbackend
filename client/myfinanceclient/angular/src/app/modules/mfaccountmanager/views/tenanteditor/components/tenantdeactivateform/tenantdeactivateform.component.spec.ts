import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TenantdeactivateformComponent } from './tenantdeactivateform.component';

describe('TenantdeactivateformComponent', () => {
  let component: TenantdeactivateformComponent;
  let fixture: ComponentFixture<TenantdeactivateformComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TenantdeactivateformComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TenantdeactivateformComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
