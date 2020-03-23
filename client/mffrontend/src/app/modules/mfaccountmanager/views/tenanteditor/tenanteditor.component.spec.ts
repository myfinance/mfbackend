import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TenanteditorComponent } from './tenanteditor.component';

describe('TenanteditorComponent', () => {
  let component: TenanteditorComponent;
  let fixture: ComponentFixture<TenanteditorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TenanteditorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TenanteditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
