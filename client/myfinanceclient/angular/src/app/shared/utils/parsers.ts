import { timeParse, utcParse } from 'd3-time-format';
import { isNumeric } from 'rxjs/util/isNumeric';
import * as moment from 'moment';

/**
 * Parses a date in the format Y-m-d (2017-11-17) or d.m.Y (17.11.2017) and returns a date object.
 * @param value The date object representing the given string.
 */
const dateParser = timeParse('%Y-%m-%d');
const alternateDateParser = timeParse('%Y%m%d');
const germanDateParser = timeParse('%d.%m.%Y');
const alternateGermanDateParser = timeParse('%d/%m/%Y');
const isoDateTimeParser = utcParse('%Y-%m-%dT%H:%M:%S.%L');
const germanDateTimeParser = utcParse('%d.%m.%YT%H:%M:%S.%L');

export function parseDate(value: string): Date {
  let date = dateParser(value);
  if (!date) {
    date = alternateDateParser(value);
  }
  if (!date) {
    date = germanDateParser(value);
  }
  if (!date) {
    date = alternateGermanDateParser(value);
  }
  return date;
}

/**
 * Parses a date in the format Y-m-dTH:M:S:L (2017-11-17T10:57:10.000) or d.m.YTH:M:S:L (17.11.2017T10:57:10.000) and returns a date object.
 * @param value The date object representing the given string.
 */
export function parseDatetime(value: string): Date {
  if (!value) return null;

  let date = isoDateTimeParser(value.replace(' ', 'T'));
  if (!date) {
    date = germanDateTimeParser(value.replace(' ', 'T'));
  }
  return date;
}

export function parseBoolean(value: any) {
  if (value === 1 || value === true || value === '1' || value === 'true' || value === 'yes') return true;
  if (value === 0 || value === false || value === '0' || value === 'false' || value === 'no') return false;
  else return null;
}

/**
 * Parses the given data row and tries to identify column types.
 * @param row The row to parse and identify.
 */
export function parseDataColumns(row: any): any {
  return Object.keys(row).map(column => {
    let type = 'string';
    if (moment(row[column]).isValid() && !isNumeric(row[column])) {
      if (row[column].toString().indexOf(':') > -1) type = 'datetime';
      else type = 'date';
    } else if (isNumeric(row[column])) {
      if (
        (row[column].toString().indexOf('0') !== 0 || (row[column].toString().indexOf('0') === 0 && Math.abs(+row[column]) < 1)) &&
        column.indexOf('gpnr') === -1 // Ignore GpNr columns which do not contain numbers escaped by leading zeros.
      ) {
        type = 'double';
      } else {
        type = 'string';
      }
    }

    return { key: column, type: type };
  });
}
