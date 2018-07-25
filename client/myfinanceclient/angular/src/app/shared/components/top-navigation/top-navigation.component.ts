import { Component, OnInit } from '@angular/core';
import {ConfigService} from "../../services/config.service";
import {MyFinanceDataService} from "../../services/myfinance-data.service";
import {InstrumentListModel, StringListModel} from "../../../modules/myfinance-tsclient-generated";

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

  handleEnvSelect(env: string): void {
    this.configService.setCurrentEnv(env)
  }

  private _updateCurrentZone(): void {
    try {
      this.currentZone = this.configService.get("currentZone").name;
    } catch(e) {
      setTimeout(this._updateCurrentZone.bind(this), 1000);
    }
  }
}