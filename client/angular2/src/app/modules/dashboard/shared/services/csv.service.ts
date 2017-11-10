import { Injectable } from '@angular/core';
import * as Papa  from 'papaparse';

/**
 * CSV Parser service.
 */
@Injectable()
export class CsvService {

  constructor() { }

  /**
   * Parses the given CSV file and calls the callback method after it.
   * @param file The CSV file to parse.
   * @param callback The callback method to call after parsing.
   */
  parse(file: any, callback: any): void {
    Papa.parse(file, {
      header: true,
      skipEmptyLines: true,
      comments: '#',
      beforeFirstChunk: function( chunk ) {
        let rows = chunk.split( /\r\n|\r|\n/ );
        let headings = rows[0].split( ',' );
        for(let i = 0; i < headings.length; i++) {
          headings[i] = headings[i].toLowerCase().replace(' ', '_');
        }
        rows[0] = headings.join();
        return rows.join( '\n' );
      },
      complete: callback
    });
  }

}
