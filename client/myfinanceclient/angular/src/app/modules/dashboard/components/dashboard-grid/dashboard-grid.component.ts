import { Component, OnInit, Input, ChangeDetectorRef, ContentChildren, TemplateRef, QueryList, Output, EventEmitter, AfterViewInit } from '@angular/core';
import { Subject } from 'rxjs/Subject';

import { GridsterConfig } from 'angular-gridster2';

import { DefaultOptions } from './default-options';
import { DashboardLayoutModel } from '../../models/dashboard-layout.model';
import { DashboardModel } from '../../models/dashboard.model';
import { DashboardItemModel } from '../../models/dashboard-item.model';
import { DashboardGridInterface } from '../../dashboard-grid.interface';
import { DashboardWidgetDirective } from '../../directives/dashboard-widget.directive';
import {gridTypes} from 'angular-gridster2/lib/gridsterConfig.interface';

@Component({
  selector: 'app-dashboard-grid',
  templateUrl: './dashboard-grid.component.html',
  styleUrls: ['./dashboard-grid.component.scss']
})
export class DashboardGridComponent implements DashboardGridInterface, OnInit, AfterViewInit {

  resizedSubject: Subject<any> = new Subject();
  options: GridsterConfig;
  dashboard: Array<DashboardItemModel>;

  @Input() title: string;
  @Input() view: gridTypes;

  @Input() widgets: {
    uuid: string,
    title: string,
    x: number,
    y: number,
    cols: number,
    rows: number,
    draggable: boolean,
    resizable: boolean
  }[] = [];

  @Output() ready: EventEmitter<boolean> = new EventEmitter<boolean>();

  @ContentChildren(DashboardWidgetDirective) templates: QueryList<DashboardWidgetDirective>;

  constructor(
    public changeDetectorRef: ChangeDetectorRef) { }

  eventStop(item, itemComponent, event): void {
    if (item) { this.resizedSubject.next({ uuid: item.widget.uuid, component: itemComponent }); }
  }

  itemChange(item, itemComponent): void {
    this._saveDashboard();
  }

  itemResize(item, itemComponent): void {
    this.resizedSubject.next({ uuid: item.widget.uuid, component: itemComponent });
  }

  itemInit(item, itemComponent): void { }

  itemRemoved(item, itemComponent): void { }

  emptyCellClick(event, item): void {
    this.dashboard.push(item);
  }

  ngOnInit() {
    this.setDefaultOptions();
    this.refresh();
  }

  ngAfterViewInit() {
    this.ready.emit(true);
  }

  setDefaultOptions(): void {
    this.options = {};
    Object.assign(this.options, DefaultOptions, {
      gridType: this.view,
      itemChangeCallback: this.itemChange.bind(this),
      itemResizeCallback: this.itemResize.bind(this),
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
        },
        stop: this.eventStop.bind(this)
      },
      itemInitCallback: this.itemInit.bind(this),
      itemRemovedCallback: this.itemRemoved.bind(this),
      emptyCellClickCallback: this.emptyCellClick.bind(this),
      emptyCellContextMenuCallback: this.emptyCellClick.bind(this),
      emptyCellDropCallback: this.emptyCellClick.bind(this),
      emptyCellDragCallback: this.emptyCellClick.bind(this),
      api: {
        resize: this.eventStop.bind(this),
        optionsChanged: this.eventStop.bind(this),
        getNextPossiblePosition: this.eventStop.bind(this)
      }
    });
  }

  changeOption(key: string, value: any): void {
    this.options[key] = value;
    this.changedOptions();
    this._saveDashboard();
  }

  changedOptions(): void {
    this.options.api.optionsChanged();
  }

  removeItem(item): void {
    this.dashboard.splice(this.dashboard.indexOf(item), 1);
  }

  addItem(): void {
    this.dashboard.push(<DashboardItemModel>{});
  }

  refresh(): void {
    this.changeDetectorRef.detectChanges();
    this._buildDashboard();
    this.changeDetectorRef.detectChanges();
  }

  resize(): void {
    if (this.options && this.options.api) { this.options.api.resize(); }
  }

  private _buildDashboard(): void {
    this.dashboard = [];
    const savedDashboard = this._loadDashboard();

    if (savedDashboard && savedDashboard.options) {
      Object.assign(this.options, savedDashboard.options);
      if (this.options.api) { this.options.api.optionsChanged(); }
    }

    for (const widget of this.widgets) {
      let savedWidget;
      if (savedDashboard && savedDashboard.widgets) {
        for (const sw of savedDashboard.widgets) {
          if (sw.uuid === widget.uuid) {
            savedWidget = sw;
            break;
          }
        }
      }

      this.dashboard.push({
        x: savedWidget ? savedWidget.x : widget.x,
        y: savedWidget ? savedWidget.y : widget.y,
        cols: savedWidget ? savedWidget.cols : widget.cols,
        rows: savedWidget ? savedWidget.rows : widget.rows,
        dragEnabled: savedWidget ? savedWidget.dragEnabled : (widget.draggable === false) ? false : true,
        resizeEnabled: savedWidget ? savedWidget.resizeEnabled : (widget.resizable === false) ? false : true,
        widget: widget
      });
    }
  }

  resetDashboard(): void {
    this.setDefaultOptions();
    this.changeDetectorRef.detectChanges();

    for (const item of this.dashboard) {
      const widget = this._getWidgetByUuid(item.widget.uuid);
      item.x = widget.x;
      item.y = widget.y;
      item.cols = widget.cols;
      item.rows = widget.rows;
      item.dragEnabled = (widget.draggable === false) ? false : true;
      item.resizeEnabled = (widget.resizable === false) ? false : true;
      item.widget = widget;
    }

    this.changeDetectorRef.detectChanges();
    this.changedOptions();

    localStorage.removeItem(this.title.toLowerCase().replace(' ', '_'));
  }

  private _getWidgetByUuid(uuid: string): any {
    return this.widgets.find(w => w.uuid === uuid);
  }

  getTemplateByUuid(uuid: string): TemplateRef<any> {
    if (this.templates == null) { return null; }
    return this.templates.toArray().find(t => t.uuid === uuid).template;
  }

  private _saveDashboard(): void {
    const dashboardLayout: DashboardLayoutModel[] = [];
    for (const item of this.dashboard) {
      dashboardLayout.push(<DashboardLayoutModel>{
        uuid: item.widget.uuid,
        x: item.x,
        y: item.y,
        cols: item.cols,
        rows: item.rows,
        dragEnabled: item.dragEnabled,
        resizeEnabled: item.resizeEnabled
      });
    }

    const dashboard = <DashboardModel>{
      options: Object.assign({}, this.options, { api: undefined }),
      widgets: dashboardLayout
    };
    localStorage.setItem(this.title.toLowerCase().replace(' ', '_'), JSON.stringify(dashboard));
  }

  private _loadDashboard(): DashboardModel | null {
    const dashboard = localStorage.getItem(this.title.toLowerCase().replace(' ', '_'));
    return dashboard != null ? JSON.parse(dashboard) : null;
  }
}
