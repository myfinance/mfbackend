import {Component, OnInit, ViewChild} from '@angular/core';
import {Instrument} from "../../../../../myfinance-tsclient-generated";
import {NgForm} from "@angular/forms";
import {TransactionService} from "../../../transactioneditor/services/transaction.service";
import InstrumentTypeEnum = Instrument.InstrumentTypeEnum;
import {InstrumentService} from "../../services/instrument.service";

@Component({
  selector: 'app-instrumentinputform',
  templateUrl: './instrumentinputform.component.html',
  styleUrls: ['./instrumentinputform.component.scss']
})
export class InstrumentinputformComponent implements OnInit {
  instrumentTypes: InstrumentTypeEnum[] = [InstrumentTypeEnum.Tenant, InstrumentTypeEnum.Giro, InstrumentTypeEnum.Budget];
  instrumentType: InstrumentTypeEnum
  @ViewChild('f')
  form:NgForm;
  desc:string;


  constructor(private instrumentservice: InstrumentService) { }

  ngOnInit() {
    InstrumentTypeEnum[Symbol.iterator]
  }


  onSubmit(){
    console.log(this.form)
    if(this.instrumentType == InstrumentTypeEnum.Tenant){
      this.instrumentservice.saveTenant(this.desc)
    }
    this.form.reset();
  }
}
