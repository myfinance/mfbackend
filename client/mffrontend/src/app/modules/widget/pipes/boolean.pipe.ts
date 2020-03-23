import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'boolean'
})
export class BooleanPipe implements PipeTransform {

  transform(value: any, args?: any): any {
    if (value === 1 || value === true || value === '1' || value === 'true' || value === 'yes') return 'Ja';
    if (value === 0 || value === false || value === '0' || value === 'false' || value === 'no') return 'Nein';
    else return null;
  }

}
