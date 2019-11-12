import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
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
  budgetGroup: Instrument;


  constructor(private formBuilder: FormBuilder, private instrumentservice: InstrumentService) { }

  ngOnInit() {
      this.instrumentForm = this.formBuilder.group({
        description: ['', Validators.required],
        instrumentType: [InstrumentTypeEnum.Giro, Validators.required],
        budgetGroups: this.formBuilder.array([this.budgetGroups])
      });

    if(this.instrumentservice.getIsInit()){
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
    this.instrumentForm.patchValue({'budgetGroup': this.budgetGroups})
    if(this.budgetGroups.length>0) { this.budgetGroup = this.budgetGroups[0] }
  }

  onSubmit() {
    console.log(this.instrumentForm)
    if (this.instrumentForm.value.instrumentType === InstrumentTypeEnum.Giro) {
      this.instrumentservice.saveGiro(this.instrumentForm.value.description)
    } else if (this.instrumentForm.value.instrumentType === InstrumentTypeEnum.Budget) {
      this.instrumentservice.saveBudget(this.instrumentForm.value.description, this.budgetGroup.instrumentid)
    }
  }

  ngOnDestroy(): void {
  }
}
