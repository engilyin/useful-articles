import { TestBed } from '@angular/core/testing';

import { AddNewUserService } from './add-new-user.service';

describe('AddNewUserService', () => {
  let service: AddNewUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AddNewUserService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
