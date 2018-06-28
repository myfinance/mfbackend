import { Component, OnInit } from '@angular/core';
import {ConfigService} from "../../services/config.service";

@Component({
  selector: 'app-top-navigation',
  templateUrl: './top-navigation.component.html',
  styleUrls: ['./top-navigation.component.scss']
})
export class TopNavigationComponent implements OnInit {

  _isCollapsed: boolean = true;
  currentZone: string;

  constructor(public configService: ConfigService) { }

  ngOnInit() {
    this._updateCurrentZone();
  }

  handleZoneSelect(identifier: string): void {
    this.configService.setCurrentZone(identifier);
    this._updateCurrentZone();
  }

  private _updateCurrentZone(): void {
    try {
      this.currentZone = this.configService.get("currentZone").name;
    } catch(e) {
      setTimeout(this._updateCurrentZone.bind(this), 1000);
    }
  }

}
