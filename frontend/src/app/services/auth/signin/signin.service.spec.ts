/*
 Copyright 2022-2025 engilyin

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

      https://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
*/
import { authorize } from "./../../../store/original-target/original-target.actions";
/*
 Copyright 2022 engilyin

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

      https://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

import { TestBed } from "@angular/core/testing";
import {
  HttpClientTestingModule,
  HttpTestingController,
} from "@angular/common/http/testing";

import { SigninService } from "./signin.service";
import { environment } from "@root/environments/environment";

describe("SigninService", () => {
  let service: SigninService;
  let httpController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    httpController = TestBed.inject(HttpTestingController);
    service = TestBed.inject(SigninService);
  });

  afterEach(() => {
    httpController.verify();
  });

  it("should be created", () => {
    expect(service).toBeTruthy();
  });

  it("should call feed(0, 10) and return an array of ArticleFeedItem", () => {
    const authResult = {
      username: "test@test.com",
      name: "Test user",
      role: "generic",
      token: "dummy.token",
    };

    // 1
    service.signin({ username: "test", password: "pass" }).subscribe((res) => {
      //2
      expect(res).toEqual(authResult);
    });

    //3
    const req = httpController.expectOne({
      method: "POST",
      url: `${environment.baseUrl}/auth/signin`,
    });

    //4
    req.flush(authResult);
  });
});
