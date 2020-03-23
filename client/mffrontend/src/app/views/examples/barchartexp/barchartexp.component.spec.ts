import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BarchartexpComponent } from './barchartexp.component';

describe('BarchartexpComponent', () => {
  let component: BarchartexpComponent;
  let fixture: ComponentFixture<BarchartexpComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BarchartexpComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BarchartexpComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
