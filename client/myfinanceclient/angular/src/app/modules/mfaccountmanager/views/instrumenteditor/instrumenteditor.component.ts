import {ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {AbstractDashboard} from '../../../dashboard/abstract-dashboard';
import {DashboardService} from '../../../dashboard/services/dashboard.service';
import {InstrumentService} from './services/instrument.service';
import {DashboardDataLoadedEventModel} from '../../../dashboard/models/dashboard-data-loaded-event.model';

@Component({
  selector: 'app-instrumenteditor',
  templateUrl: './instrumenteditor.component.html',
  styleUrls: ['./instrumenteditor.component.scss']
})
export class InstrumenteditorComponent  extends AbstractDashboard implements OnInit, OnDestroy {

  title = 'Transactions';
  view = 'fit';

  widgets = [
    {
      uuid: '5c147b24-d1a8-4eaf-b7e9-c5c98f96ca12',
      title: 'Controller',
      x: 0,
      y: 0,
      rows: 1,
      cols: 1,
      draggable: true,
      resizable: true
    },
    {
      uuid: '5c227628-80d4-4b47-b4a3-d7fb824a56f2',
      title: 'Alle Instrumente',
      x: 0,
      y: 1,
      rows: 1,
      cols: 1,
      draggable: true,
      resizable: true
    }
  ];

  // dashboardService and instrumentservice are not used directly here but it is necessary to put them in the constructor to initialize them
  constructor( public dashboardService: DashboardService,
               public instrumentservice: InstrumentService,
               changeDetectorRef: ChangeDetectorRef ) {
    super(changeDetectorRef);
  }

  ngOnInit() {
    this.dashboardService.dataLoadedSubject.subscribe(
      () => {
        if (this.grid) { this.grid.refresh(); }
      })
  }

  ngOnDestroy() {
  }
}
