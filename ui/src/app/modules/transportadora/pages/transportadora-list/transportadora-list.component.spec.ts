import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TransportadoraListComponent } from './transportadora-list.component';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { of } from 'rxjs';
import { TransportadoraService } from '../../services/transportadora.service';

describe('TransportadoraListComponent', () => {
  let component: TransportadoraListComponent;
  let fixture: ComponentFixture<TransportadoraListComponent>;
  let transportadoraService: jasmine.SpyObj<TransportadoraService>;

  beforeEach(async () => {
    const spy = jasmine.createSpyObj('TransportadoraService', ['getAll', 'delete']);
    await TestBed.configureTestingModule({
      imports: [RouterTestingModule, HttpClientTestingModule, TableModule, ButtonModule],
      declarations: [TransportadoraListComponent],
      providers: [
        { provide: TransportadoraService, useValue: spy }
      ]
    }).compileComponents();
    transportadoraService = TestBed.inject(TransportadoraService) as jasmine.SpyObj<TransportadoraService>;
    transportadoraService.getAll.and.returnValue(of([]));
    fixture = TestBed.createComponent(TransportadoraListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
