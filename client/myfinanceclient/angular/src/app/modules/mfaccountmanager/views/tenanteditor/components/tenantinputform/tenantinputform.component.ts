import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {TenantService} from '../../services/tenant.service';

@Component({
  selector: 'app-tenantinputform',
  templateUrl: './tenantinputform.component.html',
  styleUrls: ['./tenantinputform.component.scss']
})
export class TenantinputformComponent implements OnInit, OnDestroy {
  instrumentForm: FormGroup;

  constructor(private tenantservice: TenantService) { }

  ngOnInit() {
    this.instrumentForm = new FormGroup({
      'description': new FormControl(null, Validators.required)
    });

  }

  onSubmit() {
    console.log(this.instrumentForm)
      this.tenantservice.saveTenant(this.instrumentForm.value.description)
  }

  ngOnDestroy(): void {
  }
}
