import { Subject } from 'rxjs';

import { GridsterConfig } from 'angular-gridster2';

import { DashboardItemModel } from './models/dashboard-item.model';

/**
 * Base grid interface.
 * Needs to be implemented by explicit dashboard implementations,
 * which implements Gridster2.
 */
export interface DashboardGridInterface {

  /**
   * A subject which informs subscribers about a resizing event.
   */
  resizedSubject: Subject<any>;

  /**
   * The configuration for the Gridster2 component.
   */
  options: GridsterConfig;

  /**
   * An array of dashboard items of the current dashboard.
   */
  dashboard: Array<DashboardItemModel>;

  /**
   * Should implement functionality to refresh the dashboard.
   */
  refresh(): void;

  /**
   * Should implement functionality to resize the dashboard.
   */
  resize(): void;

  /**
   * Changes a grid option parameter.
   * @param key The parameter to change.
   * @param value The value it should be set to.
   */
  changeOption(key: string, value: any): void;

  /**
   * Should reset the dashboard by loading its state from the provided default grid options.
   */
  resetDashboard(): void;


}
