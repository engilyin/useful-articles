import { Directive, Input } from "@angular/core";
import { AbstractControl, ControlValueAccessor, ValidationErrors, Validator } from "@angular/forms";

@Directive()
export abstract class BaseInput<T> implements ControlValueAccessor, Validator {
  @Input() id!: string;
  @Input() label = '';
  @Input() disabled = false;
  @Input() value: T;
  @Input() errors: { [key: string]: string } = {};

  hasErrors = false;
  touched = false;

  onChange: any = () => {};
  onTouched: any = () => {};

  errorMessage = '';

  constructor() {
    this.value = this.defaultValue();
  }

  abstract defaultValue(): T;

  onInputChange(event: any) {
    this.updateValue(event.target.value);
  }

  updateValue(val: any) {
    this.value = val;
    this.onChange(val);
    this.markAsTouched();
  }

  registerOnChange(onChange: any) {
    this.onChange = onChange;
  }

  registerOnTouched(onTouched: any) {
    this.onTouched = onTouched;
  }

  markAsTouched() {
    this.onTouched();
    this.touched = true;
    this.onChange(this.value);
  }

  writeValue(value: any) {
    if (value) {
      this.value = value;
    }
  }

  setDisabledState?(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }

  validate(control: AbstractControl<any, any>): ValidationErrors | null {
    this.hasErrors = control.touched && !!control.errors;
    this.errorMessage = '';
    if (this.hasErrors) {
      Object.keys(control.errors!).forEach((key) => {
        this.errorMessage += this.errors[key];
        +' ';
      });
    }
    return null;
  }
}
