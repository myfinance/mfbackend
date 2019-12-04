import { Component } from '@angular/core';
import {DashboardService} from "../../services/dashboard.service";


@Component({
  selector: 'app-dashboard-content',
  templateUrl: './dashboard-content.component.html',
  styleUrls: ['./dashboard-content.component.scss']
})
export class DashboardContentComponent {

  constructor(public dashboardService: DashboardService) { }

}
