import { TestBed, inject } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';

import { ToastrService } from 'ngx-toastr';

import { DashboardService } from './dashboard.service';
import { DataService } from './data.service';
import { AppService } from '../../../services/app.service';
import { CcrService } from '../../xva-common/services/ccr.service';

class DataServiceStub { }
class CcrServiceStub { }
class AppServiceStub { }
class ToastrServiceStub { }

describe('DashboardService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      providers: [
        DashboardService,
        { provide: ToastrService, useClass: ToastrServiceStub },
        { provide: DataService, useClass: DataServiceStub },
        { provide: CcrService, useClass: CcrServiceStub },
        { provide: AppService, useClass: AppServiceStub }
      ]
    });
  });

  it('should be created', inject([DashboardService], (service: DashboardService) => {
    expect(service).toBeTruthy();
  }));
});
