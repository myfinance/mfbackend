import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ValuegraphComponent } from './valuegraph.component';

describe('ValuegraphComponent', () => {
  let component: ValuegraphComponent;
  let fixture: ComponentFixture<ValuegraphComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ValuegraphComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ValuegraphComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
