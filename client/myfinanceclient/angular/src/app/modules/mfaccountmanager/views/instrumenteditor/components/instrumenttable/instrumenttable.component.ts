import {Component, Input, OnInit} from '@angular/core';
import {InstrumentService} from '../../services/instrument.service';
import { GridOptions } from 'ag-grid-community';

@Component({
  selector: 'app-instrumenttable',
  templateUrl: './instrumenttable.component.html',
  styleUrls: ['./instrumenttable.component.scss']
})
export class InstrumenttableComponent implements OnInit {


  @Input() data: any;

  options: GridOptions;

  title = 'Transactions';

  constructor(private instrumentservice: InstrumentService) { }

  ngOnInit() {
    this.options = <GridOptions>{
      rowSelection: 'single',
      onSelectionChanged: () => this.onSelectionChanged(),
      onGridReady: () => this.onGridReady(),
      floatingFilter: true,
      resizeable: true,
      sortable: true,
      sideBar: 'filters',
      suppressPropertyNamesCheck: true,
      columnDefs: [
        {headerName: 'Id', field: 'instrumentid' },
        {headerName: 'Beschreibung', field: 'description'},
        {headerName: 'Zuletzt geändert', field: 'lastchanged'},
        {headerName: 'InstrumentType', field: 'instrumentType'}
      ]
    };
  }

  private loadData(): void {
    if(this.options.api != null) {
      this.options.api.setRowData(this.instrumentservice.getInstruments());
    }
  }

  onSelectionChanged(): void {
    // this.applicationLogService.selectedLogEntry = this.options.api.getSelectedRows()[0];
  }

  onGridReady(): void {
    if (this.instrumentservice.getIsInit()) {
      this.loadData();
    } else {
      this.instrumentservice.instrumentSubject.subscribe(
        () => {
            this.loadData()
          }
      )
    }
  }
}
