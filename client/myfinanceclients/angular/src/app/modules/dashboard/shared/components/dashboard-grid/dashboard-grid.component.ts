import { Component, OnInit, ChangeDetectionStrategy, Input } from '@angular/core';
import { Subject } from 'rxjs/Subject';
import { GridsterConfig, GridsterItem }  from 'angular-gridster2';
import { UUID } from 'angular2-uuid';

import { DefaultOptions } from './default-options';

@Component({
  selector: 'app-dashboard-grid',
  templateUrl: './dashboard-grid.component.html',
  styleUrls: ['./dashboard-grid.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class DashboardGridComponent implements OnInit {

  _options: GridsterConfig = {};
  _dashboard: Array<GridsterItem> = [];

  @Input()
  config

  private _resizedSubject: Subject<any> = new Subject();

  constructor() { }

  static eventStop(item, itemComponent, event): void {
    console.info('eventStop', item, itemComponent, event);
  }

  itemChange(item, itemComponent): void {
    console.info('itemChanged', item, itemComponent);
  }

  itemResize(item, itemComponent): void {
    console.info('itemResized', item, itemComponent);
    this._resizedSubject.next(item.widget.config.uuid);
  }

  itemInit(item, itemComponent): void {
    console.info('itemInitialized', item, itemComponent);
  }

  itemRemoved(item, itemComponent): void {
    console.info('itemRemoved', item, itemComponent);
  }

  emptyCellClick(event, item): void {
    console.info('empty cell click', event, item);
    this._dashboard.push(item);
  }

  ngOnInit() {
    Object.assign(this._options, DefaultOptions, {
      gridType: this.config.grid,
      itemChangeCallback: this.itemChange.bind(this),
      itemResizeCallback: this.itemResize.bind(this),
      itemInitCallback: this.itemInit.bind(this),
      itemRemovedCallback: this.itemRemoved.bind(this),
      emptyCellClickCallback: this.emptyCellClick.bind(this),
      emptyCellContextMenuCallback: this.emptyCellClick.bind(this),
      emptyCellDropCallback: this.emptyCellClick.bind(this),
      emptyCellDragCallback: this.emptyCellClick.bind(this),
      api: {
          resize: DashboardGridComponent.eventStop,
          optionsChanged: DashboardGridComponent.eventStop,
          getNextPossiblePosition: DashboardGridComponent.eventStop,
      }
    });

    this._buildDashboard();
  }

  changedOptions(): void {
    this._options.api.optionsChanged();
  }

  removeItem(item): void {
    this._dashboard.splice(this._dashboard.indexOf(item), 1);
  }

  addItem(): void {
    this._dashboard.push({});
  }

  private _buildDashboard(): void {
    for(let widget of this.config.widgets) {
      this._generateUUIDs(widget);

      this._dashboard.push({
        x: widget.layout.x,
        y: widget.layout.y,
        cols: widget.layout.cols,
        rows: widget.layout.rows,
        widget: widget
      });
    };
  }

  private _generateUUIDs(widget: any): void {
    widget.config.uuid = UUID.UUID();

    if(widget.config.tabs) {
      for(let tab of widget.config.tabs) {
        tab.config.uuid = UUID.UUID();
      }
    }
  }

}
