import { Injectable, OnDestroy } from '@angular/core';

/**
 * Data service to load, cache and provide dashboard data.
 * TODO: Needs a proper caching strategy.
 */
@Injectable()
export class DataService implements OnDestroy {

  private _data: any[] = [];

  /**
   * Returns data for the given identifier.
   * @param identifier The identifier used to retrieve the data.
   */
  getData(identifier: string): any {
    return this._data[identifier];
  }

  constructor() { }

  /**
   * Loads data from the ccr service into the data cache.
   * @param url
   * @param handler
   */
  loadData(url: string, handler: any): void {
    let identifier = btoa(url)
    if(this._data[identifier]) {
      handler('ccr', identifier);
    } else {
      this._clearCache(); //TODO Caching strategy
      this._data[identifier] = identifier;
      handler('ccr', identifier);
    }
  }

  /**
   * Resets the cache.
   */
  private _clearCache(): void {
    this._data = [];
  }

  ngOnDestroy() {
    this._clearCache();
  }

}
