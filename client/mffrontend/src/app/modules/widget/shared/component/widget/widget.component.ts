import { Component, OnInit, Input, OnDestroy } from '@angular/core';
import { Subject } from 'rxjs/Subject';

import 'rxjs/add/operator/takeWhile';

@Component({
  selector: 'app-widget',
  templateUrl: './widget.component.html',
  styleUrls: ['./widget.component.scss']
})
export class WidgetComponent implements OnInit, OnDestroy {

  private _alive = true;

  @Input() title: string;
  @Input() uuid: string;
  @Input() hideHeader = false;

  @Input() resized: Subject<any>;

  constructor() { }

  ngOnInit() {
    if (this.resized) {
      this.resized
        .takeWhile(() => this._alive)
        .subscribe(event => {
          if (this.uuid === event.uuid) {
            this.resize(event.component);
          }
        });
    }
  }

  resize(component: any): void {
    if (typeof (Event) === 'function') {
      // modern browsers
      window.dispatchEvent(new Event('resize'));
    } else {
      // for IE and other old browsers
      const evt = window.document.createEvent('UIEvents');
      evt.initUIEvent('resize', true, false, window, 0);
      window.dispatchEvent(evt);
    }
  }

  ngOnDestroy() {
    this._alive = false;
  }

}
