import { createReducer, on } from "@ngrx/store"
import { initialGlobalProgressState } from "./global-progress.state"
import * as GlobalProgressStateActions from '@store/global-progress/global-progress.actions'

export const reducer = createReducer(
    initialGlobalProgressState,
  on(GlobalProgressStateActions.busy, () => {
    console.log(`Becoming busy`);

    return {
      ready: false
    }
  }),
  on(GlobalProgressStateActions.ready, () => {
    console.log(`Becoming unbusy`);
    return {
      ready: true
    }
  }),
);
  