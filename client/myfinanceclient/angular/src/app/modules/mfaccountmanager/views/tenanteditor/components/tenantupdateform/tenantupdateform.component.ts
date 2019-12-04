import {Component, OnDestroy, OnInit} from '@angular/core';
import {Instrument} from '../../../../../myfinance-tsclient-generated';
import {TenantService} from '../../services/tenant.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-tenantupdateform',
  templateUrl: './tenantupdateform.component.html',
  styleUrls: ['./tenantupdateform.component.scss']
})
export class TenantupdateformComponent  implements OnInit, OnDestroy {
  noTenantSelected = true;
  selectedTenant: Instrument;
  tenantForm: FormGroup;

  constructor(private tenantservice: TenantService) { }

  ngOnInit() {
    this.tenantForm = new FormGroup({
      'description': new FormControl(null, Validators.required),
      'active': new FormControl(null, Validators.required)
    });
    this.tenantservice.selectedinstrumentSubject.subscribe(
      () => {
        this.updateSelectedTenant()
      }
    )
  }

  updateSelectedTenant() {
    this.selectedTenant = this.tenantservice.getSelectedTenant()
    if (this.selectedTenant) {
      this.noTenantSelected = false;
      this.tenantForm.get('description').setValue(this.selectedTenant.description);
      this.tenantForm.get('active').setValue(this.selectedTenant.isactive);
    }

  }

  getSelectedTenantId(): number {
    if (!this.selectedTenant) { return 0; } else { return this.selectedTenant.instrumentid; }
  }

  onSubmit() {
    console.log(this.tenantForm);
    if(this.tenantForm.touched) {
      console.log('touched');
      this.tenantservice.updateTenant(this.getSelectedTenantId(), this.tenantForm.value.description, this.tenantForm.value.active);
    } else {
      console.log('untouched');
    }
  }

  ngOnDestroy(): void {
  }
}

