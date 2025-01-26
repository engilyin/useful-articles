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
import { createSelector } from "@ngrx/store";
import { AppState } from "../app.state";

export const selectSession = (state: AppState) => state.session;

export const selectToken = createSelector(
  selectSession,
  (session) => session.token
);

export const selectLoggedin = createSelector(
  selectSession,
  (userState) => userState.token.length > 0
);

export const selectCurrentRole = createSelector(
  selectSession,
  (userState) => userState.role
);

export const selectCurrentUser = createSelector(
  selectSession,
  (userState) => userState.username
);

export const selectCurrentUserName = createSelector(
  selectSession,
  (userState) => userState.name
);

export const selectWelcomeUser = createSelector(
  selectSession,
  (userState) =>
    `Welcome, <b>${userState.name}</b> you are acting as <b>${userState.role}</b>`
);

export const selectAuthError = createSelector(
  selectSession,
  (userState) => userState.lastError
);
