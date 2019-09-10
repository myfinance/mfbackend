import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import InstrumentTypeEnum = Instrument.InstrumentTypeEnum;
import {InstrumentService} from "../../services/instrument.service";
import {Instrument} from "../../../../../myfinance-tsclient-generated";

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
      'budgetGroup': new FormControl()
    });
    this.instrumentservice.instrumentSubject.subscribe(
      () => {
        this.loadData()}
    )
  }

  private loadData(): void {
    this.budgetGroups = this.instrumentservice.getBudgetGroups();
  }

  onSubmit() {
    console.log(this.instrumentForm)
    if(this.instrumentForm.value.instrumentType === InstrumentTypeEnum.Tenant) {
      this.instrumentservice.saveInstrument(this.instrumentForm.value.description)
    }
  }
}
