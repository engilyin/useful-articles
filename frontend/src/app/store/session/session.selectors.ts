import { createSelector } from '@ngrx/store';
import { AppState } from '../app.state';

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

  export const selectWelcomeUser = createSelector(
    selectSession,
    (userState) => `Welcome, <b>${userState.name}</b> you are acting as <b>${userState.role}</b>`
  );

  export const selectAuthError = createSelector(
    selectSession,
    (userState) => userState.lastError
  );
