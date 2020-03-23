import {ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {AbstractDashboard} from '../../../dashboard/abstract-dashboard';
import {DashboardService} from '../../../dashboard/services/dashboard.service';
import {TransactionService} from './services/transaction.service';

@Component({
  selector: 'app-transactioneditor',
  templateUrl: './transactioneditor.component.html',
  styleUrls: ['./transactioneditor.component.scss']
})
export class TransactioneditorComponent extends AbstractDashboard implements OnInit, OnDestroy {

  title = 'Transactions';
  view = 'fit';

  widgets = [
    {
      uuid: '21544ed9-5c0c-4d28-81e1-f586f6bc6621',
      title: 'Controller',
      x: 0,
      y: 0,
      rows: 2,
      cols: 1,
      draggable: true,
      resizable: true
    },
    {
      uuid: '3870110e-6bfb-4cdc-9c3e-14d4c1924c72',
      title: 'Editor',
      x: 0,
      y: 2,
      rows: 3,
      cols: 1,
      draggable: true,
      resizable: true
    },
    {
      uuid: '83d199d1-3bf4-43c1-a037-a465e7b6a87b',
      title: 'Wertentwicklung',
      x: 1,
      y: 0,
      rows: 1,
      cols: 3,
      draggable: true,
      resizable: true
    },
    {
      uuid: '5cbceb3d-a766-4ed0-8b83-467490bd3c9b',
      title: 'Alle Einnahmen und Ausgaben',
      x: 1,
      y: 1,
      rows: 2,
      cols: 3,
      draggable: true,
      resizable: true
    },
    {
      uuid: '763454ef-ca61-4a46-9df3-412f76ea3efb',
      title: 'Cashflows',
      x: 1,
      y: 3,
      rows: 2,
      cols: 3,
      draggable: true,
      resizable: true
    }
  ];

  // dashboardService and instrumentservice are not used directly here but it is necessary to put them in the constructor to initialize them
  constructor( public dashboardService: DashboardService,
               public transactionService: TransactionService,
               changeDetectorRef: ChangeDetectorRef ) {
    super(changeDetectorRef);
  }

  ngOnInit() {
    this.transactionService.dataLoadedSubject.subscribe(
      () => {
        if (this.grid) { this.grid.refresh(); }
      })
  }

  ngOnDestroy() {
  }
}
