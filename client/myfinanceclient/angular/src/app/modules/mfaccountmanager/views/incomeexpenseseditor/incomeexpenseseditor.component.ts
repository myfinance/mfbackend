import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {AbstractDashboard} from "../../../dashboard/abstract-dashboard";

@Component({
  selector: 'incomeexpenseseditor',
  templateUrl: './incomeexpenseseditor.component.html',
  styleUrls: ['./incomeexpenseseditor.component.scss']
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
    },
    {
      uuid: '3870110e-6bfb-4cdc-9c3e-14d4c1924c72',
      title: 'Editor',
      x: 0,
      y: 1,
      rows: 1,
      cols: 1,
      draggable: true,
      resizable: true
    }
  ];

  constructor(    changeDetectorRef: ChangeDetectorRef) {
    super(changeDetectorRef);
  }

  ngOnInit() {
  }


}
