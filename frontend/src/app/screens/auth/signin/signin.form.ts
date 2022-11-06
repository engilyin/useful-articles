import { FormBuilder, NonNullableFormBuilder, Validators } from "@angular/forms";

export const signinFormModel = (formBuilder: NonNullableFormBuilder) => formBuilder.group({

    username: ['', Validators.required],
    password: ['', Validators.required],
    remember: [false]
});
