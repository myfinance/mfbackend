import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import { GridOptions } from 'ag-grid-community';
import {TenantService} from '../../services/tenant.service';

@Component({
  selector: 'app-tenanttable',
  templateUrl: './tenanttable.component.html',
  styleUrls: ['./tenanttable.component.scss']
})
export class TenanttableComponent implements OnInit, OnDestroy {


  @Input() data: any;

  options: GridOptions;
  private gridApi;

  title = 'Transactions';


  constructor(private tenantservice: TenantService) {
    this.tenantservice.instrumentSubject.subscribe(
      () => {
        this.loadData()}
    )
  }

  ngOnInit() {
    this.options = <GridOptions>{
      rowSelection: 'single',
      onSelectionChanged: () => this.onSelectionChanged(),
      onGridReady: (params) => this.onGridReady(params),
      floatingFilter: true,
      resizeable: true,
      sortable: true,
      sideBar: 'filters',
      suppressPropertyNamesCheck: true,
      columnDefs: [
        {headerName: 'Id', field: 'instrumentid' },
        {headerName: 'Beschreibung', field: 'description'},
        {headerName: 'Zuletzt geändert', field: 'treelastchanged'},
        {headerName: 'Aktiv', field: 'isactive'},
        {headerName: 'InstrumentType', field: 'instrumentType'}
      ]
    };
  }

  private loadData(): void {
    if (this.options.api) {
      this.options.api.setRowData(this.tenantservice.getTenants());
    }
  }

  onSelectionChanged(): void {
    this.tenantservice.setSelectedTenant(this.options.api.getSelectedRows()[0])
  }

  onGridReady(params): void {
    this.gridApi = params.api;
    if (this.tenantservice.getIsInit()) {
      this.loadData();
    }
  }

  ngOnDestroy(): void {
  }
}
