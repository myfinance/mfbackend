import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GridexpComponent } from './gridexp.component';

describe('GridexpComponent', () => {
  let component: GridexpComponent;
  let fixture: ComponentFixture<GridexpComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GridexpComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GridexpComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
