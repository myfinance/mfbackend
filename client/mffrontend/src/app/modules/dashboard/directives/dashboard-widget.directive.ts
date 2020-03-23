import { Directive, Input, TemplateRef } from '@angular/core';

@Directive({
  selector: '*ng-template[appDashboardWidget]'
})
export class DashboardWidgetDirective {

  @Input() uuid: string;

  constructor(public template: TemplateRef<any>) { }

}
