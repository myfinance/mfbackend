import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {DashboardDataLoadedEventModel} from "../../shared/models/dashboard-data-loaded-event.model";
import {DataService} from "../../shared/services/data.service";
import {DcService} from "../../../widget/services/dc.service";

@Component({
  selector: 'app-generic-dashboard-view',
  templateUrl: './generic-dashboard-view.component.html',
  styleUrls: ['./generic-dashboard-view.component.scss']
})
export class GenericDashboardViewComponent implements OnInit, OnDestroy {

  private _loading: boolean = false;
  private _dataLoaded: boolean = false;
  private _preparing: boolean = false;
  private _config;
  private _routeSubscription;

  constructor(private _dataService: DataService, private _dcService: DcService, private _route: ActivatedRoute) { }

  ngOnInit() {
    this._routeSubscription = this._route.data
      .subscribe(data => this._config = data.config);
  }

  /**
   * Handler to react on loading events.
   * @param loading Boolean value indicating if the dashboard is loading.
   */
  handleLoading(loading: boolean): void {
    this._loading = loading;
  }

  /**
   * Handler to react on data change.
   * @param event The fired event.
   */
  handleDataLoaded(event: DashboardDataLoadedEventModel): void {
    let data;
    if(event.type == 'csv') {
      data = this._dataService.getData(event.identifier);
    } else if(event.type == 'service') {
      console.log('Service loaded:', event.identifier);
    }

    this._loading = false;
    this._dataLoaded = true;
    this._preparing = true;

    this._dcService.load(this._config, data, this.dashboardLoaded.bind(this));
  }

  dashboardLoaded(): void {
    this._preparing = false;
  }

  ngOnDestroy() {
    this._routeSubscription.unsubscribe();
  }

}
