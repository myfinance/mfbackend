import { Injectable, OnDestroy } from '@angular/core';

import { CsvService } from './csv.service';

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

  constructor(private _csvService: CsvService) { }

  /**
   * Loads a CSV file into the data cache.
   * @param file The CSV file to load.
   * @param handler The handler to call upon finish.
   */
  loadCsv(file: any, handler: any): void {
    let identifier = btoa(file.name);
    if(this._data[identifier]) {
      handler('csv', identifier);
    } else {
      this._clearCache(); //TODO Caching strategy
      this._csvService.parse(file, result => {
        this._data[identifier] = result.data;
        handler('csv', identifier);
      });
    }
  }

  /**
   * Loads data from the ccr service into the data cache.
   * @param url
   * @param handler
   */
  loadService(url: string, handler: any): void {
    let identifier = btoa(url)
    if(this._data[identifier]) {
      handler('service', identifier);
    } else {
      this._clearCache(); //TODO Caching strategy
      this._data[identifier] = identifier;
      handler('service', identifier);
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
