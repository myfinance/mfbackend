import { ViewChild, ChangeDetectorRef } from '@angular/core';

import { DashboardGridComponent } from './components/dashboard-grid/dashboard-grid.component';
import { DashboardGridInterface } from './dashboard-grid.interface';

/**
 * Base class for dashboards.
 * A dashboard is defined as a component which contains a grid
 * with one or more widgets as items.
 */
export abstract class AbstractDashboard {

  /**
   * The grid of the dashboard.
   */
  @ViewChild(DashboardGridComponent, {static: false})
  grid: DashboardGridInterface;

  protected constructor(public changeDetectorRef: ChangeDetectorRef) { }


  /**
   * Handler to react on refresh events.
   * Refresh the grid when called.
   * @param event The event.
   */
  handleDashboardRefresh(event: any): void {
    if (this.grid) { this.grid.refresh(); }
  }

  /**
   * Handler to react on dashboard resizes.
   * Calls resize on the grid.
   * @param event The event.
   */
  handleDashboardResize(event: any): void {
    if (this.grid) { this.grid.resize(); }
  }

  /**
   * Handler to react on dashboard mode changes.
   * Sets the gridType of the underlying grid to the given mode.
   * @param mode The mode the grid should be set to.
   */
  handleDashboardMode(mode: any): void {
    if (this.grid) { this.grid.changeOption('gridType', mode); }
  }

  /**
   * Handler to react on dashboard reset.
   * Resets the dashboards configuration to the default grid options.
   * @param event The event.
   */
  handleDashboardReset(event: any): void {
    if (this.grid) { this.grid.resetDashboard(); }
  }




}
