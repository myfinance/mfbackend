import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'financialNumber'
})
export class FinancialNumberPipe implements PipeTransform {

  transform(value: number): string {
    if (Math.abs(value) >= 1e9) {
      return `${this._round(value / 1e9, 1).toLocaleString()} Mrd`;
    } else if (Math.abs(value) >= 1e6) {
      return `${this._round(value / 1e6, 1).toLocaleString()} Mio`;
    } else if (Math.abs(value) >= 1e3) {
      return `${this._round(value / 1e3, 1).toLocaleString()} Tsd`;
    } else {
      return this._round(value, 2).toLocaleString();
    }
  }

  private _round(number, precision) {
    let factor = Math.pow(10, precision);
    let tempNumber = number * factor;
    let roundedNumber = Math.round(tempNumber);
    return roundedNumber / factor;
  }

}
