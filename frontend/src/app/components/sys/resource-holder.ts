import { Injectable, OnDestroy } from "@angular/core";
import { Subject } from "rxjs";

@Injectable()
export abstract class ResourceHolder implements OnDestroy {

  protected destroy$: Subject<boolean> = new Subject<boolean>();

  constructor() {}

  ngOnDestroy() {
    this.destroy$.next(true);
    this.destroy$.unsubscribe();
  }
}
