import {Injectable, OnDestroy} from '@angular/core';
import {ToastrService} from 'ngx-toastr';
import {Subject} from 'rxjs/Rx';


@Injectable()
export class DashboardService implements OnDestroy {

  /**
   * True if the dashboard is loading, else false.
   */
  loading = false;

  /**
   * True if data was loaded, else false.
   */
  dataLoaded = false;

  /**
   * True if the dashboard is e.g. rendering items.
   */
  preparing = false;


  constructor(public toastr: ToastrService) {
  }

  /**
   * Default handler if data could not be loaded.
   */
  handleDataNotLoaded(error: any) {
    this.loading = false;
    console.error(error);
    this.toastr.error('Daten konnten nicht geladen werden.');
  }

  handleLoading(): void {
    this.loading = true;
    this.dataLoaded = false;
    this.preparing = false;
  }

  handleDataPreparing(): void {
    this.loading = false;
    this.dataLoaded = false;
    this.preparing = true;
  }

  handleDataLoaded(): void {
    this.loading = false;
    this.dataLoaded = true;
    this.preparing = false;
  }

  /**
   * Resets the current state of the widget.
   */
  reset(): void {
    this.loading = false;
    this.dataLoaded = false;
    this.preparing = false;
  }

  ngOnDestroy() {
    this.reset();
  }
}
