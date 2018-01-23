import { Component, OnInit, Input, Output, EventEmitter, ViewChild } from '@angular/core';
import {DashboardMenuFilterModel} from "../../models/dashboard-menu-filter.model";
import {DataService} from "../../services/data.service";



@Component({
  selector: 'app-dashboard-top-menu',
  templateUrl: './dashboard-top-menu.component.html',
  styleUrls: ['./dashboard-top-menu.component.scss']
})
export class DashboardTopMenuComponent implements OnInit {

  private _title: string;
  private _filters: DashboardMenuFilterModel[];
  private _csv: boolean;
  private _service: boolean;
  private _csvFilepath: string;
  _isCollapsed: boolean = true;

  @Input()
  set title(title: string) {
    this._title = title;
  }
  get title(): string {
    return this._title;
  }

  @Input()
  set filters(filters: DashboardMenuFilterModel[]) {
    this._filters = filters;
  }
  get filters(): DashboardMenuFilterModel[] {
    return this._filters;
  }

  @Input()
  set csv(csv: boolean) {
    this._csv = csv;
  }
  get csv(): boolean {
    return this._csv;
  }

  @Input()
  set service(service: boolean) {
    this._service = service;
  }
  get service(): boolean {
    return this._service;
  }

  @Output()
  dataLoaded = new EventEmitter();

  @Output()
  loading = new EventEmitter();

  @ViewChild("csvFileInput")
  private _csvFileInput;

  constructor(private _dataService: DataService) { }

  ngOnInit() {
  }

  private _handleLoadService(event): void {
    this.loading.emit(true);
    this._dataService.loadService('urlbase64', this.handleDataLoaded.bind(this));
  }

  private _handleLoadCsv(event): void {
    this.loading.emit(true);
    let fileInput = this._csvFileInput.nativeElement;
    if (fileInput.files && fileInput.files[0]) {
      let file = fileInput.files[0];
      this._dataService.loadCsv(file, this.handleDataLoaded.bind(this));
    }
  }

  handleDataLoaded(type: string, identifier: string) {
    this.dataLoaded.emit( { type: type, identifier: identifier } );
  }

}
