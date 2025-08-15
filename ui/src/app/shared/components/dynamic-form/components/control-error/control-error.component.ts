import { Component, ChangeDetectionStrategy, Input, ChangeDetectorRef, OnInit } from '@angular/core';

@Component({
  selector: 'control-error',
  templateUrl: './control-error.component.html',
  styleUrls: ['./control-error.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ControlErrorComponent implements OnInit {

  _text: string | undefined;
  _hide = true;

  @Input() set text(value:any) {
    if (value !== this._text) {
      this._text = value;
      this._hide = !value;
      this.changeDetector.detectChanges();
    }
  }

  constructor(private changeDetector: ChangeDetectorRef) { }

  ngOnInit() { }
}
