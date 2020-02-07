import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {InstrumentService} from '../../services/instrument.service';
import {Instrument} from '../../../../../myfinance-tsclient-generated';

@Component({
  selector: 'app-instrumentupdateform',
  templateUrl: './instrumentupdateform.component.html',
  styleUrls: ['./instrumentupdateform.component.scss']
})
export class InstrumentupdateformComponent  implements OnInit, OnDestroy {
  instrumentForm: FormGroup;
  noInstrumentSelected = true;
  selectedInstrument: Instrument;

  constructor(private formBuilder: FormBuilder, private instrumentservice: InstrumentService) { }

  ngOnInit() {
    this.instrumentForm = this.formBuilder.group({
      description: ['', Validators.required],
      active: ['', Validators.required]
    });
    this.instrumentservice.selectedinstrumentSubject.subscribe(
      () => {
        this.updateSelectedInstrument()
      }
    )
  }

  updateSelectedInstrument() {
    this.selectedInstrument = this.instrumentservice.getSelectedInstrument()
    if (this.selectedInstrument) {
      this.noInstrumentSelected = false;
      this.instrumentForm.get('description').setValue(this.selectedInstrument.description);
      this.instrumentForm.get('active').setValue(this.selectedInstrument.isactive);
    }

  }

  getSelectedInstrumentId(): number {
    if (!this.selectedInstrument) { return 0; } else { return this.selectedInstrument.instrumentid; }
  }

  onSubmit() {
    console.log(this.instrumentForm);
    if (this.instrumentForm.touched) {
      console.log('touched');
      this.instrumentservice.updateInstrument(
        this.getSelectedInstrumentId(),
        this.instrumentForm.value.description,
        this.instrumentForm.value.active);
    } else {
      console.log('untouched');
    }
  }

  ngOnDestroy(): void {
  }
}
