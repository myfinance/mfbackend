import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DimmerComponent } from './components/dimmer/dimmer.component';

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [
    DimmerComponent
  ],
  exports: [
    DimmerComponent
  ]
})
export class MyFinanceCommonModule { }
