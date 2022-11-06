import { createReducer, on } from "@ngrx/store";
import { initialOriginalTargetState } from "./original-target.state";
import * as OriginalTargeActions from './original-target.actions';


export const reducer = createReducer(
    initialOriginalTargetState,
  on(OriginalTargeActions.authorize, (state, r) => {
    console.log(`Remember back path: ${r.target}`)
    return {
      target: r.target
    }
  }),
  on(OriginalTargeActions.complete, () => {
    return {
      ...initialOriginalTargetState
    }
  })
);
