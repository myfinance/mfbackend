import { GridsterConfig }  from 'angular-gridster2';

/**
 * Default options to be used in a dashboard grid component.
 */
export const DefaultOptions: GridsterConfig = {
  gridType: 'fit',
  compactType: 'none',
  margin: 5,
  outerMargin: true,
  mobileBreakpoint: 640,
  minCols: 1,
  maxCols: 100,
  minRows: 1,
  maxRows: 100,
  maxItemCols: 100,
  minItemCols: 1,
  maxItemRows: 100,
  minItemRows: 1,
  maxItemArea: 2500,
  minItemArea: 1,
  defaultItemCols: 1,
  defaultItemRows: 1,
  fixedColWidth: 105,
  fixedRowHeight: 105,
  keepFixedHeightInMobile: false,
  keepFixedWidthInMobile: false,
  scrollSensitivity: 10,
  scrollSpeed: 20,
  enableEmptyCellClick: false,
  enableEmptyCellContextMenu: false,
  enableEmptyCellDrop: false,
  enableEmptyCellDrag: false,
  emptyCellDragMaxCols: 50,
  emptyCellDragMaxRows: 50,
  draggable: {
    delayStart: 0,
    enabled: true,
    ignoreContentClass: 'gridster-item-content',
    ignoreContent: true,
    dragHandleClass: 'drag-handler'
  },
  resizable: {
    delayStart: 0,
    enabled: true,
    handles: {
      s: true,
      e: true,
      n: true,
      w: true,
      se: true,
      ne: true,
      sw: true,
      nw: true
    }
  },
  swap: false,
  pushItems: true,
  disablePushOnDrag: false,
  disablePushOnResize: false,
  pushDirections: {north: true, east: true, south: true, west: true},
  pushResizeItems: false,
  displayGrid: 'onDrag&Resize',
  disableWindowResize: false
};