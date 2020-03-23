import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TenanttableComponent } from './tenanttable.component';

describe('TenanttableComponent', () => {
  let component: TenanttableComponent;
  let fixture: ComponentFixture<TenanttableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TenanttableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TenanttableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
