import { TestBed, inject } from '@angular/core/testing';

import { MockDataProviderService } from './mock-data-provider.service';

describe('MockDataProviderService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [MockDataProviderService]
    });
  });

  it('should be created', inject([MockDataProviderService], (service: MockDataProviderService) => {
    expect(service).toBeTruthy();
  }));
});
