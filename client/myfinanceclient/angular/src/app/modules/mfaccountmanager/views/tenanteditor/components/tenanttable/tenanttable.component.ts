import {Component, Input, OnInit} from '@angular/core';
import { GridOptions } from 'ag-grid-community';
import {TenantService} from "../../services/tenant.service";

@Component({
  selector: 'app-tenanttable',
  templateUrl: './tenanttable.component.html',
  styleUrls: ['./tenanttable.component.scss']
})
export class TenanttableComponent implements OnInit {


  @Input() data: any;

  options: GridOptions;

  title = 'Transactions';


  constructor(private tenantservice: TenantService) { }

  ngOnInit() {
    this.options = <GridOptions>{
      rowSelection: 'single',
      onSelectionChanged: () => this.onSelectionChanged(),
      onGridReady: () => this.onGridReady(),
      floatingFilter: true,
      enableColResize: true,
      enableSorting: true,
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
    this.options.api.setRowData(this.tenantservice.getTenants());
  }

  onSelectionChanged(): void {
    this.tenantservice.setSelectedTenant(this.options.api.getSelectedRows()[0])
  }

  onGridReady(): void {
    if(this.tenantservice.getIsInit()){
      this.loadData();
    } else {
      this.tenantservice.instrumentSubject.subscribe(
        () => {
          this.loadData()}
      )
    }
  }
}
