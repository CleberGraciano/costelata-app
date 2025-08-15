/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { FretesListComponent } from './fretes-list.component';

describe('FretesListComponent', () => {
  let component: FretesListComponent;
  let fixture: ComponentFixture<FretesListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FretesListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FretesListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
