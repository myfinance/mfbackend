/**
 * Representation of a dashboards layout information.
 * Used to store and retrieve layout configuration from local storage.
 */
export interface DashboardLayoutModel {
  /**
   * Unique identifier for a widget on the dashboard.
   */
  uuid: string;

  x: number;
  y: number;
  cols: number;
  rows: number;

  /**
   * True if the widget is draggable.
   */
  dragEnabled: boolean;

  /**
   * True if the widget is resizable.
   */
  resizeEnabled: boolean;
}
