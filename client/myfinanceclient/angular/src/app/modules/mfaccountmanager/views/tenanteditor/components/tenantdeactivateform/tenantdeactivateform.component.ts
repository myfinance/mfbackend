import { Component, OnInit } from '@angular/core';
import {Instrument} from "../../../../../myfinance-tsclient-generated";
import {TenantService} from "../../services/tenant.service";

@Component({
  selector: 'app-tenantdeactivateform',
  templateUrl: './tenantdeactivateform.component.html',
  styleUrls: ['./tenantdeactivateform.component.scss']
})
export class TenantdeactivateformComponent implements OnInit {

  noTenantSelected:boolean = true;
  selectedTenant: Instrument;

  constructor(private tenantservice: TenantService) { }

  ngOnInit() {
    this.tenantservice.selectedinstrumentSubject.subscribe(
      () => {
        this.updateSelectedTenant()
      }
    )
  }

  updateSelectedTenant() {
    this.selectedTenant = this.tenantservice.getSelectedTenant()
    if (this.selectedTenant) this.noTenantSelected = false;
  }

  getSelectedTenantId() : number {
    if(!this.selectedTenant) return 0;
    else return this.selectedTenant.instrumentid;
  }

  onSubmit(){
    console.log("deactivate tenant")
    //this.instrumentservice.saveTenant(this.instrumentForm.value.description)
  }

}
