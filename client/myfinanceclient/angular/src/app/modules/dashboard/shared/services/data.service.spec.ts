import { TestBed, inject } from '@angular/core/testing';

import { DataService } from './data.service';
import { CsvService } from './csv.service';

describe('DataService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DataService, CsvService]
    });
  });

  it('should be created', inject([DataService], (service: DataService) => {
    expect(service).toBeTruthy();
  }));
});