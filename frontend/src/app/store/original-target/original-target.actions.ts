import { createAction } from "@ngrx/store";
import { props } from "@ngrx/store";


export const authorize = createAction(
  'Authorize',
  props<{target: string}>()
);

export const allow = createAction(
    'Allow'
);

export const complete = createAction(
    'Complete'
);