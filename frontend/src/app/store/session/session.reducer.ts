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
