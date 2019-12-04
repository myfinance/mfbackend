import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import InstrumentTypeEnum = Instrument.InstrumentTypeEnum;
import {InstrumentService} from '../../services/instrument.service';
import {Instrument} from '../../../../../myfinance-tsclient-generated';

@Component({
  selector: 'app-instrumentinputform',
  templateUrl: './instrumentinputform.component.html',
  styleUrls: ['./instrumentinputform.component.scss']
})
export class InstrumentinputformComponent implements OnInit, OnDestroy {
  instrumentTypes: InstrumentTypeEnum[] = [InstrumentTypeEnum.Giro, InstrumentTypeEnum.Budget];
  budgetGroups: Instrument[] = [];
  instrumentForm: FormGroup;


  constructor(private formBuilder: FormBuilder, private instrumentservice: InstrumentService) { }

  ngOnInit() {
      this.instrumentForm = this.formBuilder.group({
        description: ['', Validators.required],
        instrumentType: [InstrumentTypeEnum.Giro, Validators.required],
        budgetGroup: [null, [Validators.required, this.isBudgetGroupNecessary.bind(this)]]
      });

    if (this.instrumentservice.getIsInit()) {
      this.loadData();
    }
    this.instrumentservice.instrumentSubject.subscribe(
      () => {
        this.loadData();
      }
    )
  }

  loadData(): void {
    this.budgetGroups = this.instrumentservice.getBudgetGroups();

    if (this.budgetGroups.length > 0) {
      this.instrumentForm.controls['budgetGroup'].setValue(this.budgetGroups[0], {onlySelf: true});
    }
  }

  onSubmit() {
    if (this.instrumentForm.value.instrumentType === InstrumentTypeEnum.Giro) {
      this.instrumentservice.saveGiro(this.instrumentForm.value.description)
    } else if (this.instrumentForm.value.instrumentType === InstrumentTypeEnum.Budget) {
      this.instrumentservice.saveBudget(this.instrumentForm.value.description, this.instrumentForm.value.budgetGroup.instrumentid)
    }
  }

  isBudgetGroupNecessary(control: FormControl): {[s: string]: boolean} {
    if (this.instrumentForm == null) { return null; }
    if (this.instrumentForm.value == null) { return null; }
    if (this.instrumentForm.value.instrumentType === InstrumentTypeEnum.Budget && this.instrumentForm.value.budgetGroup == null) {
      return {'BudgetGroup is necessary': true};
    } else { return null; }
  }

  ngOnDestroy(): void {
  }
}
