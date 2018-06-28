import { Injectable } from '@angular/core';
import {ConfigModel} from "../models/config.model";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class ConfigService {

  private _config: ConfigModel;

  constructor(private _http: HttpClient) { }

  /**
   * Loads the configuration.
   */
  load(): void {
    this._config = null;

    this._http
      .get('assets/config.json')
      .subscribe((data: ConfigModel) => {
        this._config = data;

        // Check if zone is saved in local storage.
        // Set the current zone to the saved zone or else
        // set it to the default zone in the configuration.
        let zone = localStorage.getItem('zone');
        if(zone) {
          this.setCurrentZone(zone);
        } else {
          this.setCurrentZone(data.defaultZone);
        }
      });
  }

  get(property: string): any {
    let value = this._config;
    for(let p of property.split('.')) {
      value = value[p];
    }
    return value;
  }

  setCurrentZone(identifier: string): void {
    for(let zone of this._config.zones) {
      if(zone.identifier == identifier) {
        this._config.currentZone = zone;
        // Additionally save the zone in the local storage.
        localStorage.setItem('zone', identifier);
      }
    }
  }
}
