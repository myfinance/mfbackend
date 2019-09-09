import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InstrumentupdateformComponent } from './instrumentupdateform.component';

describe('InstrumentupdateformComponent', () => {
  let component: InstrumentupdateformComponent;
  let fixture: ComponentFixture<InstrumentupdateformComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InstrumentupdateformComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InstrumentupdateformComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
