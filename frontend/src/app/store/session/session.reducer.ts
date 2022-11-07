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

import { createReducer, on } from "@ngrx/store";
import { initialSessionState } from "./session.state";
import * as SessionActions from './session.actions';


export const reducer = createReducer(
    initialSessionState,
  on(SessionActions.signin, (state, r) => {
    return {
      ...state,
      username: r.username ?? '',
      lastError: ''
    }
  }),
  on(SessionActions.signinSuccess, (state, r) => {
    return {
        username: r.username,
        name: r.name,
        role: r.role,
        token: r.token,
        lastError: ''
    }
  }),
  on(SessionActions.signinFail, (state, r) => {
    return {
        ...state,
        role: '',
        token: '',
        lastError: r.errorMessage
    }
  })
  ,
  on(SessionActions.signoff, (state) => {
    return {
        ...initialSessionState
    }
  })
);
