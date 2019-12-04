import { DashboardLayoutModel } from './dashboard-layout.model';

/**
 * Representation of a dashboard config.
 * Used to store and retrieve configuration from local storage.
 */
export interface DashboardModel {
  options: any;
  widgets: DashboardLayoutModel[];
}
