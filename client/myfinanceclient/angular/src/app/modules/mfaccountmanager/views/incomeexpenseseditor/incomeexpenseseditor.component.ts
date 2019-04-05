import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {AbstractDashboard} from "../../../dashboard/abstract-dashboard";
import {DashboardDataLoadedEventModel} from "../../../dashboard/models/dashboard-data-loaded-event.model";
import {DatePipe} from "@angular/common";
import {DashboardService} from "../../../dashboard/services/dashboard.service";

@Component({
  selector: 'incomeexpenseseditor',
  templateUrl: './incomeexpenseseditor.component.html',
  styleUrls: ['./incomeexpenseseditor.component.scss'],
  providers: [DashboardService]
})
export class IncomeexpenseseditorComponent extends AbstractDashboard implements OnInit {

  title = 'Einnahmen und Ausgaben';
  view = 'fit';

  widgets = [
    {
      uuid: '5cbceb3d-a766-4ed0-8b83-467490bd3c9b',
      title: 'Alle Einnahmen und Ausgaben',
      x: 0,
      y: 0,
      rows: 1,
      cols: 1,
      draggable: true,
      resizable: true
    }
  ];

  constructor(    dashboardService: DashboardService,
                  changeDetectorRef: ChangeDetectorRef) {
    super(dashboardService, changeDetectorRef);
  }

  ngOnInit() {
  }

  handleDataLoaded(event: DashboardDataLoadedEventModel): void {
    this.dashboardService.toastr.success('Daten wurden erfolgreich geladen.');
    const pipe = new DatePipe('de');
    this.dashboardLoaded();
  }

}
