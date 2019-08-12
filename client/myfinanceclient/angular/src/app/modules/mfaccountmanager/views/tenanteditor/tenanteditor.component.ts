import {ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {AbstractDashboard} from "../../../dashboard/abstract-dashboard";
import {DashboardService} from "../../../dashboard/services/dashboard.service";
import {InstrumentService} from "../instrumenteditor/services/instrument.service";

@Component({
  selector: 'app-tenenteditor',
  templateUrl: './tenanteditor.component.html',
  styleUrls: ['./tenanteditor.component.scss']
})
export class TenanteditorComponent  extends AbstractDashboard implements OnInit, OnDestroy {

  title = 'Transactions';
  view = 'fit';

  widgets = [
    {
      uuid: 'c326fa31-7a60-4b8e-9bda-e4e59cf4bede',
      title: 'Controller',
      x: 0,
      y: 0,
      rows: 1,
      cols: 1,
      draggable: true,
      resizable: true
    },
    {
      uuid: 'aa7c6c1f-2da6-4b92-89b5-1e03b21121ff',
      title: 'Editor',
      x: 1,
      y: 0,
      rows: 1,
      cols: 1,
      draggable: true,
      resizable: true
    },
    {
      uuid: 'edff5286-e9e9-4ae9-b23f-d70adbabffae',
      title: 'Alle Mandaten',
      x: 0,
      y: 1,
      rows: 1,
      cols: 2,
      draggable: true,
      resizable: true
    }
  ];

  //dashboardService and instrumentservice are not used directly here but it is necessary to put them in the constructor to initialize them
  constructor( public dashboardService: DashboardService, instrumentservice: InstrumentService, changeDetectorRef: ChangeDetectorRef ) {
    super(changeDetectorRef);
  }

  ngOnInit() {

  }

  ngOnDestroy() {
  }
}
