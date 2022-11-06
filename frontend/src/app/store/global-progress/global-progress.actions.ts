import { createAction } from "@ngrx/store";

export const busy = createAction(
    'Busy'
);

export const ready = createAction(
    'Ready'
);