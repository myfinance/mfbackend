import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TenantupdateformComponent } from './tenantupdateform.component';

describe('TenantupdateformComponent', () => {
  let component: TenantupdateformComponent;
  let fixture: ComponentFixture<TenantupdateformComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TenantupdateformComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TenantupdateformComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
