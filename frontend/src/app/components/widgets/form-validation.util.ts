import { FormControl, FormGroup } from "@angular/forms";

export const validateAllFormFields = (formGroup: FormGroup) => {
  Object.keys(formGroup.controls).forEach(field => {
    const control = formGroup.get(field);
    if (control instanceof FormControl) {
      control.markAsTouched({ onlySelf: true });
      control.updateValueAndValidity();
    } else if (control instanceof FormGroup) {
      validateAllFormFields(control);
    }
  });
}
