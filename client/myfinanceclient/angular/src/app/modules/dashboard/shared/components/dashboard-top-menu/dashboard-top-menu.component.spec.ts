import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { DataService } from '../../services/data.service';
import { CsvService } from '../../services/csv.service';

import { DashboardTopMenuComponent } from './dashboard-top-menu.component';

describe('DashboardTopMenuComponent', () => {
  let component: DashboardTopMenuComponent;
  let fixture: ComponentFixture<DashboardTopMenuComponent>;
  let dataService: DataService;
  let csvService: CsvService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      providers: [ 
        DataService,
        CsvService
      ],
      declarations: [ DashboardTopMenuComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    dataService = TestBed.get(DataService);
    csvService = TestBed.get(CsvService);
    fixture = TestBed.createComponent(DashboardTopMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
