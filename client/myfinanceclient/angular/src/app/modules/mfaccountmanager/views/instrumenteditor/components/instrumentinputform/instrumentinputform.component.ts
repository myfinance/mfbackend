import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import InstrumentTypeEnum = Instrument.InstrumentTypeEnum;
import {InstrumentService} from '../../services/instrument.service';
import {Instrument} from '../../../../../myfinance-tsclient-generated';

@Component({
  selector: 'app-instrumentinputform',
  templateUrl: './instrumentinputform.component.html',
  styleUrls: ['./instrumentinputform.component.scss']
})
export class InstrumentinputformComponent implements OnInit {
  instrumentTypes: InstrumentTypeEnum[] = [InstrumentTypeEnum.Giro, InstrumentTypeEnum.Budget];
  budgetGroups: Instrument[];
  instrumentForm: FormGroup;
  budgetGroup: Instrument;

  constructor(private instrumentservice: InstrumentService) { }

  ngOnInit() {

    this.instrumentForm = new FormGroup({
      'description': new FormControl(null, Validators.required),
      'instrumentType': new FormControl(InstrumentTypeEnum.Giro),
      'budgetGroup': new FormControl(null, [Validators.required, this.isBudgetGroupNecessary.bind(this)])
    });
    this.instrumentservice.instrumentSubject.subscribe(
      () => {
        this.loadData()}
    )
  }

  private loadData(): void {
    this.budgetGroups = this.instrumentservice.getBudgetGroups();
  }

  isBudgetGroupNecessary(control: FormControl): {[s: string]: boolean} {
    if (this.instrumentForm == null) { return null; }
    if (this.instrumentForm.value == null) { return null; }
    if (this.instrumentForm.value.instrumentType === InstrumentTypeEnum.Budget && this.budgetGroup == null) {
      return {'BudgetGroup is necessary': true};
    } else { return null; }
  }

  onSubmit() {
    console.log(this.instrumentForm)
    if (this.instrumentForm.value.instrumentType === InstrumentTypeEnum.Giro) {
       this.instrumentservice.saveGiro(this.instrumentForm.value.description)
    } else if (this.instrumentForm.value.instrumentType === InstrumentTypeEnum.Budget) {
      this.instrumentservice.saveBudget(this.instrumentForm.value.description, this.budgetGroup.instrumentid)
    }
  }
}
