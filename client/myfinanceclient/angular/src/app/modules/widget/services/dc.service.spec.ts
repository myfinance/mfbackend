import { TestBed, inject } from '@angular/core/testing';

import { DcService } from './dc.service';

describe('DcService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DcService]
    });
  });

  it('should be created', inject([DcService], (service: DcService) => {
    expect(service).toBeTruthy();
  }));
});
