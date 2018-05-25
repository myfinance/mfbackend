import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LinechartexpComponent } from './linechartexp.component';

describe('LinechartexpComponent', () => {
  let component: LinechartexpComponent;
  let fixture: ComponentFixture<LinechartexpComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LinechartexpComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LinechartexpComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
