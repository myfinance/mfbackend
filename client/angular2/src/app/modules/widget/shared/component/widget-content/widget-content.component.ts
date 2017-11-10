import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-widget-content',
  templateUrl: './widget-content.component.html',
  styleUrls: ['./widget-content.component.scss']
})
export class WidgetContentComponent implements OnInit {

  private _title: string;
  private _hideHeader: boolean;
  
  @Input()
  set title(title: string) {
    this._title = title;
  }
  get title() {
    return this._title;
  }

  @Input()
  set hideHeader(hideHeader: boolean) {
    this._hideHeader = hideHeader;
  }
  get hideHeader() {
    return this._hideHeader;
  }

  constructor() { }

  ngOnInit() {
  }

}
