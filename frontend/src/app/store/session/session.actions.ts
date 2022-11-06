import { createAction } from "@ngrx/store";
import { props } from "@ngrx/store";

export interface SigninParams {
  readonly username?: string;
  readonly password?: string;
}


export const signin = createAction(
  'Signin',
  props<SigninParams>()
);

export const signinSuccess = createAction(
    'Signin Success',
    props<{username: string, name: string, role: string, token: string}>()
);

export const signinFail = createAction(
    'Signin Fail',
    props<{errorMessage: string}>()
);

export const signoff = createAction(
  'Signoff'
);
