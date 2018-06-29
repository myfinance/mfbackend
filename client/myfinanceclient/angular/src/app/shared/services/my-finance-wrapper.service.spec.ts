import { TestBed, inject } from '@angular/core/testing';

import { MyFinanceWrapperService } from './my-finance-wrapper.service';

describe('MyFinanceWrapperService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [MyFinanceWrapperService]
    });
  });

  it('should be created', inject([MyFinanceWrapperService], (service: MyFinanceWrapperService) => {
    expect(service).toBeTruthy();
  }));
});
