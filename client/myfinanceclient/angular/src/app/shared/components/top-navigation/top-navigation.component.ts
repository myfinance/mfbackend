import {Component, OnDestroy, OnInit} from '@angular/core';
import {ConfigService} from '../../services/config.service';
import {Instrument} from '../../../modules/myfinance-tsclient-generated';

@Component({
  selector: 'app-top-navigation',
  templateUrl: './top-navigation.component.html',
  styleUrls: ['./top-navigation.component.scss']
})
export class TopNavigationComponent implements OnInit, OnDestroy {

  _isCollapsed = true;
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

  handleTenantSelect(tenant: Instrument): void {
    this.configService.setCurrentTenant(tenant)
  }


  private _updateCurrentZone(): void {
    try {
      this.currentZone = this.configService.get('currentZone').name;
    } catch (e) {
      setTimeout(this._updateCurrentZone.bind(this), 1000);
    }
  }

  ngOnDestroy(): void {
  }
}
